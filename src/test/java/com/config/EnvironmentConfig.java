package com.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentConfig {

    private static final String default_env = "uat";
    private static Properties properties;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        String env = System.getProperty("env", default_env).toLowerCase();
        String configFile = String.format("config/%s.properties", env);
        
        properties = new Properties();
        try (InputStream input = EnvironmentConfig.class.getClassLoader()
                .getResourceAsStream(configFile)) {
            if (input == null) {
                throw new RuntimeException("Unable to find config file: " + configFile);
            }
            properties.load(input);
            System.out.println("Loaded configuration for environment: " + env.toUpperCase());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load environment configuration", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.pageload", "90"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("timeout.implicit", "10"));
    }
    
    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("browser.headless", "false"));
    }
    
    public static String getBrowserType() {
        return properties.getProperty("browser.type", "chrome");
    }
    
    public static boolean takeScreenshotOnFailure() {
        return Boolean.parseBoolean(properties.getProperty("screenshot.onfailure", "true"));
    }
    
    public static String getScreenshotPath() {
        return properties.getProperty("screenshot.path", "resources/screenshots");
    }
    
    public static String getEnvironment() {
        return System.getProperty("env", default_env).toUpperCase();
    }

}
