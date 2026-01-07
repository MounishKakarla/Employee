package com.employee.util;

import java.io.InputStream;
import java.util.Properties;

import com.employee.storage.StorageType;

public class DbConfigLoader {

    private static final Properties props = new Properties();
    private static boolean initialized = false;

    private DbConfigLoader() {}

   
    public static void init(StorageType type) {
        if (initialized) return;

        try {
            String fileName;

            if (type == StorageType.POSTGRES) {
                fileName = "db-postgres.properties";
            } else if (type == StorageType.MYSQL) {
                fileName = "db-mysql.properties";
            } else {
                throw new RuntimeException("Invalid DB type");
            }

            InputStream is = DbConfigLoader.class
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            if (is == null) {
                throw new RuntimeException(fileName + " not found");
            }

            props.load(is);
            Class.forName(props.getProperty("db.driver"));

            initialized = true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB config", e);
        }
    }

    public static String getUrl() {
        return props.getProperty("db.url");
    }

    public static String getUserName() {
        return props.getProperty("db.username");
    }

    public static String getPassword() {
        return props.getProperty("db.password");
    }
}
