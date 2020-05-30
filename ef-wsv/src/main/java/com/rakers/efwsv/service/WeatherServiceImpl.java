package com.rakers.efwsv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakers.efwsv.exception.BusinessException;
import com.rakers.efwsv.vo.Weather;
import com.rakers.efwsv.vo.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;


@Service
public class WeatherServiceImpl implements WeatherService {
	private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
	private final static String URL_PREFIX = "http://wthrcdn.etouch.cn/weather_mini?";
	private final static long TIME_OUT = 1800L;

	@Qualifier("restTemplate")
	@Autowired
	private RestTemplate client;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public Weather getWeatherByCityId(String id) {
		String url = URL_PREFIX + "citykey=" + id;
		return this.doGetData(url);
	}

	@Override
	public Weather getWeatherByCityName(String name) {
		String url = URL_PREFIX + "city=" + name;
		return this.doGetData(url);	
	}

	@Override
	public void saveaDataByCityId(String cityId) {
		String url = URL_PREFIX + "citykey=" + cityId;
		try{
			ResponseEntity<String> response = client.getForEntity(url, String.class);
			if(response.getStatusCode() == HttpStatus.OK){
				String dataStr = response.getBody();
				WeatherResponse wr = new ObjectMapper().readValue(dataStr, WeatherResponse.class);
				if("1000".equals(wr.getStatus()) && dataStr != null){
					stringRedisTemplate.opsForValue().set(url, dataStr, TIME_OUT, TimeUnit.SECONDS);
				}else {
					throw new BusinessException("call weather api error");
				}
			}else {
				throw new BusinessException("call weather api error");
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
	}

	//	@ResponseBody
	private Weather doGetData(String url) {
		/**
		 * (1) 从缓存中查询天气数据,如果有则直接返回
		 * 从容器注入StringRedisTemplate类对象srt, 调用方法opsForValue()获取操作接口vot；
		 * 调用srt.hasKey(url)查询缓存中是否存在key: url;
		 * 若存在, 就调用vot.get(url)获得天气数据json串；
		 * 把天气数据json串反序列化得到WeatherResponse对象；
		 * 返回它的data属性；
		 *
		 * (2) 否则就调用接口获取该城市的天气数据，如果接口调用正常，就把数据存入缓存（设置数据保存时间是30分钟），并返回给用户; 否则返回异常消息给用户
		 * 只比原来的doGetData(url)方法多了一步处理：把数据存入缓存。
		 * 因此，复制原来方法的代码，在return语句之前加上一行代码：
		 * vot.set(<键>, <值>, <存储时间>, TimeUnit.SECONDS);
		 * 键: url, 值：天气接口的响应数据json串，存储时间：1800L
		 */
		logger.info("开始获取数据");
		ValueOperations<String, String> vot = stringRedisTemplate.opsForValue();
		if (stringRedisTemplate.hasKey(url)) {
			logger.info("在redis中找到key="+url);
			try {
				String dataStr = vot.get(url);
				WeatherResponse wr = new ObjectMapper().readValue(dataStr, WeatherResponse.class);
				return wr.getData();
			}catch (Exception e){
				logger.error(e.getMessage(), e);
				throw new BusinessException("从redis获取数据时发生错误!");
			}
		}else{
			logger.info("在redis中未找到key");
			try {
				ResponseEntity<String> response = client.getForEntity(url, String.class);
				if(response.getStatusCode() == HttpStatus.OK) {
					String dataStr = response.getBody();
					logger.info("获取到的天气数据"+dataStr);
					WeatherResponse wr = new ObjectMapper().readValue(dataStr, WeatherResponse.class);
					if(wr.getStatus().equals("1000") && dataStr != null) {
						vot.set(url, dataStr, TIME_OUT, TimeUnit.SECONDS);
						return wr.getData();
					}else {
						throw new BusinessException("call weather api error");
					}
				}else {
					throw new BusinessException("call weather api error");
				}
			}catch(BusinessException e) {
				logger.error(e.getMessage(), e);
				throw e;
			}catch(Exception e) {
				logger.error(e.getMessage(), e);
				throw new BusinessException("call weather api error", e);
			}
		}

	}
	
}
