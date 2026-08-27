package com.ems.controller;

import com.ems.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

// Controller handling login authentication.
// Checks default credentials first, then credentials saved in config/users.json.
public class AuthController {
    private static final Logger logger = LogManager.getLogger(AuthController.class);
    private static final String USERS_FILE = "users.json";
    private static final String CONFIG_DIR = "config";

    public boolean authenticate(String username, String password) {
        logger.info("Login attempt for user: {}", username);
        username = username.trim();
        password = password.trim();

        CredentialsManager credentials = CredentialsManager.getInstance();

        // Check default credentials
        if (credentials.getUsername().equals(username) && credentials.getPassword().equals(password)) {
            logger.info("Login successful with default credentials");
            saveUserToFile(new User(username, password));
            return true;
        }

        // Check saved credentials
        try {
            User savedUser = loadUserFromFile();
            if (savedUser != null && savedUser.validate(username, password)) {
                logger.info("Login successful with saved credentials");
                return true;
            }
        } catch (Exception e) {
            logger.warn("Could not load saved credentials: {}", e.getMessage());
        }

        logger.warn("Login failed for user: {}", username);
        return false;
    }

    private User loadUserFromFile() throws IOException {
        File configDir = new File(CONFIG_DIR);
        File file = new File(configDir, USERS_FILE);

        if (file.exists() && file.isFile()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(file, User.class);
        }

        return null;
    }

    private void saveUserToFile(User user) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File configDir = new File(CONFIG_DIR);

            if (!configDir.exists()) {
                configDir.mkdirs();
            }

            File file = new File(configDir, USERS_FILE);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, user);
            logger.info("User credentials saved to: {}", file.getAbsolutePath());

        } catch (IOException e) {
            logger.error("Error saving user credentials", e);
        }
    }
}