package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WeatherControl {
    public void control(List<Location> locations, String apikey) {
        SqliteWeatherStore sqliteWeatherStore = new SqliteWeatherStore();
        OpenWeatherMapSupplier openWeatherMapSupplier = new OpenWeatherMapSupplier();
        String dbPath = "weather.db";
        List<Weather> allWeathers = new ArrayList<>();
        for (Location island : locations) {
            List<Weather> weathers = openWeatherMapSupplier.getWeather(island, setInstantList(), apikey);
            allWeathers.addAll(weathers);
            weathers.clear();
        }
        try (Connection connection = connect(dbPath)) {
            Statement statement = connection.createStatement();
            for (Location island : locations) {
                sqliteWeatherStore.createTable(statement, island);
            }
            for (Weather weatherIsland : allWeathers) {
                if (sqliteWeatherStore.select(statement, weatherIsland).next()) {
                    if (sqliteWeatherStore.select(statement, weatherIsland).getString("date").equals(weatherIsland.getTs().toString())) {
                        sqliteWeatherStore.update(statement, weatherIsland);
                    } else sqliteWeatherStore.save(statement, weatherIsland);
                } else sqliteWeatherStore.save(statement, weatherIsland);
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

    public static List<Instant> setInstantList() {
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
