package org.aic.UI.Views;
import org.aic.DTOModels.Employee.EmployeeListDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeesListView extends JFrame {

    public EmployeesListView(List<EmployeeListDTO> employees) {
        // Set up the main window
        setTitle("Employee Directory");
        setSize(600, 400); // Made it wider to fit the table columns nicely
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centers the window

        // 1. Define the column headers
        String[] columnNames = {"Surname", "Name", "Role", "Date of Birth"};

        // 2. Create the table model
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // 3. Loop through the list of DTOs and add them as rows to the model
        for (EmployeeListDTO emp : employees) {
            Object[] rowData = {
                    emp.getSurname(),
                    emp.getName(),
                    emp.getRole(),
                    emp.getDateOfBirth()
            };
            tableModel.addRow(rowData);
        }

        // 4. Create the JTable with the model
        JTable employeeTable = new JTable(tableModel);

        // Optional: Make the table read-only (so users can't edit cells directly)
        employeeTable.setDefaultEditor(Object.class, null);

        // 5. Add the table to a ScrollPane (crucial for column headers to show and for scrolling)
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        // Add a title at the top
        JLabel titleLabel = new JLabel("All Employees", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add components to the frame
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add a simple close button at the bottom
        JPanel bottomPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}