package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;

import java.util.List;

public interface HotelSupplier {
    Hotel getHotel(List<String> hotelData, String checkIn, String checkOut);
}
