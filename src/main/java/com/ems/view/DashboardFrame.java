package com.ems.view;

import com.ems.controller.EmployeeController;
import com.ems.model.Employee;
import com.ems.view.components.ModernButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

// Main dashboard window with the employee table and action buttons.
// Routes add/edit/delete/search operations through the EmployeeController.
public class DashboardFrame extends JFrame {
    private static final Logger logger = LogManager.getLogger(DashboardFrame.class);
    private EmployeeController employeeController;
    private EmployeeTablePanel tablePanel;
    private JLabel statusLabel;

    public DashboardFrame() {
        employeeController = new EmployeeController();
        initializeUI();
        loadEmployees();
    }

    private void initializeUI() {
        setTitle("Employee Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);

        ModernButton logoutButton = new ModernButton("Logout");
        logoutButton.setBackgroundColor(new Color(231, 76, 60));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        logoutPanel.add(logoutButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        // Left Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        controlPanel.setBackground(new Color(240, 240, 240));

        Dimension buttonSize = new Dimension(180, 40);

        ModernButton addButton = new ModernButton("Add Employee");
        addButton.setPreferredSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        addButton.addActionListener(e -> showAddEmployeeDialog());

        ModernButton searchButton = new ModernButton("Search Employee");
        searchButton.setPreferredSize(buttonSize);
        searchButton.setMaximumSize(buttonSize);
        searchButton.setBackgroundColor(new Color(155, 89, 182));
        searchButton.addActionListener(e -> showSearchDialog());

        ModernButton editButton = new ModernButton("Edit Employee");
        editButton.setPreferredSize(buttonSize);
        editButton.setMaximumSize(buttonSize);
        editButton.addActionListener(e -> showEditEmployeeDialog());

        ModernButton deleteButton = new ModernButton("Delete Employee");
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        deleteButton.setBackgroundColor(new Color(231, 76, 60));
        deleteButton.addActionListener(e -> deleteEmployee());

        ModernButton refreshButton = new ModernButton("Refresh");
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.setMaximumSize(buttonSize);
        refreshButton.addActionListener(e -> {
            employeeController.reloadFromFile();
            loadEmployees();
        });

        ModernButton aboutButton = new ModernButton("About EMS");
        aboutButton.setPreferredSize(buttonSize);
        aboutButton.setMaximumSize(buttonSize);
        aboutButton.setBackgroundColor(new Color(52, 152, 219));
        aboutButton.addActionListener(e -> showAboutDialog());

        // Add buttons to control panel
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(addButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(searchButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(editButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(deleteButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(refreshButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(aboutButton);
        controlPanel.add(Box.createVerticalGlue());

        // Table Panel
        tablePanel = new EmployeeTablePanel();

        // Status Panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.setBackground(Color.LIGHT_GRAY);

        statusLabel = new JLabel("Total Employees: " + employeeController.getEmployeeCount());
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        // Add panels
        add(headerPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.WEST);
        add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        logger.info("Dashboard initialized");
    }

    private void loadEmployees() {
        try {
            java.util.List<Employee> employees = employeeController.getAllEmployees();
            tablePanel.updateTable(employees);
            updateStatusLabel();
            logger.info("Displayed {} employees in table", employees.size());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading employees: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatusLabel() {
        if (statusLabel != null) {
            statusLabel.setText("Total Employees: " + employeeController.getEmployeeCount());
        }
    }

    private void showAddEmployeeDialog() {
        EmployeeFormDialog dialog = new EmployeeFormDialog(this, "Add New Employee", null);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            employeeController.reloadFromFile();
            SwingUtilities.invokeLater(this::loadEmployees);
        }
    }

    private void showSearchDialog() {
        SearchFrame searchFrame = new SearchFrame(this);

        // Add window listener to detect when search dialog closes
        searchFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Refresh dashboard when search dialog is closed
                employeeController.reloadFromFile();
                loadEmployees();
            }
        });

        searchFrame.setVisible(true);
    }

    private void showEditEmployeeDialog() {
        int selectedId = tablePanel.getSelectedEmployeeId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Employee employee = employeeController.getEmployee(selectedId);
            if (employee != null) {
                EmployeeFormDialog dialog = new EmployeeFormDialog(this, "Edit Employee", employee);
                dialog.setVisible(true);
                if (dialog.isSuccess()) {
                    employeeController.reloadFromFile();
                    loadEmployees();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Employee not found",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            logger.error("Error editing employee", ex);
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        int selectedId = tablePanel.getSelectedEmployeeId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete employee ID: " + selectedId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (employeeController.deleteEmployee(selectedId)) {
                    JOptionPane.showMessageDialog(this,
                            "Employee deleted successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadEmployees();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Employee not found",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                logger.error("Error deleting employee", ex);
                JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAboutDialog() {
        String aboutMessage = """
            <html>
            <div style='text-align:center;'>
            <h3 style='margin-top:5px; margin-bottom:10px; font-size:12px;'>Employee Management System</h3>
            <p style='font-size:10px; line-height:1.4;'>
            <b>Developed by Moontasir Al Mansur at ULAB</b>
            </p>
            </div>
            </html>""";

        JOptionPane.showMessageDialog(this,
                aboutMessage,
                "About EMS",
                JOptionPane.INFORMATION_MESSAGE);
    }
}