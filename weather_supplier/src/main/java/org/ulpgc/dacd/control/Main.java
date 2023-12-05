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
        return List.of(new Location(28.096253755181895, -15.478166412366257, "GranCanaria"),
                new Location(28.466621443595226, -16.294065389577884, "Tenerife"),
                new Location(27.769472210164082, -17.92350974098833, "ElHierro"),
                new Location(28.667040110674396, -17.782705553444686, "LaPalma"),
                new Location(28.100626419548533, -17.153456433163814, "LaGomera"),
                new Location(28.571843425986817, -13.929365755108329, "Fuerteventura"),
                new Location(29.02285617641442, -13.641930977756244, "Lanzarote"),
                new Location(29.26457226233749, -13.498213609717018, "LaGraciosa"));
    }
}