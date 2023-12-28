package org.ulpgc.dacd.model;

import java.time.Instant;

public class Weather {
    private Instant ts;
    private final String ss;
    private Instant predictionTime;
    private double rain;
    private double wind;
    private double temp;
    private int humidity;
    private int cloud;
    private Location location;

    public Weather(Instant ts, String ss, Instant predictionTime, double rain, double wind, double temp, int humidity, int cloud, double latitude, double longitude, String island) {
        this.ts = ts;
        this.ss = ss;
        this.predictionTime = predictionTime;
        this.rain = rain;
        this.wind = wind;
        this.temp = temp;
        this.humidity = humidity;
        this.cloud = cloud;
        this.location = new Location(latitude, longitude, island);
    }
}
