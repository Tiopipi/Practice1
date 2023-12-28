package org.ulpgc.dacd.model;

public class Rate {
    private final String name;
    private final double rate;

    public Rate(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }
}
