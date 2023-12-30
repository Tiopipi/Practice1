package org.ulpgc.dacd.control;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;

public class SQLiteEventStore implements EventStore {
    private static final String URL = "jdbc:sqlite:database.db";

    public void write(String event, String topicName) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node;
        try {
            node = objectMapper.readTree(event);
            if (topicName.equals("prediction.Weather")) {
                createWeatherTable();
                String location = node.get("location").get("locationName").asText();
                String predictionTime = node.get("predictionTime").asText();
                if (entryWeatherExists(location, predictionTime)){
                    updateWeatherData(location, predictionTime, node);
                } else {
                    insertWeatherData(node);
                }
            } else {
                createHotelTable();
                String location = node.get("location").asText();
                String checkIn = node.get("checkIn").asText();
                String checkOut = node.get("checkOut").asText();
                if (entryHotelExists(location, checkIn, checkOut)){
                    updateHotelData(location, checkIn, checkOut, node);
                } else {
                    insertHotelData(node);
                }
            }
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private static void createWeatherTable() {
        String createWeatherTable = "CREATE TABLE IF NOT EXISTS weather (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "location TEXT NOT NULL," +
                "prediction_time TEXT NOT NULL," +
                "temperature DOUBLE NOT NULL," +
                "rain DOUBLE NOT NULL)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(createWeatherTable)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createHotelTable() {
        String createHotelTable = "CREATE TABLE IF NOT EXISTS hotels (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "location TEXT NOT NULL," +
                "hotelName TEXT NOT NULL," +
                "check_in_date TEXT NOT NULL," +
                "check_out_date TEXT NOT NULL," +
                "web_page TEXT NOT NULL," +
                "price TEXT NOT NULL)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(createHotelTable)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertWeatherData(JsonNode node) {
        String insertWeatherData = "INSERT INTO weather (location, date, temperature, humidity) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertWeatherData)) {
            String location = node.get("location").get("locationName").asText();
            String predictionTime = node.get("predictionTime").asText();
            double temperature = node.get("temp").asDouble();
            double rain = node.get("rain").asDouble();

            preparedStatement.setString(1, location);
            preparedStatement.setString(2, predictionTime);
            preparedStatement.setDouble(3, temperature);
            preparedStatement.setDouble(4, rain);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertHotelData(JsonNode node) {
        String insertHotelData = "INSERT INTO hotels (location, check_in_date, check_out_date, prices) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertHotelData)) {
            String location = node.get("location").asText();
            String hotelName = node.get("name").asText();
            String checkIn = node.get("checkIn").asText();
            String checkOut = node.get("checkOut").asText();
            String webPage = node.get("rates").get("name").asText();
            double rate = node.get("rates").get("rate").asDouble();

            preparedStatement.setString(1, location);
            preparedStatement.setString(2, hotelName);
            preparedStatement.setString(3, checkIn);
            preparedStatement.setString(4, checkOut);
            preparedStatement.setString(5, webPage);
            preparedStatement.setDouble(5, rate);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean entryWeatherExists(String location, String predictionTime) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(*) FROM " + "weather" + " WHERE location = ? AND predictionTime = ?")) {

            preparedStatement.setString(1, location);
            preparedStatement.setString(2, predictionTime);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }

    private boolean entryHotelExists(String location, String checkIn, String checkOut) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(*) FROM " + "hotels" + " WHERE location = ? AND checkIn = ? AND checkOut = ?")) {

            preparedStatement.setString(1, location);
            preparedStatement.setString(2, checkIn);
            preparedStatement.setString(3, checkOut);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }

    private void updateWeatherData(String location, String predictionTime, JsonNode node) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE weather SET temperature = ?, rain = ? WHERE location = ? AND predictionTime = ?")) {

            double temperature = node.get("temp").asDouble();
            double rain = node.get("rain").asDouble();

            preparedStatement.setDouble(1, temperature);
            preparedStatement.setDouble(2, rain);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, predictionTime);

            preparedStatement.executeUpdate();
        }
    }

    private void updateHotelData(String location, String checkIn, String checkOut, JsonNode node) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE hotels SET webPage = ?, rate = ? WHERE location = ? AND checkIn = ? AND checkOut = ?")) {

            String webPage = node.get("rates").get("name").asText();
            double rate = node.get("rates").get("rate").asDouble();

            preparedStatement.setString(1, webPage);
            preparedStatement.setDouble(2, rate);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, checkIn);
            preparedStatement.setString(5, checkOut);

            preparedStatement.executeUpdate();
        }
    }

}
