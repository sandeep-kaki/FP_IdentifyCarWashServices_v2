package com.indiamart.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader – Utility class (Singleton pattern).
 *
 * PURPOSE:
 *   Reads config.properties ONCE and serves values to all classes.
 *   Avoids repeated file I/O on every get() call.
 *
 * USAGE:
 *   ConfigReader.getInstance().get("city");
 *   ConfigReader.getInstance().get("browser");
 */
public class ConfigReader {

    // Single shared instance
    private static ConfigReader instance;

    // Java Properties object that holds all key=value pairs
    private final Properties properties;

    // Private constructor – called only once (first time getInstance() runs)
    private ConfigReader() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(
                "config.properties not found. Make sure it is in the project root folder.", e);
        }
    }

    /** Returns the single shared instance, creating it if needed. */
    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    /**
     * Returns the value for the given key.
     * Throws a clear error if the key does not exist – easier to debug than null.
     */
    public String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException(
                "Key not found in config.properties: [" + key + "]");
        }
        return value.trim();
    }
}
