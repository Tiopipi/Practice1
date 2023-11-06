package org.ulpgc.dacd.model;

public class Location {
    private float latitude;
    private float longitude;
    private String island;

    public Location(float latitude, float longitude, String island) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.island = island;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }
}
