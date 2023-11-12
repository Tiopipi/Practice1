package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenWeatherMapSupplier implements WeatherSupplier {

    public List<Weather> getWeather(Location location, List<Instant> instants, String apikey) {
        List<Weather> weathers = new ArrayList<>();
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?lat="
                    + location.getLatitude() + "&lon="
                    + location.getLongitude() + "&appid=" + apikey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Ocurrió el error " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(String.valueOf(informationString), JsonObject.class);
                JsonArray jsonArray = jsonObject.getAsJsonArray("list");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject object = jsonArray.get(i).getAsJsonObject();
                    String date = object.get("dt_txt").getAsString();
                    String dateFormated = date.substring(0, 10) + "T" + date.substring(11) + "Z";
                    for (Instant ts : instants) {
                        if (dateFormated.equals(String.valueOf(ts))) {
                            JsonObject wind = object.get("wind").getAsJsonObject();
                            JsonObject main = object.get("main").getAsJsonObject();
                            JsonObject clouds = object.get("clouds").getAsJsonObject();
                            weathers.add(new Weather(ts,
                                    object.get("pop").getAsDouble(),
                                    wind.get("speed").getAsDouble(),
                                    main.get("temp").getAsDouble(),
                                    main.get("humidity").getAsInt(),
                                    clouds.get("all").getAsInt(),
                                    location.getLatitude(),
                                    location.getLongitude(),
                                    location.getIsland())
                            );
                            break;
                        }
                    }
                }
                return weathers;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
