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

    public Weather(Instant ts, Instant predictionTime, double rain, double wind, double temp, int humidity, int cloud, double latitude, double longitude, String island) {
        this.ts = ts;
        this.ss = "OpenWeatherMapSupplier";
        this.predictionTime = predictionTime;
        this.rain = rain;
        this.wind = wind;
        this.temp = temp;
        this.humidity = humidity;
        this.cloud = cloud;
        this.location = new Location(latitude, longitude, island);
    }

    public Instant getTs() {
        return ts;
    }

    public double getRain() {
        return rain;
    }

    public double getWind() {
        return wind;
    }

    public double getTemp() {
        return temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public Location getLocation() {
        return location;
    }

    public int getCloud() {
        return cloud;
    }

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public void setPredictionTime(Instant predictionTime) {
        this.predictionTime = predictionTime;
    }
}
