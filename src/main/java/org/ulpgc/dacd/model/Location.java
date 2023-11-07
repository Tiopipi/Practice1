package org.ulpgc.dacd.model;

public class Location {
    private double latitude;
    private double longitude;
    private String island;

    public Location(double latitude, double longitude, String island) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.island = island;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", island='" + island + '\'' +
                '}';
    }
}
