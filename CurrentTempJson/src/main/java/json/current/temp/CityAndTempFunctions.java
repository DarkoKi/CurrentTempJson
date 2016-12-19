package json.current.temp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityAndTempFunctions {

	public static Scanner input = new Scanner(System.in);
	private static Logger logger = Logger.getLogger(CityAndTempFunctions.class);

	public static String callURL(String myURL) throws IOException {

		logger.info("Loading data from API....\n");
		StringBuilder sb = new StringBuilder();
		URL url = new URL(myURL);
		URLConnection urlConn = url.openConnection();
		if (urlConn != null)
			urlConn.setReadTimeout(30 * 1000);
		try (InputStreamReader in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset())) {
			BufferedReader bufferedReader = new BufferedReader(in);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append("[");
				sb.append(line);
				sb.append("]");
			}
			bufferedReader.close();
		}
		return sb.toString();
	}

	public static JSONArray getDataFromApi(JSONArray jsonArray) throws JSONException {

		JSONObject jsonObject = jsonArray.getJSONObject(0);
		JSONArray listArray = jsonObject.getJSONArray("list");
		return listArray;

	}

	public static void showCities(JSONArray listArray) throws JSONException {

		logger.info("All cities for which you can check current temperature:");
		ArrayList<String> liGradova = new ArrayList<String>();
		for (int i = 0; i < listArray.length(); i++) {
			liGradova.add(listArray.getJSONObject(i).getString("name"));
		}
		logger.info(liGradova);
	}

	public static int numOfCities() {

		logger.info("For how many cities you want to view the current temperature(Enter the number):");
		boolean inputNm = true;
		int numCities = 0;
		while (inputNm) {
			try {
				numCities = input.nextInt();
				logger.info("Chosen number of input: " + numCities);
				inputNm = false;
			} catch (Exception e) {
				logger.warn("Please enter the integer number!");
				logger.debug(e, e);
				input.nextLine();
			}
		}
		logger.info("Enter the cities from the list:\n");
		return numCities;
	}

	public static String cityName() {

		Scanner inName = new Scanner(System.in);
		String city = inName.nextLine().toLowerCase();
		return city;
	}

	public static void addValuesFromApi(JSONArray listArray, ArrayList<String> cities, ArrayList<String> cuTemp,
			ArrayList<String> minTe, ArrayList<String> maxTe, ArrayList<String> press) throws JSONException {

		int numCities = numOfCities();

		for (int j = 1; j <= numCities; j++) {
			String cityName;
			logger.info(j + "." + (cityName = cityName()) + "\n");
			for (int i = 0; i < listArray.length(); i++) {

				JSONObject jsonObject = listArray.getJSONObject(i);

				if (jsonObject.getString("name").toLowerCase().equals(cityName)) {

					cities.add(jsonObject.getString("name"));
					cuTemp.add(jsonObject.getJSONObject("main").getString("temp"));
					minTe.add(jsonObject.getJSONObject("main").getString("temp_min"));
					maxTe.add(jsonObject.getJSONObject("main").getString("temp_max"));
					press.add(jsonObject.getJSONObject("main").getString("pressure"));

				}

			}
		}

	}

	public static void eraseDuplicate(ArrayList<String> cities) throws Exception {

		CityAndTemp.getRemDuplCities().addAll(cities);
		cities.clear();
		cities.addAll(CityAndTemp.getRemDuplCities());

	}

	public static void showCitiesAndData(ArrayList<String> cities, ArrayList<String> cuTemp, ArrayList<String> minTe,
			ArrayList<String> maxTe, ArrayList<String> press) throws Exception {

		logger.info("Overview of entered cities and temperatures:\n");
		for (int i = 0; i < cities.size(); i++) {
			logger.info(cities.get(i) + ": Temperatura: " + cuTemp.get(i) + " Min temperatura: " + minTe.get(i)
			+ " Max temperatura: " + maxTe.get(i) + " Pressure: " + press.get(i));
		}

	}

}