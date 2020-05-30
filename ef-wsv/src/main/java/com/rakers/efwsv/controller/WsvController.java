package com.rakers.efwsv.controller;

import com.rakers.efwsv.service.WeatherService;
import com.rakers.efwsv.vo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wsv")
public class WsvController {

    @Autowired
    private WeatherService wtsv;

    @GetMapping("/cityId/{id}")
    public Weather getWeatherByCityId(@PathVariable String id) {
        return this.wtsv.getWeatherByCityId(id);
    }

    @GetMapping("/cityName/{name}")
    public Weather getWeatherByCityName(@PathVariable String name) {
        return this.wtsv.getWeatherByCityName(name);
    }

}
