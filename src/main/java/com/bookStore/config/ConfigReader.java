package com.bookStore.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigReader {

    private static final Properties properties = new Properties();

   
    public static final String BASE_URI_KEY = "base.uri";
    public static final String CONTENT_TYPE_KEY = "content.type";

    static {
        try {
            String configPath = System.getProperty("user.dir") + File.separator + "src" +
                                File.separator + "test" + File.separator + "resources" +
                                File.separator + "config.properties";
            FileInputStream input = new FileInputStream(configPath);
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties file from path.", e);
        }
    }

   
     
    public static String getProperty(String key) {
        String envOverride = System.getenv(key.toUpperCase().replace('.', '_')); 
        String value = envOverride != null ? envOverride : properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Missing required configuration: " + key);
        }
        return value.trim();
    }

    public static String getBaseUri() {
        return getProperty(BASE_URI_KEY);
    }

    public static String getContentType() {
        return getProperty(CONTENT_TYPE_KEY);
    }
}
