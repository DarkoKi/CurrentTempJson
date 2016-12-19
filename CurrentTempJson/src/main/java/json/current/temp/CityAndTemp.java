package json.current.temp;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import json.pdf.Pdf;

public class CityAndTemp {

	private static final String API_URL = "http://api.openweathermap.org/data/2.5/group?id=" + Pdf.citiesToUrl()
			+ "&units=metric&appid=953cba0c9efb27866d89bb56d67162a1";
	private static ArrayList<String> cities = new ArrayList<String>();
	private static LinkedHashSet<String> remDuplCities = new LinkedHashSet<String>();
	private static ArrayList<String> currentTemp = new ArrayList<String>();
	private static ArrayList<String> minTemp = new ArrayList<String>();
	private static ArrayList<String> maxTemp = new ArrayList<String>();
	private static ArrayList<String> pressure = new ArrayList<String>();

	public static ArrayList<String> getCities() {
		return cities;
	}

	public static void setCities(ArrayList<String> cities) {
		CityAndTemp.cities = cities;
	}

	public static LinkedHashSet<String> getRemDuplCities() {
		return remDuplCities;
	}

	public static void setRemDuplCities(LinkedHashSet<String> remDuplCities) {
		CityAndTemp.remDuplCities = remDuplCities;
	}

	public static ArrayList<String> getCurrentTemp() {
		return currentTemp;
	}

	public static void setCurrentTemp(ArrayList<String> currentTemp) {
		CityAndTemp.currentTemp = currentTemp;
	}

	public static ArrayList<String> getMinTemp() {
		return minTemp;
	}

	public static void setMinTemp(ArrayList<String> minTemp) {
		CityAndTemp.minTemp = minTemp;
	}

	public static ArrayList<String> getMaxTemp() {
		return maxTemp;
	}

	public static void setMaxTemp(ArrayList<String> maxTemp) {
		CityAndTemp.maxTemp = maxTemp;
	}

	public static ArrayList<String> getPressure() {
		return pressure;
	}

	public static void setPressure(ArrayList<String> pressure) {
		CityAndTemp.pressure = pressure;
	}

	public static String getApiUrl() {
		return API_URL;
	}

}