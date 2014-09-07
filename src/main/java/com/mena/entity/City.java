package com.mena.entity;

import java.util.Date;
import java.sql.Time;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CITY")
public class City implements java.io.Serializable {
	
	private static final long serialVersionUID = 6929952258544189851L;

	@Id
	@Column(name = "CITY_ID", unique = true)
	private int cityID;

	@Column(name = "CITY_NAME")
	private String cityName;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "COORD_LON")
	private double lon;

	@Column(name = "COORD_LAT")
	private double lat;

	@Column(name = "SUNRISE")
	private Time sunrise;

	@Column(name = "SUNSET")
	private Time sunset;

	@Column(name = "TEMP_AVG")
	private double tempAvg;

	@Column(name = "TEMP_MIN")
	private double tempMin;

	@Column(name = "TEMP_MAX")
	private double tempMax;

	@Column(name = "LAST_FETCHED")
	private Date lastFetched;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "city")
	private Set<Weather> weather;

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public Time getSunrise() {
		return sunrise;
	}

	public void setSunrise(Time sunrise) {
		this.sunrise = sunrise;
	}

	public Time getSunset() {
		return sunset;
	}

	public void setSunset(Time sunset) {
		this.sunset = sunset;
	}

	public double getTempAvg() {
		return tempAvg;
	}

	public void setTempAvg(double tempAvg) {
		this.tempAvg = tempAvg;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	public Date getLastFetched() {
		return lastFetched;
	}

	public void setLastFetched(Date lastFetched) {
		this.lastFetched = lastFetched;
	}

	public Set<Weather> getWeather() {
		return weather;
	}

	public void setWeather(Set<Weather> weather) {
		this.weather = weather;
	}
}