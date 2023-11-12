package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface WeatherStore {
     boolean save(java.sql.Statement statement, Weather weather) throws SQLException;
     void createTable(Statement statement, Location location) throws SQLException;
     boolean update(Statement statement, Weather weather) throws SQLException;
     ResultSet select(Statement statement, Weather weather) throws  SQLException;
}
