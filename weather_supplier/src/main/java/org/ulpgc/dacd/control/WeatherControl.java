package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WeatherControl {
    public void execute(List<Location> locations, String apikey) {
        JMSWeatherStore jmsWeatherStore = new JMSWeatherStore("tcp://localhost:61616");
        OpenWeatherMapSupplier openWeatherMapSupplier = new OpenWeatherMapSupplier();
        List<Weather> allWeathers = fetchAllWeatherData(locations, apikey, openWeatherMapSupplier);
        Gson gson = prepareGson();
        for (Weather weather: allWeathers){
            String weatherSerialized = gson.toJson(weather);
            jmsWeatherStore.save(weatherSerialized);
        }
    }

    private List<Weather> fetchAllWeatherData(List<Location> locations, String apikey, OpenWeatherMapSupplier supplier) {
        List<Weather> allWeathers = new ArrayList<>();
        for (Location island : locations) {
            List<Weather> weathers = supplier.getWeather(island, createInstantList(), apikey);
            allWeathers.addAll(weathers);
        }
        return allWeathers;
    }

    public static List<Instant> createInstantList() {
        List<Instant> dateList = new ArrayList<>();
        Instant now = Instant.now();
        for (int i = 0; i <= 4; i++) {
            Instant date = LocalDateTime.ofInstant(now, ZoneOffset.UTC)
                    .plusDays(i)
                    .toLocalDate()
                    .atTime(LocalTime.of(12, 0, 0))
                    .toInstant(ZoneOffset.UTC)
                    .truncatedTo(ChronoUnit.SECONDS);
            dateList.add(date);
        }
        return dateList;
    }

    private static Gson prepareGson(){
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
                    @Override
                    public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
                        jsonWriter.value(instant.toString());
                    }

                    @Override
                    public Instant read(JsonReader jsonReader) throws IOException {
                        return Instant.parse(jsonReader.nextString());
                    }
                }).create();
    }
}
