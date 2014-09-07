package com.mena.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "WEATHER")
public class Weather implements java.io.Serializable {
	
	private static final long serialVersionUID = 135817694934108367L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "WEATHER_ID", unique = true, nullable = false)
	private int weatherID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CITY_ID")
	private City city;

	@Column(name = "CONTEXT")
	private String context;

	@Column(name = "DESCRIPTION")
	private String description;

	public int getWeatherID() {
		return weatherID;
	}

	public void setWeatherID(int weatherID) {
		this.weatherID = weatherID;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}