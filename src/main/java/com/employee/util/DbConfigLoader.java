package com.employee.util;

import java.io.InputStream;
import java.util.Properties;

import com.employee.storage.StorageType;

public class DbConfigLoader {

    private static final Properties props = new Properties();

    private DbConfigLoader() {}

    public static void init(StorageType type) {

        String fileName = switch (type) {
            case POSTGRES -> "db-postgres.properties";
            case MYSQL -> "db-mysql.properties";
            case SUPABASE -> "db-supabase.properties";
            default -> throw new IllegalArgumentException("Not a DB storage");
        };

        try (InputStream is =
                 DbConfigLoader.class
                     .getClassLoader()
                     .getResourceAsStream(fileName)) {

            if (is == null) {
                throw new RuntimeException(fileName + " not found");
            }

            props.clear();
            props.load(is);
            Class.forName(props.getProperty("db.driver"));

        } catch (Exception exception) {
            throw new RuntimeException("DB config load failed", exception);
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
