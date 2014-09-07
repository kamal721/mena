package com.mena.service;

import com.mena.entity.City;
import com.mena.exception.WeatherException;
import com.mena.idao.IWeatherDAO;
import com.mena.util.WeatherConstants;
import com.mena.util.WeatherUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class will handles the web service requested for path "weather"
 * 
 */
@Service("weatherService")
public class WeatherService {

	@Autowired
	private IWeatherDAO weatherDAO;
	
	@Autowired
	private WeatherUtil weatherUtil;
	
	/** This method used to fetch the current weather detail of the given city.
	 * @param cityName
	 * @return Object holds city weather details
	 * @throws JSONException
	 */
	@GET
	@Path("/current")
	@Produces("application/json")
	public String getCurrent(@QueryParam(WeatherConstants.Q) final String cityName) throws JSONException
	{
		JSONObject response;
		try
		{
			City cityWeather = weatherDAO.getCityWeather(cityName);
			if(cityWeather == null)
			{
				response = weatherUtil.fetchCityWeather(cityName);
				cityWeather = weatherUtil.createCityEntity(response);
				weatherDAO.saveCityWeather(cityWeather);
			}
			else if (!weatherUtil.isTodayWeather(cityWeather))
			{
				response = weatherUtil.fetchCityWeather(cityName);
				cityWeather = weatherUtil.createCityEntity(response);
				weatherDAO.updateCityWeather(cityWeather);
			}
			response = weatherUtil.prepareResponseObject(cityWeather);
		}
		catch(WeatherException e)
		{
			response = new JSONObject();
			response.put(WeatherConstants.MESSAGE, e.getMessage());
		}
		return response.toString();
	}
}