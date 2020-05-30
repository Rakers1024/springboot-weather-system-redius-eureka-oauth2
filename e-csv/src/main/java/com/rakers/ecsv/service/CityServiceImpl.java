package com.rakers.ecsv.service;


import com.rakers.ecsv.exception.BusinessException;
import com.rakers.ecsv.utils.XmlBuilder;
import com.rakers.ecsv.vo.City;
import com.rakers.ecsv.vo.CityList;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;


@Service
public class CityServiceImpl implements CityService {

	@Override
	public List<City> getCityList() {
		Resource rsc = new ClassPathResource("citylist.xml");
		
		try {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(rsc.getInputStream(), "utf-8"));
			StringBuffer bf = new StringBuffer();
			String line = bfr.readLine();
			
			while(line != null) {
				bf.append(line);
				line = bfr.readLine();
			}
			
			String content = bf.toString();
			CityList cl = (CityList) XmlBuilder.xmlStrToObject(CityList.class, content);
			return cl.getCityList();
		}catch(Exception e) {
			throw new BusinessException("get city data error", e);
		}
	}

}
