package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SqliteWeatherStore implements WeatherStore {
    public boolean save(Statement statement, Weather weather) throws SQLException {
        String tableName = weather.getLocation().getIsland();
        String insertQuery = "INSERT INTO " + tableName +
                " (date, rainProbability, wind, temp, humidity, cloud) VALUES ('" +
                weather.getTs().toString() + "', " +
                weather.getRain() + ", " +
                weather.getWind() + ", " +
                weather.getTemp() + ", " +
                weather.getHumidity() + ", " +
                weather.getCloud() + ")";
        return statement.executeUpdate(insertQuery) > 0;
    }
    public void createTable(Statement statement, Location location) throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS " + location.getIsland() + " (" +
                "date TEXT PRIMARY KEY,\n" +
                "rainProbability REAL DEFAULT 0,\n" +
                "wind REAL DEFAULT 0, \n"+
                "temp REAL DEFAULT 0, \n"+
                "humidity INT DEFAULT 0, \n"+
                "cloud INT DEFAULT 0"+
                ");");

    }
    public boolean update(Statement statement, Weather weather) throws SQLException {
        String tableName = weather.getLocation().getIsland();
        String updateQuery = "UPDATE " + tableName + " SET " +
                "rainProbability = " + weather.getRain() + ", " +
                "wind = " + weather.getWind() + ", " +
                "temp = " + weather.getTemp() + ", " +
                "humidity = " + weather.getHumidity() + ", " +
                "cloud = " + weather.getCloud() +
                " WHERE date = '" + weather.getTs().toString() + "'"; // Asegúrate de que weather.getTs() devuelva una cadena adecuada
        return statement.executeUpdate(updateQuery) > 0;
    }
    public void dropTable(Statement statement) throws SQLException {
        statement.execute("DROP TABLE IF EXISTS GranCanaria;\n");
    }

}
