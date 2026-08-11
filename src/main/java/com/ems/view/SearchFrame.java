package com.ems.view;

import com.ems.controller.EmployeeController;
import com.ems.model.Employee;
import com.ems.view.components.ModernButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Dialog for searching employees by ID (exact match) or name (partial, case-insensitive).
public class SearchFrame extends JDialog {
    private static final Logger logger = LogManager.getLogger(SearchFrame.class);
    private EmployeeController employeeController;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;

    public SearchFrame(Frame parent) {
        super(parent, "Search Employee", true);
        this.employeeController = new EmployeeController();
        initializeUI();
    }

    // Initialize search dialog UI
    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(500, 250);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        // Main panel with form layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dialog title
        JLabel titleLabel = new JLabel("Search Employee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);
        gbc.insets = new Insets(10, 10, 10, 10);

        // Search type selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel typeLabel = new JLabel("Search By:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        String[] searchTypes = {"Employee ID", "Employee Name"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        searchTypeCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        searchTypeCombo.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(searchTypeCombo, gbc);
        gbc.weightx = 0;

        // Search term input
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel searchLabel = new JLabel("Search Term:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(searchLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        searchField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(searchField, gbc);
        gbc.weightx = 0;

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        ModernButton searchButton = new ModernButton("Search");
        searchButton.setPreferredSize(new Dimension(120, 35));
        searchButton.addActionListener(e -> performSearch());

        ModernButton cancelButton = new ModernButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.setBackgroundColor(new Color(231, 76, 60));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set Enter key to trigger search
        getRootPane().setDefaultButton(searchButton);
    }

    // Execute search based on selected criteria
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        String searchType = (String) searchTypeCombo.getSelectedItem();

        // Validate input
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a search term",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Employee> searchResults;

            if ("Employee ID".equals(searchType)) {
                // ID search - exact match
                try {
                    int searchId = Integer.parseInt(searchTerm);
                    searchResults = employeeController.searchById(searchId);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a valid numeric ID",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                // Name search - partial, case-insensitive match
                searchResults = employeeController.searchByName(searchTerm);
            }

            // Handle search results
            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No employees found matching your search criteria",
                        "No Results",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Display results in separate dialog
                SearchDialog resultsDialog = new SearchDialog(
                        (Frame) getOwner(),
                        searchResults,
                        searchType,
                        searchTerm
                );
                resultsDialog.setVisible(true);
                dispose();
            }

            logger.info("Search performed: {}='{}', found {} results",
                    searchType, searchTerm, searchResults.size());

        } catch (Exception ex) {
            logger.error("Error performing search", ex);
            JOptionPane.showMessageDialog(this,
                    "Error during search: " + ex.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}