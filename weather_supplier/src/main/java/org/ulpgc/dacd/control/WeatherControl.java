package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WeatherControl {
    public void execute(List<Location> locations, String apikey) {
        JMSWeatherStore jmsWeatherStore = new JMSWeatherStore("tcp://localhost:61616");
        OpenWeatherMapSupplier openWeatherMapSupplier = new OpenWeatherMapSupplier();
        List<Weather> allWeathers = fetchAllWeatherData(locations, apikey, openWeatherMapSupplier);
        for (Weather weather: allWeathers){
            jmsWeatherStore.save(weather);
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
}
