package com.mena.util;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;

import org.apache.cxf.jaxrs.client.WebClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.mena.entity.City;
import com.mena.entity.Weather;
import com.mena.exception.WeatherException;

/**
 * This class will provides util methods to service layer
 *
 */
@Component
public class WeatherUtil {

	/**
	 * This method will fetch the weather details from openweathermap.org site via CXF/JAX-RS web service call
	 * @param cityName
	 * @return JSONObject which will contains the city weather details.
	 * @throws WeatherException
	 */
	public JSONObject fetchCityWeather(final String cityName) throws WeatherException
	{
		WebClient client;
		JSONObject respObj = null;
		try
		{
			client = WebClient.create(WeatherConstants.OPEN_WEATHER_URI).path(WeatherConstants.WEATHER).query(WeatherConstants.Q, cityName);
			respObj = new JSONObject(client.get(String.class));
			if(respObj.equals(null) || (respObj.has(WeatherConstants.MESSAGE) && respObj.get(WeatherConstants.MESSAGE).equals(WeatherConstants.OPEN_WEATHER_ERR)))
			{
				throw new WeatherException(404, WeatherConstants.CITY_NOT_FOUND_ERR);
			}	
			return respObj;
		}
		catch(JSONException e)
		{
			throw new WeatherException(500, WeatherConstants.JSON_ERR);
		}	
	}
	
	/**
	 * This method will compare the current date with weather details fetched date.
	 * @param cityWeather
	 * @return true if the weather details was fetched on today, else false.
	 */
	public boolean isTodayWeather(final City cityWeather)
	{
		SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
		String fetchedDate = ft.format(cityWeather.getLastFetched());
		String today = ft.format(new Date());
		if(fetchedDate.equals(today))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This method used to convert JSON to City entity object to perform persistence action.
	 * @param json
	 * @return city object which hold the weather details.
	 * @throws JSONException
	 */
	public City createCityEntity(final JSONObject json) throws JSONException
	{
		City city = new City();
		city.setCityID(json.getInt(WeatherConstants.ID));
		city.setCityName(json.getString(WeatherConstants.NAME));
		city.setCountry(json.getJSONObject(WeatherConstants.SYS).getString(WeatherConstants.COUNTRY));
		city.setLastFetched(new Date((long)(json.getInt(WeatherConstants.DT))*1000));
		city.setLat((Double) (json.getJSONObject(WeatherConstants.COORD).get(WeatherConstants.LAT)));
		city.setLon((Double) (json.getJSONObject(WeatherConstants.COORD).get(WeatherConstants.LON)));
		city.setSunrise(new Time((long)(json.getJSONObject(WeatherConstants.SYS).getInt(WeatherConstants.SUN_RISE))*1000));
		city.setSunset(new Time((long)(json.getJSONObject(WeatherConstants.SYS).getInt(WeatherConstants.SUN_SET))*1000));
		city.setTempAvg((Double)(json.getJSONObject(WeatherConstants.MAIN).get(WeatherConstants.TEMP))-273.00);
		city.setTempMin((Double)(json.getJSONObject(WeatherConstants.MAIN).get(WeatherConstants.TEMP_MIN))-273.00);
		city.setTempMax((Double)(json.getJSONObject(WeatherConstants.MAIN).get(WeatherConstants.TEMP_MAX))-273.00);
		city.setWeather(this.getWeatherSet(json.getJSONArray(WeatherConstants.WEATHER)));
		return city;
	}
	
	/**
	 * This method used to convert JSON to entity object to perform persistence action.
	 * @param json
	 * @return Weather object holds weather details
	 * @throws JSONException
	 */
	public Weather createWeatherEntity(JSONObject json) throws JSONException
	{
		Weather weather = new Weather();
		weather.setContext(json.getString(WeatherConstants.MAIN));
		weather.setDescription(json.getString(WeatherConstants.DESCRIPTION));
		return weather;
	}
	
	/**
	 * This method used to get the set of weather object from JSON array
	 * @param array which holds the weather objects.
	 * @return set of weather objects
	 * @throws JSONException
	 */
	private Set<Weather> getWeatherSet(JSONArray array) throws JSONException 
	{
		Set<Weather> weatherSet = new HashSet<Weather>();
		for(int i=0; i<array.length(); i++)
		{
			weatherSet.add(this.createWeatherEntity(array.getJSONObject(i)));
		}
		return weatherSet;
	}
	
	/**
	 * This method used to form repose JSON object from entity object.
	 * @param cityWeather
	 * @return JSONObject holding city weather details.
	 * @throws JSONException
	 */
	public JSONObject prepareResponseObject(final City cityWeather) throws JSONException
	{
		JSONObject response = new JSONObject();
		JSONObject city = new JSONObject();
		JSONObject coord = new JSONObject();
		JSONObject main = new JSONObject();
		JSONArray weatherArray = new JSONArray();
		JSONObject weatherObj;
		Weather weather;
		Set<Weather> weatherSet;
		SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		ft.setTimeZone(TimeZone.getTimeZone("GMT"));
		response.put(WeatherConstants.DATE, ft.format(new Date()));
		city.put(WeatherConstants.NAME, cityWeather.getCityName());
		city.put(WeatherConstants.COUNTRY, cityWeather.getCountry());
		city.put(WeatherConstants.SUN_RISE, cityWeather.getSunrise().toString().substring(0,5));
		city.put(WeatherConstants.SUN_SET, cityWeather.getSunset().toString().substring(0,5));
		coord.put(WeatherConstants.LON, (Double) cityWeather.getLon());
		coord.put(WeatherConstants.LAT, (Double) cityWeather.getLat());
		city.put(WeatherConstants.COORD, coord);
		response.put(WeatherConstants.CITY, city);
		weatherSet = cityWeather.getWeather();
		for (Iterator<Weather> iterator = weatherSet.iterator(); iterator.hasNext();)
		{
			weather = iterator.next();
			weatherObj = new JSONObject();
			weatherObj.put(WeatherConstants.MAIN, weather.getContext());
			weatherObj.put(WeatherConstants.DESCRIPTION, weather.getDescription());
			weatherArray.put(weatherObj);
		}
		response.put(WeatherConstants.WEATHER, weatherArray);
		main.put(WeatherConstants.TEMP, (Double) cityWeather.getTempAvg());
		main.put(WeatherConstants.TEMP_MIN, (Double) cityWeather.getTempMin());
		main.put(WeatherConstants.TEMP_MAX, (Double) cityWeather.getTempMax());
		response.put(WeatherConstants.MAIN, main);
		return response;
	}	
}