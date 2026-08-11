package com.ems.view;

import com.ems.model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Reusable table panel displaying employee records in a JTable.
// Table data is refreshed through updateTable() when records change.
public class EmployeeTablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public EmployeeTablePanel() {
        setLayout(new BorderLayout());
        initializeTable();
    }

    private void initializeTable() {
        String[] columns = {"ID", "Name", "Age", "Type", "Department", "Email", "Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));

        // Selection colors
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setBackground(Color.WHITE);

        // Header styling
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);

        // Grid lines
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        // Selection settings
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(180); // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Age
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Type
        table.getColumnModel().getColumn(4).setPreferredWidth(130); // Department
        table.getColumnModel().getColumn(5).setPreferredWidth(220); // Email
        table.getColumnModel().getColumn(6).setPreferredWidth(120); // Salary

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateTable(List<Employee> employees) {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
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

    private String getDisplayType(String type) {
        switch (type) {
            case "FULLTIME": return "Full-Time";
            case "PARTTIME": return "Part-Time";
            case "INTERN": return "Intern";
            default: return type;
        }
    }

    public int getSelectedEmployeeId() {
        int selectedRow = table.getSelectedRow();
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

    public JTable getTable() {
        return table;
    }
}