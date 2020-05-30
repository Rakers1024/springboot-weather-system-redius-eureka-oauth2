package com.rakers.efwsv.job;

import com.rakers.efwsv.service.CityServiceClient;
import com.rakers.efwsv.service.WeatherService;
import com.rakers.efwsv.vo.City;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class SyncJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(SyncJob.class);

    @Autowired
    private CityServiceClient client;

    @Autowired
    private WeatherService weatherService;

    /**
     * 保存所有城市数据
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        logger.info("开始同步天气数据!");
        try {
            List<City> citys = this.client.getCityList();
            for (City city : citys) {
                weatherService.saveaDataByCityId(city.getCityId());
            }
            logger.info("天气数据同步成功!");
        }catch (Exception e){
            logger.error(e.getMessage(), e);

        }
    }
}
