package org.aic.UI.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddNewProductView extends JFrame {
    // UI Components exposed for the Controller
    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField manufacturerField;
    private final JTextField characteristicsField;
    private JComboBox<String> categoryComboBox;

    private final JButton saveButton;
    private final JButton cancelButton;

    public AddNewProductView() {

        //region Frame
        // 1. Frame Setup
        setTitle("ZLAGODA - Додати товар");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLayout(new BorderLayout());

        // Match the background color from the mockup
        Color bgColor = new Color(204, 204, 204);
        getContentPane().setBackground(bgColor);
        //endregion

        //region HeaderPanel
        // ==========================================
        // 2. HEADER PANEL (Top)
        // ==========================================
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        headerPanel.setBackground(bgColor);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // Bottom border line

        JLabel logoLabel = new JLabel("ZLAGODA");
        logoLabel.setFont(new Font("Serif", Font.PLAIN, 28));
        headerPanel.add(logoLabel);

        add(headerPanel, BorderLayout.NORTH);
        //endregion

        // ==========================================
        // 3. MAIN FORM PANEL (Center)
        // ==========================================
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(bgColor);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 80, 20, 80)); // Padding around the form

        // Title
        JLabel titleLabel = new JLabel("Додати нового товару");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spacer

        // Initialize input fields
        idField = createStyledTextField();
        nameField = createStyledTextField();
        manufacturerField = createStyledTextField();
        characteristicsField = createStyledTextField();

        // Mock categories for the dropdown
        String[] categories = {"Оберіть категорію...", "Молочні продукти", "М'ясо", "Бакалія", "Напої"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        categoryComboBox.setBackground(new Color(180, 180, 180));
        categoryComboBox.setFocusable(false);

        // Add fields to the panel
        // addFormRow(mainPanel, "ID товару", idField);
        addFormRow(mainPanel, "Назва", nameField);
        addFormRow(mainPanel, "Виробник", manufacturerField);
        addFormRow(mainPanel, "Характеристики", characteristicsField);
        addFormRow(mainPanel, "Категорія", categoryComboBox);

        add(mainPanel, BorderLayout.CENTER);

        //region Bottom Panel
        // ==========================================
        // 4. BUTTON PANEL (Bottom)
        // ==========================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        buttonPanel.setBackground(bgColor);

        saveButton = new JButton("Зберегти");
        styleButton(saveButton, new Color(85, 85, 85), Color.WHITE); // Dark gray

        cancelButton = new JButton("Скасувати");
        styleButton(cancelButton, new Color(230, 230, 230), Color.DARK_GRAY); // Light gray

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        //endregion
    }

    // --- Helper Methods for Styling ---

    private void addFormRow(JPanel panel, String labelText, JComponent inputComponent) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.Y_AXIS));
        rowPanel.setBackground(panel.getBackground());
        rowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        rowPanel.add(label);
        rowPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Tiny space between label and field
        rowPanel.add(inputComponent);

        panel.add(rowPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15))); // Space between rows
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // Force height, allow wide expansion
        field.setBackground(new Color(180, 180, 180)); // Match the gray input box color
        field.setBorder(new EmptyBorder(5, 10, 5, 10)); // Inner padding
        field.setCaretColor(Color.BLACK);
        return field;
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder()); // Flat design
        // Note: Pure Swing doesn't support rounded button corners natively without overriding paintComponent.
        // This flat design mimics the modern feel of the mockup.
    }

    // Method for Categories
    public void changeCategoryComboBox(String[] categories) {
        categoryComboBox.removeAllItems();
        for (var value : categories) categoryComboBox.addItem(value);
    }

    // --- Getters for the Controller ---

    public JTextField getIdField() { return idField; }
    public JTextField getNameField() { return nameField; }
    public JTextField getManufacturerField() { return manufacturerField; }
    public JTextField getCharacteristicsField() { return characteristicsField; }
    public JComboBox<String> getCategoryComboBox() { return categoryComboBox; }
    public JButton getSaveButton() { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }

    // --- Main Method to preview the UI ---

    public static void main(String[] args) {
        // Set cross-platform look and feel for consistent colors
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            AddNewProductView view = new AddNewProductView();
            view.setLocationRelativeTo(null); // Center on screen
            view.setVisible(true);
        });
    }
}
