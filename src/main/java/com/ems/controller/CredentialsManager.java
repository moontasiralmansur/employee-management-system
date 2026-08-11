package com.ems.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

// Singleton pattern: provides a single shared credentials manager instance.
// Loads login credentials from application.properties, falling back to defaults.
public class CredentialsManager {
    private static final Logger logger = LogManager.getLogger(CredentialsManager.class);
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin1234";
    private static CredentialsManager instance;

    private String username;
    private String password;

    private CredentialsManager() {
        loadCredentials();
    }

    public static synchronized CredentialsManager getInstance() {
        if (instance == null) {
            instance = new CredentialsManager();
        }
        return instance;
    }

    private void loadCredentials() {
        // Try to load from properties file
        try {
            Properties props = new Properties();
            File propFile = new File("application.properties");

            if (propFile.exists()) {
                try (FileInputStream input = new FileInputStream(propFile)) {
                    props.load(input);
                    username = props.getProperty("default.username", DEFAULT_USERNAME);
                    password = props.getProperty("default.password", DEFAULT_PASSWORD);
                    logger.info("Loaded credentials from application.properties");
                    return;
                }
            }
        } catch (Exception e) {
            logger.warn("Could not load credentials from properties file: {}", e.getMessage());
        }

        // Use defaults
        username = DEFAULT_USERNAME;
        password = DEFAULT_PASSWORD;
        logger.info("Using default credentials");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}