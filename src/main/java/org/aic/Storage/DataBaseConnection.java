package org.aic.Storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/bd_aic_2026";
    private static final String USER = "root";
    private static final String PASSWORD = "tema_bro_8474";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Підключення успішне!");
        }
        return connection;
    }
}
