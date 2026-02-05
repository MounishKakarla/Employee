package com.employee.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class ConnectionFactory {

    private static HikariDataSource dataSource;

    private ConnectionFactory() {}

    public static void init() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(DbConfigLoader.getUrl());
        config.setUsername(DbConfigLoader.getUserName());
        config.setPassword(DbConfigLoader.getPassword());

        config.setMaximumPoolSize(10);        
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        config.setPoolName("EMS-HikariPool");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
