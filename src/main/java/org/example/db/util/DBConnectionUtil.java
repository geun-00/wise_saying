package org.example.db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBConnectionUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/wisedb";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException(e);
        }
    }
}
