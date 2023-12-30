package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenWeatherMapSupplier implements WeatherSupplier {

    public List<Weather> getWeather(Location location, List<Instant> instants, String apikey) {
        List<Weather> weathers = new ArrayList<>();
        try {
            String apiUrl = buildApiUrl(location, apikey);
            String jsonResponse = fetchDataFromApi(apiUrl);
            weathers = deserializeWeatherData(jsonResponse, instants, location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weathers;
    }

    private String buildApiUrl(Location location, String apikey) {
        return "https://api.openweathermap.org/data/2.5/forecast?" +
                        "lat=" + location.getLatitude() +
                        "&lon=" + location.getLongitude() +
                        "&appid=" + apikey +
                        "&units=metric";
    }

    private String fetchDataFromApi(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error: " + responseCode);
        } else {
            return readApiResponse(url);
        }
    }

    private String readApiResponse(URL url) throws IOException {
        StringBuilder informationString = new StringBuilder();
        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
        }
        return informationString.toString();
    }

    private List<Weather> deserializeWeatherData(String jsonResponse, List<Instant> instants, Location location) {
        List<Weather> weathers = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray list = jsonObject.getAsJsonArray("list");
        for (int i = 0; i < list.size(); i++) {
            JsonObject object = list.get(i).getAsJsonObject();
            String date = object.get("dt_txt").getAsString();
            String dateFormatted = formatDate(date);
            for (Instant predictionTime : instants) {
                if (dateFormatted.equals(String.valueOf(predictionTime))) {
                    Weather weather = createWeatherObject(object, predictionTime, location);
                    weathers.add(weather);
                    break;
                }
            }
        }
        return weathers;
    }

    private String formatDate(String date) {
        return date.substring(0, 10) + "T" + date.substring(11) + "Z";
    }

    private Weather createWeatherObject(JsonObject object, Instant predictionTime, Location location) {
        JsonObject wind = object.get("wind").getAsJsonObject();
        JsonObject main = object.get("main").getAsJsonObject();
        JsonObject clouds = object.get("clouds").getAsJsonObject();
        return new Weather(Instant.now().truncatedTo(ChronoUnit.SECONDS),
                "OpenWeatherMapSupplier",
                predictionTime,
                object.get("pop").getAsDouble(),
                wind.get("speed").getAsDouble(),
                main.get("temp").getAsDouble(),
                main.get("humidity").getAsInt(),
                clouds.get("all").getAsInt(),
                location.getLatitude(),
                location.getLongitude(),
                location.getLocationName());
    }
}
