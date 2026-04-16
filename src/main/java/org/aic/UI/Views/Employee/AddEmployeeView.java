package org.aic.UI.Views.Employee;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class AddEmployeeView extends JFrame {

    private JTextField surnameField, nameField, patronymicField, roleField;
    private JTextField salaryField, phoneField, cityField, streetField, zipField;
    private DatePicker dobField, dosField;
    private JButton saveButton, cancelButton;

    public AddEmployeeView() {
        // Set up the main window
        setTitle("Add New Employee");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes just this window, not the whole app
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create a panel for the form with an 11-row, 2-column grid
        JPanel formPanel = new JPanel(new GridLayout(11, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize empty text fields
        surnameField = new JTextField();
        nameField = new JTextField();
        patronymicField = new JTextField();
        roleField = new JTextField();
        salaryField = new JTextField();

        dobField = new DatePicker();
        dosField = new DatePicker();
        dobField.setDateToToday();
        dosField.setDateToToday();

        phoneField = new JTextField();
        cityField = new JTextField();
        streetField = new JTextField();
        zipField = new JTextField();

        // Add labels and fields to the panel
        addField(formPanel, "Surname:", surnameField);
        addField(formPanel, "Name:", nameField);
        addField(formPanel, "Patronymic:", patronymicField);
        addField(formPanel, "Role:", roleField);
        addField(formPanel, "Salary:", salaryField);
        addField(formPanel, "Date of Birth", dobField);
        addField(formPanel, "Date of Start", dosField);
        addField(formPanel, "Phone Number:", phoneField);
        addField(formPanel, "City:", cityField);
        addField(formPanel, "Street:", streetField);
        addField(formPanel, "Zip Code:", zipField);

        // Create the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save Employee");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add everything to the frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    /*// Helper method to gather data and simulate saving
    private void saveEmployee() {
        try {
            // 1. Generate a mock ID (or leave it null for the Database to handle)
            String generatedId = "EMP-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

            // 2. Parse the salary safely
            double salary = Double.parseDouble(salaryField.getText());

            // 3. Create the Employee Model/DTO behind the scenes
            EmployeeDTO newEmployee = new EmployeeDTO(
                    generatedId,
                    surnameField.getText(),
                    nameField.getText(),
                    patronymicField.getText(),
                    roleField.getText(),
                    salary,
                    dobField.getText(),
                    dosField.getText(),
                    phoneField.getText(),
                    cityField.getText(),
                    streetField.getText(),
                    zipField.getText()
            );

            // 4. Show success message (In a real app, you'd send 'newEmployee' to your DB here)
            JOptionPane.showMessageDialog(this,
                    "Employee Saved Successfully!\nAssigned ID: " + generatedId,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear the form or close the window
            dispose();

        } catch (NumberFormatException ex) {
            // Handle the case where the user types letters in the salary field
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for Salary.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }*/

    // Helper method to add label and field pairs easily
    private void addField(JPanel panel, String labelText, JTextField field) {
        panel.add(new JLabel(labelText));
        panel.add(field);
    }

    private void addField(JPanel panel, String labelText, DatePicker datePicker) {
        panel.add(new JLabel(labelText));
        panel.add(datePicker);
    }

    public DatePicker getDobField() { return dobField; }
    public DatePicker getDosField() { return dosField; }

    public JTextField getSurnameField() { return surnameField; }
    public JTextField getNameField() { return nameField; }
    public JTextField getPatronymicField() { return patronymicField; }
    public JTextField getRoleField() { return roleField; }
    public JTextField getSalaryField() { return salaryField; }
    public JTextField getPhoneField() { return phoneField; }
    public JTextField getCityField() { return cityField; }
    public JTextField getStreetField() { return streetField; }
    public JTextField getZipField() { return zipField; }

    public JButton getSaveButton() { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }

    public static void main(String[] args) {
        AddEmployeeView view = new AddEmployeeView();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }
}
