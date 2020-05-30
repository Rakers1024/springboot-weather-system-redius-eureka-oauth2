package com.rakers.weather8systemawrp.controller;


import com.rakers.weather8systemawrp.exception.BusinessException;
import com.rakers.weather8systemawrp.vo.City;
import com.rakers.weather8systemawrp.vo.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/weather")
public class WeatherController {

	private static final Logger logger =LoggerFactory.getLogger(WeatherController.class);

	private static final String URL_CVS = "http://localhost:8060/csv/cities";
	private static final String URL_WSV_CITYID = "http://localhost:8060/wsv/cityId/{id}";

	@Autowired
	private RestTemplate client;
	
	@GetMapping("/cities")
	public ResponseEntity<City[]> getCityList(){
		try {
			return this.client.getForEntity(URL_CVS, City[].class);
		}catch (Exception e){
			logger.error("call city service error", e);
			throw new BusinessException("call city service error", e);
		}
	}
	
	@GetMapping("/cityId/{id}")
	public Weather getWeatherByCityId(@PathVariable String id) {
		try {
			return this.client.getForObject(URL_WSV_CITYID, Weather.class, id);
		}catch (Exception e){
			logger.error("call Weather service error", e);
			throw new BusinessException("call Weather service error", e);
		}
	}
	

}
