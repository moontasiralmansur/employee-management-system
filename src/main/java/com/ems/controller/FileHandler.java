package com.ems.controller;

import com.ems.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

// Handles JSON persistence of employee data using Jackson.
// Registered subtypes allow Jackson to restore the correct employee subtype on load.
public class FileHandler {
    private static final Logger logger = LogManager.getLogger(FileHandler.class);
    private ObjectMapper objectMapper;
    private static final String DATA_DIR = "data";

    public FileHandler() {
        objectMapper = new ObjectMapper();

        // Register subtypes for JSON serialization
        objectMapper.registerSubtypes(
                FullTimeEmployee.class,
                PartTimeEmployee.class,
                Intern.class
        );
    }

    public void saveEmployees(List<Employee> employees, String filename) throws IOException {
        // Ensure data directory exists
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File file = new File(dataDir, filename);

        try (FileWriter writer = new FileWriter(file)) {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employees);
            writer.write(json);
            logger.info("Saved {} employees to JSON", employees.size());
        }
    }

    public List<Employee> loadEmployees(String filename) throws IOException {
        File dataDir = new File(DATA_DIR);
        File file = new File(dataDir, filename);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        if (file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            // Read the entire file
            BufferedReader br = new BufferedReader(reader);
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.append(line);
            }

            // Parse JSON
            List<Employee> employees = objectMapper.readValue(
                    fileContent.toString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Employee.class)
            );

            logger.info("Successfully loaded {} employees from JSON", employees.size());
            return employees;

        } catch (Exception e) {
            logger.error("Error loading JSON: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}