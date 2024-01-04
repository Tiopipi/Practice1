package org.ulpgc.dacd.model;

public class Prediction {
    private final String predictionTime;
    private final double rain;
    private final double temp;

    public Prediction(String predictionTime, double rain, double temp) {
        this.predictionTime = predictionTime;
        this.rain = rain;
        this.temp = temp;
    }

    public String getPredictionTime() {
        return predictionTime;
    }

    public double getRain() {
        return rain;
    }

    public double getTemp() {
        return temp;
    }
}
