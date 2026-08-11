package com.ems.controller;

import com.ems.model.*;
import com.ems.model.exceptions.InvalidEmployeeDataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

// Controller managing employee operations (add, update, delete, search).
// Holds employees in memory and persists changes to JSON via FileHandler (composition).
public class EmployeeController {
    private static final Logger logger = LogManager.getLogger(EmployeeController.class);
    private static final String DATA_FILE = "employees.json";
    private List<Employee> employees;
    private Random random;
    private FileHandler fileHandler;

    public EmployeeController() {
        employees = new ArrayList<>();
        random = new Random();
        fileHandler = new FileHandler();
        loadEmployeesFromFile();
        logger.info("EmployeeController initialized with {} employees", employees.size());
    }

    // Generate unique 5-digit employee ID
    public int generateEmployeeId() {
        int newId;
        boolean idExists;

        do {
            newId = 10000 + random.nextInt(90000);
            idExists = false;

            // Check for duplicate ID
            for (Employee emp : employees) {
                if (emp.getId() == newId) {
                    idExists = true;
                    break;
                }
            }
        } while (idExists);

        logger.debug("Generated unique employee ID: {}", newId);
        return newId;
    }

    public void addEmployee(Employee employee) {
        try {
            // Prevent duplicate IDs
            for (Employee emp : employees) {
                if (emp.getId() == employee.getId()) {
                    throw new InvalidEmployeeDataException("Duplicate employee ID: " + employee.getId());
                }
            }

            employees.add(employee);
            saveEmployeesToFile();
            logger.info("Added employee: {} (ID: {})", employee.getName(), employee.getId());

        } catch (InvalidEmployeeDataException e) {
            logger.error("Error adding employee", e);
            throw new RuntimeException("Failed to add employee: " + e.getMessage(), e);
        }
    }

    public Employee getEmployee(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> sorted = new ArrayList<>(employees);
        sorted.sort(Comparator.comparingInt(Employee::getId));
        return sorted;
    }

    // Search by exact ID match (unique, returns 0 or 1 result)
    public List<Employee> searchById(int id) {
        List<Employee> results = new ArrayList<>();
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                results.add(emp);
                break; // ID is unique, stop searching
            }
        }
        logger.debug("ID search for {} returned {} results", id, results.size());
        return results;
    }

    // Search by name with partial, case-insensitive matching
    public List<Employee> searchByName(String name) {
        List<Employee> results = new ArrayList<>();
        String searchLower = name.toLowerCase();

        for (Employee emp : employees) {
            if (emp.getName().toLowerCase().contains(searchLower)) {
                results.add(emp);
            }
        }
        logger.debug("Name search for '{}' returned {} results", name, results.size());
        return results;
    }

    public void updateEmployee(Employee updatedEmployee) {
        try {
            boolean found = false;
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getId() == updatedEmployee.getId()) {
                    employees.set(i, updatedEmployee);
                    found = true;
                    saveEmployeesToFile();
                    logger.info("Updated employee: {} (ID: {})", updatedEmployee.getName(), updatedEmployee.getId());
                    break;
                }
            }

            if (!found) {
                throw new InvalidEmployeeDataException("Employee not found with ID: " + updatedEmployee.getId());
            }

        } catch (InvalidEmployeeDataException e) {
            logger.error("Error updating employee", e);
            throw new RuntimeException("Failed to update employee: " + e.getMessage(), e);
        }
    }

    public boolean deleteEmployee(int id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                employees.remove(i);
                saveEmployeesToFile();
                logger.info("Deleted employee with ID: {}", id);
                return true;
            }
        }
        logger.warn("Attempted to delete non-existent employee with ID: {}", id);
        return false;
    }

    public int getEmployeeCount() {
        return employees.size();
    }

    public double getTotalSalaryExpense() {
        double total = 0;
        for (Employee emp : employees) {
            total += emp.getSalary();
        }
        return total;
    }

    private void saveEmployeesToFile() {
        try {
            fileHandler.saveEmployees(employees, DATA_FILE);
        } catch (IOException e) {
            logger.error("Error saving employees to file", e);
            throw new RuntimeException("Failed to save employees to file: " + e.getMessage(), e);
        }
    }

    private void loadEmployeesFromFile() {
        try {
            List<Employee> loadedEmployees = fileHandler.loadEmployees(DATA_FILE);
            employees.clear();
            employees.addAll(loadedEmployees);
            logger.info("Loaded {} employees from file", employees.size());

        } catch (Exception e) {
            logger.warn("Could not load employees from file, starting fresh: {}", e.getMessage());
            employees.clear();
        }
    }

    public void reloadFromFile() {
        loadEmployeesFromFile();
    }
}