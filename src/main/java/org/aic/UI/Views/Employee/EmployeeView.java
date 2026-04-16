/*

public class EmployeeView extends JFrame {

    // Text fields for the UI
    private JTextField idField, surnameField, nameField, patronymicField, roleField;
    private JTextField salaryField, dobField, dosField, phoneField, cityField, streetField, zipField;

    public EmployeeView(EmployeeDTO employee) {
        // Set up the main window
        setTitle("Employee Profile");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centers the window

        // Create a panel for the form with a 12-row, 2-column grid
        JPanel formPanel = new JPanel(new GridLayout(12, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize fields with data from the DTO
        idField = new JTextField(employee.getId());
        idField.setEditable(false); // Make ID read-only
        surnameField = new JTextField(employee.getSurname());
        nameField = new JTextField(employee.getName());
        patronymicField = new JTextField(employee.getPatronymic());
        roleField = new JTextField(employee.getRole());
        salaryField = new JTextField(String.valueOf(employee.getSalary()));
        dobField = new JTextField(employee.getDateOfBirth());
        dosField = new JTextField(employee.getDateOfStart());
        phoneField = new JTextField(employee.getPhoneNumber());
        cityField = new JTextField(employee.getCity());
        streetField = new JTextField(employee.getStreet());
        zipField = new JTextField(employee.getZipCode());

        // Add labels and fields to the panel
        addField(formPanel, "Employee ID:", idField);
        addField(formPanel, "Surname:", surnameField);
        addField(formPanel, "Name:", nameField);
        addField(formPanel, "Patronymic:", patronymicField);
        addField(formPanel, "Role:", roleField);
        addField(formPanel, "Salary:", salaryField);
        addField(formPanel, "Date of Birth:", dobField);
        addField(formPanel, "Start Date:", dosField);
        addField(formPanel, "Phone Number:", phoneField);
        addField(formPanel, "City:", cityField);
        addField(formPanel, "Street:", streetField);
        addField(formPanel, "Zip Code:", zipField);

        // Create a panel for the buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");

        // Simple action listener for testing the Save button
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Saved profile for: " + nameField.getText());
        });

        cancelButton.addActionListener(e -> dispose()); // Closes the window

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to the frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Helper method to add label and field pairs easily
    private void addField(JPanel panel, String labelText, JTextField field) {
        panel.add(new JLabel(labelText));
        panel.add(field);
    }
}*/
