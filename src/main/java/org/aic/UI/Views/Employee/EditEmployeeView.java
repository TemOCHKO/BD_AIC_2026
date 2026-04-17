package org.aic.UI.Views.Employee;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EditEmployeeView extends JFrame {

    private static String[] ROLES = {"Manager", "Cashier"};

    // Added idField for the Edit View
    private JTextField surnameField, nameField, patronymicField, roleField;
    private JTextField salaryField, phoneField, cityField, streetField, zipField;
    private JComboBox<String> roleSelectorField;
    private DatePicker dobField, dosField;
    private JButton updateButton, cancelButton;

    public EditEmployeeView() {
        // Set up the main window
        setTitle("Edit Employee");
        setSize(400, 580); // Slightly taller to accommodate the ID field
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create a panel for the form with a 12-row, 2-column grid (added 1 row for ID)
        JPanel formPanel = new JPanel(new GridLayout(12, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        surnameField = new JTextField();
        nameField = new JTextField();
        patronymicField = new JTextField();
        roleField = new JTextField();
        roleSelectorField = new JComboBox<>(ROLES);
        salaryField = new JTextField();

        dobField = new DatePicker();
        dosField = new DatePicker();

        phoneField = new JTextField();
        cityField = new JTextField();
        streetField = new JTextField();
        zipField = new JTextField();

        // Add labels and fields to the panel
        addField(formPanel, "Surname:", surnameField);
        addField(formPanel, "Name:", nameField);
        addField(formPanel, "Patronymic:", patronymicField);
        // addField(formPanel, "Role:", roleField);
        addField(formPanel, "Role Selector", roleSelectorField);
        addField(formPanel, "Salary:", salaryField);
        addField(formPanel, "Date of Birth:", dobField);
        addField(formPanel, "Date of Start:", dosField);
        addField(formPanel, "Phone Number:", phoneField);
        addField(formPanel, "City:", cityField);
        addField(formPanel, "Street:", streetField);
        addField(formPanel, "Zip Code:", zipField);

        // Create the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        updateButton = new JButton("Update Employee"); // Renamed button
        cancelButton = new JButton("Cancel");

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        // Add everything to the frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Helper method to add label and field pairs easily
    private void addField(JPanel panel, String labelText, JTextField field) {
        panel.add(new JLabel(labelText));
        panel.add(field);
    }

    private void addField(JPanel panel, String labelText, DatePicker datePicker) {
        panel.add(new JLabel(labelText));
        panel.add(datePicker);
    }

    private void addField(JPanel panel, String labelText, JComboBox<String> comboBox) {
        panel.add(new JLabel(labelText));
        panel.add(comboBox);
    }


    // ==========================================
    // DATA POPULATION METHOD (For the Controller)
    // ==========================================
    public void setEmployeeData(String surname, String name, String patronymic,
                                String role, String salary, LocalDate dob, LocalDate dos,
                                String phone, String city, String street, String zip) {
        surnameField.setText(surname);
        nameField.setText(name);
        patronymicField.setText(patronymic);
        roleSelectorField.setSelectedItem(role);
        salaryField.setText(salary);

        if (dob != null) dobField.setDate(dob);
        if (dos != null) dosField.setDate(dos);

        phoneField.setText(phone);
        cityField.setText(city);
        streetField.setText(street);
        zipField.setText(zip);
    }

    // ==========================================
    // GETTERS
    // ==========================================
    public DatePicker getDobField() { return dobField; }
    public DatePicker getDosField() { return dosField; }
    public JTextField getSurnameField() { return surnameField; }
    public JTextField getNameField() { return nameField; }
    public JTextField getPatronymicField() { return patronymicField; }
    public JComboBox<String> getRoleField() { return roleSelectorField; }
    public JTextField getSalaryField() { return salaryField; }
    public JTextField getPhoneField() { return phoneField; }
    public JTextField getCityField() { return cityField; }
    public JTextField getStreetField() { return streetField; }
    public JTextField getZipField() { return zipField; }

    public JButton getUpdateButton() { return updateButton; }
    public JButton getCancelButton() { return cancelButton; }

    // Test the View
    public static void main(String[] args) {
        EditEmployeeView view = new EditEmployeeView();

        // Simulating the controller injecting data into the view
        view.setEmployeeData(
                "Smith", "John", "Edward", "Manager", "75000",
                LocalDate.of(1990, 5, 15), LocalDate.of(2021, 8, 1),
                "555-0198", "Seattle", "123 Tech Lane", "98101"
        );

        view.setVisible(true);
    }
}