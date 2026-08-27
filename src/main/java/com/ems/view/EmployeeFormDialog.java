package com.ems.view;

import com.ems.controller.EmployeeController;
import com.ems.model.*;
import com.ems.model.exceptions.InvalidEmployeeDataException;
import com.ems.view.components.ModernButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

// Dialog for adding or editing an employee.
// Polymorphism: the Employee reference is assigned a concrete subtype
// (FullTimeEmployee, PartTimeEmployee, or Intern) based on the selected type.
public class EmployeeFormDialog extends JDialog {
    private static final Logger logger = LogManager.getLogger(EmployeeFormDialog.class);
    private boolean success = false;
    private EmployeeController employeeController;

    private JTextField idField, nameField, ageField, addressField, salaryField;
    private JTextField departmentField, emailField;
    private JComboBox<String> typeComboBox;

    private Employee existingEmployee;

    public EmployeeFormDialog(Frame parent, String title, Employee existingEmployee) {
        super(parent, title, true);
        this.existingEmployee = existingEmployee;
        this.employeeController = new EmployeeController();
        initializeUI();

        if (existingEmployee != null) {
            populateFields();
        } else {
            idField.setText(String.valueOf(employeeController.generateEmployeeId()));
            idField.setEditable(false);
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(600, 500);
        setLocationRelativeTo(getOwner());

        // Main panel with scroll pane for responsiveness
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Title
        JLabel titleLabel = new JLabel(existingEmployee == null ? "Add New Employee" : "Edit Employee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(titleLabel, gbc);
        gbc.insets = new Insets(8, 8, 8, 8);
        row++;

        // Employee Type
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Employee Type*:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 0.7;
        String[] types = {"Full-Time", "Part-Time", "Intern"};
        typeComboBox = new JComboBox<>(types);
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        typeComboBox.setPreferredSize(new Dimension(350, 28));
        mainPanel.add(typeComboBox, gbc);
        gbc.weightx = 0;
        row++;

        // ID field (read-only)
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Employee ID:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 0.7;
        idField = createTextField();
        idField.setEditable(false);
        idField.setBackground(new Color(245, 245, 245));
        mainPanel.add(idField, gbc);
        gbc.weightx = 0;
        row++;

        // Name field
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Full Name*:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 0.7;
        nameField = createTextField();
        mainPanel.add(nameField, gbc);
        gbc.weightx = 0;
        row++;

        // Age field
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Age*:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 0.7;
        ageField = createTextField();
        mainPanel.add(ageField, gbc);
        gbc.weightx = 0;
        row++;

        // Email field
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Email*:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 0.7;
        emailField = createTextField();
        mainPanel.add(emailField, gbc);
        gbc.weightx = 0;
        row++;

        // Address field
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Address*:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 0.7;
        addressField = createTextField();
        mainPanel.add(addressField, gbc);
        gbc.weightx = 0;
        row++;

        // Department field
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Department*:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 0.7;
        departmentField = createTextField();
        mainPanel.add(departmentField, gbc);
        gbc.weightx = 0;
        row++;

        // Salary field
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Salary*:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 0.7;
        salaryField = createTextField();
        mainPanel.add(salaryField, gbc);
        gbc.weightx = 0;
        row++;

        // Required fields note
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        noteLabel.setForeground(Color.RED);
        mainPanel.add(noteLabel, gbc);
        gbc.insets = new Insets(8, 8, 8, 8);

        // Add main panel to scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        ModernButton saveButton = new ModernButton("Save Employee");
        saveButton.setPreferredSize(new Dimension(140, 35));
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.addActionListener(e -> saveEmployee());

        ModernButton cancelButton = new ModernButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.setBackgroundColor(new Color(231, 76, 60));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setMinimumSize(new Dimension(550, 450));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(350, 28));
        return field;
    }

    private void populateFields() {
        idField.setText(String.valueOf(existingEmployee.getId()));
        idField.setEditable(false);
        idField.setBackground(new Color(245, 245, 245));
        nameField.setText(existingEmployee.getName());
        ageField.setText(String.valueOf(existingEmployee.getAge()));
        addressField.setText(existingEmployee.getAddress());
        emailField.setText(existingEmployee.getEmail());
        departmentField.setText(existingEmployee.getDepartment());
        salaryField.setText(String.valueOf(existingEmployee.getSalary()));

        if (existingEmployee instanceof FullTimeEmployee) {
            typeComboBox.setSelectedItem("Full-Time");
        } else if (existingEmployee instanceof PartTimeEmployee) {
            typeComboBox.setSelectedItem("Part-Time");
        } else if (existingEmployee instanceof Intern) {
            typeComboBox.setSelectedItem("Intern");
        }
    }

    private void saveEmployee() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String address = addressField.getText().trim();
            String email = emailField.getText().trim();
            String department = departmentField.getText().trim();
            String salaryText = salaryField.getText().trim();

            // Validation
            if (name.isEmpty() || address.isEmpty() || email.isEmpty() || department.isEmpty() ||
                    ageText.isEmpty() || salaryText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all required fields (*)",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageText);
            double salary = Double.parseDouble(salaryText);

            if (age < 18 || age > 70) {
                JOptionPane.showMessageDialog(this,
                        "Age must be between 18 and 70",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (salary <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Salary must be greater than 0",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Employee employee;
            String selectedType = (String) typeComboBox.getSelectedItem();

            if ("Full-Time".equals(selectedType)) {
                employee = new FullTimeEmployee(id, name, age, address, salary, department, email);
            } else if ("Part-Time".equals(selectedType)) {
                employee = new PartTimeEmployee(id, name, age, address, salary, department, email);
            } else {
                employee = new Intern(id, name, age, address, salary, department, email);
            }

            if (existingEmployee == null) {
                employeeController.addEmployee(employee);
            } else {
                employeeController.updateEmployee(employee);
            }

            success = true;
            JOptionPane.showMessageDialog(this,
                    "Employee saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for age and salary",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.error("Error saving employee", ex);
            JOptionPane.showMessageDialog(this,
                    "Unexpected error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}