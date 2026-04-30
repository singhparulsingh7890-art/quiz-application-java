package com.miniproject.quizapp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("application.properties not found in resources.");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    public static String getUrl() {
        return PROPERTIES.getProperty("db.url");
    }

    public static String getUsername() {
        return PROPERTIES.getProperty("db.username");
    }

    public static String getPassword() {
        return PROPERTIES.getProperty("db.password");
    }
}
