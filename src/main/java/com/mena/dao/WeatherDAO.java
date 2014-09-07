package com.mena.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mena.entity.City;
import com.mena.entity.Weather;
import com.mena.exception.WeatherException;
import com.mena.idao.IWeatherDAO;
import com.mena.util.HibernateUtil;
import com.mena.util.WeatherConstants;

/**
 * This class will handles all database action requested from service layer.
 *
 */
@Repository
public class WeatherDAO implements IWeatherDAO {
	
	@Autowired
	private HibernateUtil hibernateUtil;
	
	private SessionFactory factory;
	private Session session;
	private Transaction transaction;
	
	/* (non-Javadoc)
	 * @see com.mena.idao.IWeatherDAO#getCityWeather(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public City getCityWeather(final String cityName) throws WeatherException
	{
		City city = null;
		try
		{
			factory = hibernateUtil.getFactory();
			session = factory.openSession();
			transaction = session.beginTransaction();
			List<City> cities = session.createQuery("FROM City where cityName='" + cityName + "'").list();
			for (Iterator<City> iterator = cities.iterator(); iterator.hasNext();)
			{
				city = (City) iterator.next();
			}
			transaction.commit();
		}
		catch (HibernateException e)
		{
			throw new WeatherException(500, WeatherConstants.DB_ERR);
		}
		finally
		{
			session.close();
		}
		return city;
	}

	/* (non-Javadoc)
	 * @see com.mena.idao.IWeatherDAO#saveOrUpdateCityWeather(com.mena.entity.City)
	 */
	public void saveCityWeather(final City city) throws WeatherException
	{
		try
		{
			factory = hibernateUtil.getFactory();
			session = factory.openSession();
			transaction = session.beginTransaction();
			session.save(city);
			this.saveWeatherSet(city);
			transaction.commit();
		}
		catch (HibernateException e)
		{
			throw new WeatherException(500, WeatherConstants.DB_ERR);
		}
		finally
		{
			session.close();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mena.idao.IWeatherDAO#updateCityWeather(com.mena.entity.City)
	 */
	public void updateCityWeather(final City city) throws WeatherException
	{
		try
		{
			factory = hibernateUtil.getFactory();
			session = factory.openSession();
			transaction = session.beginTransaction();
			session.update(city);
			this.deleteWeatherSet(city.getCityID());
			this.saveWeatherSet(city);
			transaction.commit();
		}
		catch (HibernateException e)
		{
			throw new WeatherException(500, WeatherConstants.DB_ERR);
		}
		finally
		{
			session.close();
		}
	}
	
	/**
	 * This method used to save whether set, before saving/updating the city entity
	 * @param weatherSet
	 */
	private void saveWeatherSet(City city)
	{
		Weather weather;
		Set<Weather> weatherSet = city.getWeather();
		for (Iterator<Weather> iterator = weatherSet.iterator(); iterator.hasNext();)
		{
			weather = iterator.next();
			weather.setCity(city);
			session.save(weather);
		}
	}
	
	/**
	 * This method used to delete the weather rows for given city
	 * @param cityID
	 */
	private void deleteWeatherSet(int cityID)
	{
		Query query = session.createQuery("DELETE FROM Weather where CITY_ID="+cityID);
		query.executeUpdate();
	}
}