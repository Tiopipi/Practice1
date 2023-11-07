package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

public class Main {
    public static void main(String[] args) {
        Location GranCanaria = new Location(44.34, 10.99, "GC");
        Location Tenerife = new Location(44.34, 10.99, "TN");
        OpenWeatherMapSupplier openWeatherMapSupplier = new OpenWeatherMapSupplier();
        LocalTime hour = LocalTime.of(12, 0);
        ZoneId timeZone = ZoneId.of("Atlantic/Canary");
        Instant ts = hour.atDate(java.time.LocalDate.of(2023, 11, 8)).atZone(timeZone).toInstant();
        Weather weather = openWeatherMapSupplier.getWeather(GranCanaria, ts);
        System.out.println(weather);
    }
}