package org.ulpgc.dacd.control;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.ulpgc.dacd.model.Rate;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SQLiteEventStore implements EventStore {
    private static final String URL = "jdbc:sqlite:datamart.db";

    public void write(String event, String topicName) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node;
        try {
            node = objectMapper.readTree(event);
            if (topicName.equals("prediction.Weather")) {
                createWeatherTable();
                deleteOldWeatherData();
                String location = node.get("location").get("locationName").asText();
                String predictionTime = node.get("predictionTime").asText();
                if (entryWeatherExists(location, predictionTime)) {
                    updateWeatherData(location, predictionTime, node);
                } else {
                    insertWeatherData(node);
                }
            } else if (topicName.equals("data.Hotel")){
                createHotelTable();
                deleteOldHotelData();
                List<String> cheaperRate = getBestHotelWebRate(node);
                String hotelName = node.get("name").asText();
                String checkIn = node.get("checkIn").asText();
                String checkOut = node.get("checkOut").asText();
                if (entryHotelExists(hotelName, checkIn, checkOut)) {
                    updateHotelData(hotelName, checkIn, checkOut, cheaperRate);
                } else {
                    insertHotelData(cheaperRate, node);
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
                "predictionTime TEXT NOT NULL," +
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
                "checkIn TEXT NOT NULL," +
                "checkOut TEXT NOT NULL," +
                "webPage TEXT NOT NULL," +
                "rate TEXT NOT NULL)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(createHotelTable)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertWeatherData(JsonNode node) {
        String insertWeatherData = "INSERT INTO weather (location, predictionTime, temperature, rain) VALUES (?, ?, ?, ?)";
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

    private static void insertHotelData(List<String> cheaperHotel, JsonNode node) {
        String insertHotelData = "INSERT INTO hotels (location, hotelName, checkIn, checkOut, webPage, rate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertHotelData)) {
            String location = node.get("location").asText();
            String hotelName = node.get("name").asText();
            String checkIn = node.get("checkIn").asText();
            String checkOut = node.get("checkOut").asText();
            String webPage = cheaperHotel.get(0);
            double rate = Double.parseDouble(cheaperHotel.get(1));

            preparedStatement.setString(1, location);
            preparedStatement.setString(2, hotelName);
            preparedStatement.setString(3, checkIn);
            preparedStatement.setString(4, checkOut);
            preparedStatement.setString(5, webPage);
            preparedStatement.setDouble(6, rate);

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

    private boolean entryHotelExists(String hotelName, String checkIn, String checkOut) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM " + "hotels" + " WHERE hotelName = ? AND checkIn = ? AND checkOut = ?")) {

            preparedStatement.setString(1, hotelName);
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

    private void updateHotelData(String hotelName, String checkIn, String checkOut, List<String> cheaperHotel) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE hotels SET webPage = ?, rate = ? WHERE hotelName = ? AND checkIn = ? AND checkOut = ?")) {

            String webPage = cheaperHotel.get(0);
            double rate = Double.parseDouble(cheaperHotel.get(1));

            preparedStatement.setString(1, webPage);
            preparedStatement.setDouble(2, rate);
            preparedStatement.setString(3, hotelName);
            preparedStatement.setString(4, checkIn);
            preparedStatement.setString(5, checkOut);

            preparedStatement.executeUpdate();
        }
    }

    private void deleteOldWeatherData() {
        String yesterday = getYesterday("yyyy-MM-dd");
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM weather WHERE strftime('%Y-%m-%d', weather.predictionTime) <= ?")) {

            preparedStatement.setString(1, yesterday);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteOldHotelData() {
        String yesterday = getYesterday("yyyy-MM-dd");
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM hotels WHERE checkIn <= ?")) {

            preparedStatement.setString(1, yesterday);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getYesterday(String format) {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(yesterday);
    }

    private List<String> getBestHotelWebRate(JsonNode node) {
        Gson gson = new Gson();
        String json = node.get("rates").toString();
        if (json.equals("[]")) {
            return List.of("null", "0.0");
        }
        Rate[] rateList = gson.fromJson(json, Rate[].class);
        Rate cheaperRate = Arrays.stream(rateList)
                .min(Comparator.comparingDouble(Rate::getRate))
                .orElse(null);
        if (cheaperRate != null) {
            return Arrays.asList(cheaperRate.getName(), String.valueOf(cheaperRate.getRate()));
        } else {
            return Arrays.asList("null", "0.0");
        }

    }
}

