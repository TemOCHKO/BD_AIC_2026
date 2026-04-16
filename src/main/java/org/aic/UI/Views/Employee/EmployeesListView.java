package org.aic.UI.Views.Employee;
import org.aic.DTOModels.Employee.EmployeeListDTO;
import org.aic.DTOModels.ProductTableDTO;
import org.aic.DTOModels.ProductTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeesListView extends JFrame {

    private final EmployeeTableModel tableModel;
    private final JButton loadDataButton;
    private final JButton createNewEmployeeButton;

    public EmployeesListView() {
        // Set up the main window
        setTitle("Employee Directory");
        setSize(600, 400); // Made it wider to fit the table columns nicely
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centers the window

        tableModel = new EmployeeTableModel();

        // 4. Create the JTable with the model
        JTable employeeTable = new JTable(tableModel);

        // Make the table read-only (so users can't edit cells directly)
        employeeTable.setDefaultEditor(Object.class, null);

        // ---> HERE IS THE SINGLE SELECTION RULE <---
        // Restrict the table so the user can only select one employee at a time
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        loadDataButton = new JButton("Load employees");
        createNewEmployeeButton = new JButton("Create New Employee");
        bottomPanel.add(loadDataButton);
        bottomPanel.add(createNewEmployeeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public EmployeeTableModel getTableModel() { return tableModel; }

    public JButton getLoadDataButton() { return loadDataButton; }
    public JButton getCreateNewEmployeeButton() { return createNewEmployeeButton; }

    public void displayEmployees(Iterable<EmployeeListDTO> employees) {
        tableModel.setProducts((List<EmployeeListDTO>) employees);
    }
}