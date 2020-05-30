package com.rakers.ecsv.controller;


import com.rakers.ecsv.service.CityService;
import com.rakers.ecsv.vo.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/csv")
public class CityController {

    @Autowired
    private CityService ctsv;

    @GetMapping("/cities")
    public List<City> getAll(){
        return this.ctsv.getCityList();
    }

}
