package org.aic.UI.Views.Employee;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class AddEmployeeView extends JFrame {

    private static String[] ROLES = { "Manager", "Cashier" };

    private JTextField surnameField, nameField, patronymicField;
    private JTextField salaryField, phoneField, cityField, streetField, zipField;
    private DatePicker dobField, dosField;
    private JComboBox<String> roleSelectorField;
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
        roleSelectorField = new JComboBox<>(ROLES);
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
        //addField(formPanel, "Role:", roleField);
        addField(formPanel, "Role Selector", roleSelectorField);
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

    public JButton getSaveButton() { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }

    public static void main(String[] args) {
        AddEmployeeView view = new AddEmployeeView();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }
}
