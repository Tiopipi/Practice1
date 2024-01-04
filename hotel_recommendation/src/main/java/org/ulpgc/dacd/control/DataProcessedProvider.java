package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;

import java.util.List;

public interface DataProcessedProvider {
    List<Hotel> read(String checkIn, String checkOut);
}
