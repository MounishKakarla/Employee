package com.employee.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    private static Connection connection;

    private ConnectionFactory() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        DbConfigLoader.getUrl(),
                        DbConfigLoader.getUserName(),
                        DbConfigLoader.getPassword()
                );
            }
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("DB connection failed", e);
        }
    }
}
