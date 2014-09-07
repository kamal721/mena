package com.mena.idao;

import com.mena.entity.City;
import com.mena.exception.WeatherException;

/**
 * This interface will handles all database action requested from service layer.
 *
 */
public interface IWeatherDAO {

	/**
	 * This method used to get the city weather details from database throw hibernate persistence call.
	 * @param cityName
	 * @return city object holding weather detail.
	 * @throws WeatherException
	 */
	public City getCityWeather(final String cityName) throws WeatherException;

	/**
	 * This method used to save the city weather details to database throw hibernate persistence call.
	 * @param city
	 * @throws WeatherException
	 */
	public void saveCityWeather(final City city) throws WeatherException;
	
	/**
	 * This method used to update the city weather details to database throw hibernate persistence call.
	 * @param city
	 * @throws WeatherException
	 */
	public void updateCityWeather(final City city) throws WeatherException;
}
