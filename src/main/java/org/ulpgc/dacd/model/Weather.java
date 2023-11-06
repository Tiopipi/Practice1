package org.ulpgc.dacd.model;

import java.time.Instant;

public class Weather {
    private Instant ts;
    private float rain;
    private float wind;
    private float temp;
    private int humidity;
    private int cloud;
    private Location location;

    public Weather(Instant ts, float rain, float wind, float temp, int humidity, int cloud, float latitude, float longitude, String island) {
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

    public float getRain() {
        return rain;
    }

    public void setRain(float rain) {
        this.rain = rain;
    }

    public float getWind() {
        return wind;
    }

    public void setWind(float wind) {
        this.wind = wind;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
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
}
