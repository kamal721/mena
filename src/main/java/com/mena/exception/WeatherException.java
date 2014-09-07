package com.mena.exception;

public class WeatherException extends Exception {

	private static final long serialVersionUID = 6387477047092726784L;
	
	private int code;
	private String message;

	public WeatherException(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}