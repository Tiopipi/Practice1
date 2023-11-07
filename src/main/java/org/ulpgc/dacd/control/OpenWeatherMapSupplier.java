package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

public class OpenWeatherMapSupplier implements WeatherSupplier{

    public Weather getWeather(Location location, Instant ts) {
        //List<Weather> cache;
        String apikey  = "3329b12f4fce739e2aa34dd2306c876f";
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?lat="
                    + String.valueOf(location.getLatitude()) + "&lon="
                    + String.valueOf(location.getLatitude()) + "&appid=" + apikey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Ocurrió el error " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()){
                    informationString.append(scanner.nextLine());
                }
                scanner.close();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(String.valueOf(informationString), JsonObject.class);
                JsonArray jsonArray = jsonObject.getAsJsonArray("list");
                for (int i = 0; i < jsonArray.size(); i++){
                    JsonObject object = jsonArray.get(i).getAsJsonObject();
                    String element = object.get("dt_txt").getAsString();
                    String date = element.substring(0,10) + "T" + element.substring(11) + "Z";
                    if (date.equals(String.valueOf(ts))){
                        JsonObject wind = object.get("wind").getAsJsonObject();
                        JsonObject main = object.get("main").getAsJsonObject();
                        JsonObject clouds = object.get("clouds").getAsJsonObject();
                        return new Weather(ts,
                                object.get("pop").getAsDouble(),
                                wind.get("speed").getAsDouble(),
                                main.get("temp").getAsDouble(),
                                main.get("humidity").getAsInt(),
                                clouds.get("all").getAsInt(),
                                location.getLatitude(),
                                location.getLongitude(),
                                location.getIsland()
                                );
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
