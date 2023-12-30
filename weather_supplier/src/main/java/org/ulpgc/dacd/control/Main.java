package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        String apikey = args[0];
        List<Location> locations = loadLocations();
        WeatherControl weatherControl = new WeatherControl();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                weatherControl.execute(locations, apikey);
            }
        }, 0, 6 * 60 * 60 * 1000);
    }

    private static List<Location> loadLocations() {
        return List.of(new Location(51.506325025678166, -0.12132929527393645, "London"),
                new Location(40.71194586603333, -74.01539798287953, "NewYork"),
                new Location(37.390094992095186, -5.983726447127279, "Seville"),
                new Location(20.65589972890631, -105.2137071579315, "VallartaPort"),
                new Location(41.157870708400324, -8.629164141981454, "Porto"),
                new Location(36.1758962296752, -115.13999320057034, "Nevada"),
                new Location(48.85769609436529, 2.350465706183786, "Paris"),
                new Location(50.84750868345027, 4.359621826036103, "Brussels"),
                new Location(39.47165635279633, -0.37696373796336824, "Valencia"),
                new Location(35.71287933357788, 139.65700715365136, "Tokio"));
    }
}