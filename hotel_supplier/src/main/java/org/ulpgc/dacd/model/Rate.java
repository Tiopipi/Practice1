package org.ulpgc.dacd.model;

public class Rate {
    private final String name;
    private final double rate;

    public Rate(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "name='" + name + '\'' +
                ", rate=" + rate +
                '}';
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }
}
