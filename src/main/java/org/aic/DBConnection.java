package org.aic;

import java.sql.*;

public class DBConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/zlagoda";
    private static final String USER = "root";
    private static final String PASSWORD = "123456Ollie";

    private static Connection connection = null;

    // Цей метод повертає підключення
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Підключення успішне!");
        }
        return connection;
    }
}