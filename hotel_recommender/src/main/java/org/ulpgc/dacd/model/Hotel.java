package org.ulpgc.dacd.model;


import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private final String name;
    private final String location;
    private final String checkIn;
    private final String checkOut;
    private final String webPage;
    private final double ratePerNight;
    private final double rate;
    private final List<Prediction> predictionList;

    public Hotel(String name, String location, String checkIn, String checkOut, String webPage, double ratePerNight, double rate) {
        this.name = name;
        this.location = location;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.webPage = webPage;
        this.ratePerNight = ratePerNight;
        this.rate = rate;
        this.predictionList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getWebPage() {
        return webPage;
    }

    public double getRate() {
        return rate;
    }

    public List<Prediction> getPredictionList() {
        return predictionList;
    }

    public double averageTemperature() {
        return predictionList.stream().mapToDouble(Prediction::getTemp).average().orElse(0);
    }

    public double averageRain() {
        return predictionList.stream().mapToDouble(Prediction::getRain).average().orElse(0);
    }
}
