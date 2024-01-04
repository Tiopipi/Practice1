package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HotelControl {
    public void execute(List<List<String>> hotels) {
        XoteloSupplier xoteloSupplier = new XoteloSupplier();
        JMSHotelStore jmsHotelStore = new JMSHotelStore("tcp://localhost:61616");
        List<List<String>> allPossibleCheckInCheckOut = loadCheckInCheckOut();
        List<Hotel> allHotels = fetchAllHotelData(hotels, allPossibleCheckInCheckOut, xoteloSupplier);
        for (Hotel hotel : allHotels) {
            jmsHotelStore.save(hotel);
        }
    }

    private List<List<String>> loadCheckInCheckOut() {
        LocalDate today = LocalDate.now();
        List<List<String>> allPossibleCheckInCheckOut = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (ZonedDateTime.now(ZoneOffset.UTC).toLocalTime().isAfter(LocalTime.of(12, 0))) {
            for (int i = 1; i < 5; i++) {
                LocalDate checkIn = today.plusDays(i);
                for (int j = i + 1; j < 5; j++) {
                    LocalDate checkOut = today.plusDays(j);
                    List<String> checkInCheckOutList = new ArrayList<>();
                    checkInCheckOutList.add(checkIn.format(dateFormat));
                    checkInCheckOutList.add(checkOut.format(dateFormat));
                    allPossibleCheckInCheckOut.add(checkInCheckOutList);
                }
            }
        } else {
            for (int i = 0; i < 5; i++) {
                LocalDate checkIn = today.plusDays(i);
                for (int j = i + 1; j < 5; j++) {
                    LocalDate checkOut = today.plusDays(j);
                    List<String> checkInCheckOutList = new ArrayList<>();
                    checkInCheckOutList.add(checkIn.format(dateFormat));
                    checkInCheckOutList.add(checkOut.format(dateFormat));
                    allPossibleCheckInCheckOut.add(checkInCheckOutList);
                }
            }
        }
        return allPossibleCheckInCheckOut;
    }

    private List<Hotel> fetchAllHotelData(List<List<String>> hotels, List<List<String>> allPossibleCheckInCheckOut, XoteloSupplier supplier) {
        List<Hotel> allHotels = new ArrayList<>();
        for (List<String> hotelData : hotels) {
            for (List<String> checkInCheckOut : allPossibleCheckInCheckOut)
                allHotels.add(supplier.getHotel(hotelData, checkInCheckOut.get(0), checkInCheckOut.get(1)));
        }
        return allHotels;
    }
}
