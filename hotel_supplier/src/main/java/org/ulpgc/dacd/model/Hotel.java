package org.ulpgc.dacd.model;

import java.time.Instant;
import java.util.List;

public class Hotel {
    private Instant ts;
    private final String ss;
    private final String name;
    private final String location;
    private final String checkIn;
    private final String checkOut;
    private final List<Rate> rates;

    public Hotel(Instant ts, String ss, String name, String location, String checkIn, String checkOut, List<Rate> rates) {
        this.ts = ts;
        this.ss = ss;
        this.name = name;
        this.location = location;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.rates = rates;
    }
}
