package org.ulpgc.dacd.model;

import java.time.Instant;

public class Weather {
    private Instant ts;
    private double rain;
    private double wind;
    private double temp;
    private int humidity;
    private int cloud;
    private Location location;

    public Weather(Instant ts, double rain, double wind, double temp, int humidity, int cloud, double latitude, double longitude, String island) {
        this.ts = ts;
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

    public void setTs(Instant ts) {
        this.ts = ts;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getCloud() {
        return cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "ts=" + ts +
                ", rain=" + rain +
                ", wind=" + wind +
                ", temp=" + temp +
                ", humidity=" + humidity +
                ", cloud=" + cloud +
                ", location=" + location +
                '}';
    }
}
