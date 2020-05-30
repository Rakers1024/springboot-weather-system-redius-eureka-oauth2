package com.rakers.efwsv.service;


import com.rakers.efwsv.vo.Weather;

public interface WeatherService {
	Weather getWeatherByCityId(String id);
	Weather getWeatherByCityName(String name);

	/**
	 * 保存天气数据
	 * @param cityId	城市id
	 */
	void saveaDataByCityId(String cityId);
}
