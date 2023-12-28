package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelControl {
    public void execute(List<List<String>> hotels, String checkIn, String checkOut) {
        XoteloSupplier xoteloSupplier = new XoteloSupplier();
        JMSHotelStore jmsHotelStore = new JMSHotelStore("tcp://localhost:61616");
        List<Hotel> allhotels = fetchAllHotelData(hotels, checkIn, checkOut, xoteloSupplier);
        for (Hotel hotel: allhotels){
            jmsHotelStore.save(hotel);
        }
    }

    private List<Hotel> fetchAllHotelData(List<List<String>> hotels, String checkIn, String checkOut, XoteloSupplier supplier) {
        List<Hotel> allHotels = new ArrayList<>();
        for (List<String> hotelData : hotels) {
            allHotels.add(supplier.getHotel(hotelData, checkIn, checkOut));
        }
        return allHotels;
    }
}
