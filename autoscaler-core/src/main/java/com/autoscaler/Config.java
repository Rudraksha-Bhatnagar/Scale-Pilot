package com.autoscaler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    private static Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            LOGGER.severe("Error loading configuration: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key, "");
    }

    public static double getDouble(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid number format for key: " + key);
            return defaultValue;
        }
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid number format for key: " + key);
            return defaultValue;
        }
    }
}
