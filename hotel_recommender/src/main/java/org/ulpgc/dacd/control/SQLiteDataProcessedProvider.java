package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.Prediction;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteDataProcessedProvider implements DataProcessedProvider {

    private static final String URL = "jdbc:sqlite:datamart.db";
    private static final SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public List<Hotel> search(String checkIn, String checkOut) {
        return selectHotelsAndPredictionForDate(checkIn, checkOut);
    }

    public List<Hotel> selectHotelsAndPredictionForDate(String checkIn, String checkOut) {
        String query = "SELECT hotels.*, weather.* FROM hotels " +
                "JOIN weather ON hotels.location = weather.location " +
                "WHERE strftime('%Y-%m-%d', weather.predictionTime) BETWEEN ? AND ? " +
                "AND hotels.rate != 0.0 AND hotels.checkIn = ? AND hotels.checkOut = ?";

        return executeQuery(query, checkIn, checkOut);
    }

    private List<Hotel> executeQuery(String query, String checkIn, String checkOut) {
        List<Hotel> availableHotels = new ArrayList<>();

        try (Connection connection = createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setQueryParameters(preparedStatement, checkIn, checkOut);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Map<String, Hotel> hotelsMap = processResultSet(resultSet, checkIn, checkOut);
                availableHotels.addAll(hotelsMap.values());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return availableHotels;
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private void setQueryParameters(PreparedStatement preparedStatement, String checkIn, String checkOut) throws SQLException {
        preparedStatement.setString(1, checkIn);
        preparedStatement.setString(2, checkOut);
        preparedStatement.setString(3, checkIn);
        preparedStatement.setString(4, checkOut);
    }

    private Map<String, Hotel> processResultSet(ResultSet resultSet, String checkIn, String checkOut) throws SQLException {
        Map<String, Hotel> hotelsMap = new HashMap<>();

        while (resultSet.next()) {
            String hotelLocation = resultSet.getString("location");
            Hotel hotel = hotelsMap.get(hotelLocation);

            if (hotel == null) {
                hotel = createHotel(resultSet, hotelLocation, checkIn, checkOut);
                hotelsMap.put(hotelLocation, hotel);
            }

            Prediction prediction = createPrediction(resultSet);

            if (isDateInRange(prediction.getPredictionTime(), checkIn, checkOut)) {
                hotel.getPredictionList().add(prediction);
            }
        }

        return hotelsMap;
    }

    private Hotel createHotel(ResultSet resultSet, String hotelLocation, String checkIn, String checkOut) throws SQLException {
        return new Hotel(
                resultSet.getString("hotelName"),
                hotelLocation,
                checkIn,
                checkOut,
                resultSet.getString("webPage"),
                resultSet.getDouble("rate"),
                getPricePerNight(checkIn, checkOut, resultSet.getDouble("rate"))
        );
    }

    private Prediction createPrediction(ResultSet resultSet) throws SQLException {
        return new Prediction(
                convertToNewFormat(resultSet.getString("predictionTime")),
                resultSet.getDouble("rain"),
                resultSet.getDouble("temperature")
        );
    }

    private double getPricePerNight(String checkIn, String checkOut, Double rate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(checkIn, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOut, formatter);
        int nights = Math.abs(checkInDate.until(checkOutDate).getDays());
        return nights * rate;
    }

    private boolean isDateInRange(String date, String checkIn, String checkOut) {
        return date.compareTo(checkIn) >= 0 && date.compareTo(checkOut) <= 0;
    }

    private String convertToNewFormat(String originalDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").format(sourceDateFormat.parse(originalDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
