package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BusinessLogic {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public List<Hotel> searchAvailableHotels(String checkIn, String checkOut) {
        SQLiteDataProcessedProvider sqLiteDataProcessedProvider = new SQLiteDataProcessedProvider();
        List<Hotel> availableHotels = sqLiteDataProcessedProvider.search(checkIn, checkOut);
        return availableHotels.stream()
                .filter(h->h.averageRain() < 0.2)
                .toList();
    }

    public List<Hotel> searchRecommendedHotels(List<Hotel> hotels,  int preferredTemperature){
        return hotels.stream()
                .filter(h-> isHotelTemperatureAcceptable(h, preferredTemperature))
                .toList();
    }

    private static boolean isHotelTemperatureAcceptable(Hotel hotel, int preferredTemperature) {
        if (preferredTemperature > 20) {
            return hotel.averageTemperature() > 20;
        } else if (preferredTemperature < 10) {
            return hotel.averageTemperature() < 10;
        } else {
            return hotel.averageTemperature() >= 10 && hotel.averageTemperature() <= 20;
        }
    }

    public String[] allPossibleCheckIn() {
        LocalDate today = LocalDate.now();
        List<String> checkInDates = new ArrayList<>();
        if (ZonedDateTime.now(ZoneOffset.UTC).toLocalTime().isAfter(LocalTime.of(12, 0))) {
            for (int i = 1; i < 5; i++) {
                checkInDates.add(today.plusDays(i).format(dateFormatter));
            }
        } else {
            for (int i = 0; i < 5; i++) {
                checkInDates.add(today.plusDays(i).format(dateFormatter));
            }
        }
        return checkInDates.toArray(new String[0]);
    }

    public String[] allPossibleCheckOut() {
        LocalDate today = LocalDate.now();
        List<String> checkOutDates = new ArrayList<>();
        if (ZonedDateTime.now(ZoneOffset.UTC).toLocalTime().isAfter(LocalTime.of(12, 0))) {
            for (int i = 2; i < 5; i++) {
                checkOutDates.add(today.plusDays(i).format(dateFormatter));
            }
        } else {
            for (int i = 1; i < 5; i++) {
                checkOutDates.add(today.plusDays(i).format(dateFormatter));
            }
        }
        return checkOutDates.toArray(new String[0]);
    }
}
