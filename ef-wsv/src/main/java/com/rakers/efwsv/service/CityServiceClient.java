package com.rakers.efwsv.service;

import com.rakers.efwsv.vo.City;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("city-service")
public interface CityServiceClient {

    @GetMapping("/csv/cities")
    List<City> getCityList();
}
