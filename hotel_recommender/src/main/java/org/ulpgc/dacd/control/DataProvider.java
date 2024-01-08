package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;

import java.util.List;

public interface DataProvider {
    List<Hotel> searchAvailableHotels(String checkIn, String checkOut);
    List<Hotel> searchRecommendedHotels(List<Hotel> hotels,  int preferredTemperature);
}
