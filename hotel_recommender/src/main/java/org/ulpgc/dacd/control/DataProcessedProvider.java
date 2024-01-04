package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;

import java.util.List;

public interface DataProcessedProvider {
    List<Hotel> search(String checkIn, String checkOut);
}
