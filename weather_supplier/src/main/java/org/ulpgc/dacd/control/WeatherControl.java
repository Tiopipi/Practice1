package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WeatherControl {
    public void execute(List<Location> locations, String apikey) {
        SqliteWeatherStore sqliteWeatherStore = new SqliteWeatherStore();
        OpenWeatherMapSupplier openWeatherMapSupplier = new OpenWeatherMapSupplier();
        String dbPath = "weather.db";
        List<Weather> allWeathers = fetchAllWeatherData(locations, apikey, openWeatherMapSupplier);
        try (Connection connection = connect(dbPath)) {
            Statement statement = connection.createStatement();
            createTablesForLocations(locations, sqliteWeatherStore, statement);
            updateWeatherData(sqliteWeatherStore, statement, allWeathers);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Weather> fetchAllWeatherData(List<Location> locations, String apikey, OpenWeatherMapSupplier supplier) {
        List<Weather> allWeathers = new ArrayList<>();
        for (Location island : locations) {
            List<Weather> weathers = supplier.getWeather(island, createInstantList(), apikey);
            allWeathers.addAll(weathers);
        }
        return allWeathers;
    }

    private void createTablesForLocations(List<Location> locations, SqliteWeatherStore store, Statement statement) throws SQLException {
        for (Location island : locations) {
            store.createTable(statement, island);
        }
    }

    private void updateWeatherData(SqliteWeatherStore store, Statement statement, List<Weather> allWeathers) throws SQLException {
        for (Weather weatherIsland : allWeathers) {
            ResultSet resultSet = store.select(statement, weatherIsland);
            if (resultSet.next()) {
                if (resultSet.getString("date").equals(weatherIsland.getTs().toString())) {
                    store.update(statement, weatherIsland);
                } else {
                    store.save(statement, weatherIsland);
                }
            } else {
                store.save(statement, weatherIsland);
            }
        }
    }

    public static Connection connect(String dbPath) {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" + dbPath;
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static List<Instant> createInstantList() {
        List<Instant> dateList = new ArrayList<>();
        Instant now = Instant.now();
        for (int i = 0; i <= 4; i++) {
            Instant date = LocalDateTime.ofInstant(now, ZoneOffset.UTC)
                    .plusDays(i)
                    .toLocalDate()
                    .atTime(LocalTime.of(12, 0, 0))
                    .toInstant(ZoneOffset.UTC)
                    .truncatedTo(ChronoUnit.SECONDS);
            dateList.add(date);
        }
        return dateList;
    }
}
