package com.ems.view;

import com.ems.controller.EmployeeController;
import com.ems.model.Employee;
import com.ems.view.components.ModernButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Dialog showing employee search results in a table.
// Offers edit/delete actions on the selected result, then refreshes the results.
public class SearchDialog extends JDialog {
    private static final Logger logger = LogManager.getLogger(SearchDialog.class);
    private EmployeeController employeeController;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private List<Employee> originalSearchResults;
    private String searchType;
    private String searchTerm;

    public SearchDialog(Frame parent, List<Employee> results, String searchType, String searchTerm) {
        super(parent, "Search Results", true);
        this.employeeController = new EmployeeController();
        this.originalSearchResults = results;
        this.searchType = searchType;
        this.searchTerm = searchTerm;
        initializeUI();
    }

    // Initialize the search results dialog UI
    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(800, 500);
        setLocationRelativeTo(getOwner());

        // Header panel with search summary
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String title = String.format("Search Results: %s - '%s' (%d found)",
                searchType, searchTerm, originalSearchResults.size());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Table panel for displaying results
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configure table model
        String[] columns = {"ID", "Name", "Age", "Type", "Department", "Email", "Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        configureTableAppearance();
        populateTable();

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel with actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        ModernButton editButton = new ModernButton("Edit Selected");
        editButton.setPreferredSize(new Dimension(140, 35));
        editButton.addActionListener(e -> editSelectedEmployee());

        ModernButton deleteButton = new ModernButton("Delete Selected");
        deleteButton.setPreferredSize(new Dimension(140, 35));
        deleteButton.setBackgroundColor(new Color(231, 76, 60));
        deleteButton.addActionListener(e -> deleteSelectedEmployee());

        ModernButton closeButton = new ModernButton("Close");
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        logger.info("Search dialog displayed with {} results", originalSearchResults.size());
    }

    // Configure table visual settings
    private void configureTableAppearance() {
        resultTable.setRowHeight(30);
        resultTable.setFont(new Font("Arial", Font.PLAIN, 12));
        resultTable.setSelectionBackground(new Color(52, 152, 219));
        resultTable.setSelectionForeground(Color.WHITE);
        resultTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        resultTable.getTableHeader().setBackground(new Color(240, 240, 240));
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // Populate table with search results
    private void populateTable() {
        tableModel.setRowCount(0);
        for (Employee emp : originalSearchResults) {
            Object[] row = {
                    emp.getId(),
                    emp.getName(),
                    emp.getAge(),
                    getDisplayType(emp.getEmployeeType()),
                    emp.getDepartment(),
                    emp.getEmail(),
                    String.format("%,.2f", emp.getSalary())
            };
            tableModel.addRow(row);
        }
        tableModel.fireTableDataChanged();
    }

    // Convert internal type code to display format
    private String getDisplayType(String type) {
        switch (type) {
            case "FULLTIME": return "Full-Time";
            case "PARTTIME": return "Part-Time";
            case "INTERN": return "Intern";
            default: return type;
        }
    }

    // Get selected employee ID from table
    private int getSelectedEmployeeId() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow >= 0) {
            Object idObj = tableModel.getValueAt(selectedRow, 0);
            if (idObj instanceof Integer) {
                return (Integer) idObj;
            } else if (idObj instanceof String) {
                try {
                    return Integer.parseInt((String) idObj);
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
        return -1;
    }

    // Open edit dialog for selected employee
    private void editSelectedEmployee() {
        int selectedId = getSelectedEmployeeId();
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
                EmployeeFormDialog dialog = new EmployeeFormDialog((Frame) getOwner(), "Edit Employee", employee);
                dialog.setVisible(true);
                if (dialog.isSuccess()) {
                    // Refresh data and re-perform original search
                    refreshSearchResults();
                }
            }
        } catch (Exception ex) {
            logger.error("Error editing employee", ex);
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete selected employee after confirmation
    private void deleteSelectedEmployee() {
        int selectedId = getSelectedEmployeeId();
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
                    // Refresh data and re-perform original search
                    refreshSearchResults();
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

    // Refresh search results with current data
    private void refreshSearchResults() {
        // Reload fresh data from file
        employeeController.reloadFromFile();

        // Re-perform original search with current data
        List<Employee> searchResults;
        if ("Employee ID".equals(searchType)) {
            try {
                int searchId = Integer.parseInt(searchTerm);
                searchResults = employeeController.searchById(searchId);
            } catch (NumberFormatException e) {
                searchResults = new ArrayList<>();
            }
        } else {
            searchResults = employeeController.searchByName(searchTerm);
        }

        // Update stored results
        originalSearchResults.clear();
        originalSearchResults.addAll(searchResults);

        // Refresh table display
        populateTable();

        // Update title with current count
        Component[] comps = ((JPanel) getContentPane().getComponent(0)).getComponents();
        if (comps.length > 0 && comps[0] instanceof JLabel) {
            JLabel titleLabel = (JLabel) comps[0];
            titleLabel.setText(String.format("Search Results: %s - '%s' (%d found)",
                    searchType, searchTerm, searchResults.size()));
        }
    }
}