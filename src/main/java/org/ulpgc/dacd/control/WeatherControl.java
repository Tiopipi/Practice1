package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherControl {
    //@TODO Schedule
    public void control(List<Location> locations, String apikey) {
        SqliteWeatherStore sqliteWeatherStore = new SqliteWeatherStore();
        OpenWeatherMapSupplier openWeatherMapSupplier = new OpenWeatherMapSupplier();
        String dbPath = "weather.db";
        System.out.println("Actualizado");
        List<Weather> weathers = new ArrayList<>();
        for (Location island : locations) {
            weathers.add(openWeatherMapSupplier.getWeather(island, setInstant(2023, 11, 9), apikey));
            weathers.add(openWeatherMapSupplier.getWeather(island, setInstant(2023, 11, 10), apikey));
            weathers.add(openWeatherMapSupplier.getWeather(island, setInstant(2023, 11, 11), apikey));
            weathers.add(openWeatherMapSupplier.getWeather(island, setInstant(2023, 11, 12), apikey));
            weathers.add(openWeatherMapSupplier.getWeather(island, setInstant(2023, 11, 13), apikey));
        }
        try (Connection connection = connect(dbPath)) {
            Statement statement = connection.createStatement();
            for (Location island : locations) {
                sqliteWeatherStore.createTable(statement, island);
            }
            for (Weather weatherIsland : weathers) {
                if (sqliteWeatherStore.select(statement, weatherIsland).next()) {
                    if (sqliteWeatherStore.select(statement, weatherIsland).getString("date").equals(weatherIsland.getTs().toString())) {
                        sqliteWeatherStore.update(statement, weatherIsland);
                    } else sqliteWeatherStore.save(statement, weatherIsland);
                } //else sqliteWeatherStore.save(statement, weatherIsland);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public static Instant setInstant(int year, int month, int day) {
        LocalTime hour = LocalTime.of(12, 0);
        ZoneId timeZone = ZoneId.of("Atlantic/Canary");
        return hour.atDate(java.time.LocalDate.of(year, month, day)).atZone(timeZone).toInstant();
    }
}
