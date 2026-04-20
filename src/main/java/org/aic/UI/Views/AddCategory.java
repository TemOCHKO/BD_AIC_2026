package org.aic.UI.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddCategory extends JFrame {

    private static final Color BG_COLOR = new Color(204, 204, 204);

    private final JTextField nameField;
    private final JButton saveButton;
    private final JButton cancelButton;

    public AddCategory(String mode) {
        setTitle("ZLAGODA - " + (mode.equals("add") ? "Додати категорію" : "Редагувати категорію"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 340);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        //region Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        JLabel logo = new JLabel("ZLAGODA");
        logo.setFont(new Font("Serif", Font.PLAIN, 28));
        headerPanel.add(logo);
        add(headerPanel, BorderLayout.NORTH);
        //endregion

        //region Form
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 80, 20, 80));

        JLabel titleLabel = new JLabel(mode.equals("add") ? "Додати нову категорію" : "Редагувати категорію");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        nameField = createStyledTextField();
        addFormRow(mainPanel, "Назва категорії", nameField);

        add(mainPanel, BorderLayout.CENTER);
        //endregion

        //region Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BG_COLOR);

        saveButton   = new RoundedButton(mode.equals("add") ? "Зберегти" : "Оновити", 48);
        cancelButton = new RoundedButton("Скасувати", 48);

        styleButton(saveButton,   new Color(85, 85, 85),   Color.WHITE);
        styleButton(cancelButton, new Color(230, 230, 230), Color.DARK_GRAY);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        //endregion
    }

    public void populate(String name) {
        nameField.setText(name);
    }

    //region Helpers
    private void addFormRow(JPanel panel, String labelText, JComponent input) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(BG_COLOR);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(label);
        row.add(Box.createRigidArea(new Dimension(0, 4)));
        row.add(input);
        panel.add(row);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
    }

    private JTextField createStyledTextField() {
        JTextField field = new RoundedTextField(48);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBackground(new Color(180, 180, 180));
        field.setBorder(new EmptyBorder(5, 10, 5, 10));
        field.setCaretColor(Color.BLACK);
        return field;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setMinimumSize(new Dimension(140, 40));
        btn.setMaximumSize(new Dimension(140, 40));
    }
    //endregion

    //region Getters
    public JTextField getNameField()   { return nameField; }
    public JButton    getSaveButton()  { return saveButton; }
    public JButton    getCancelButton(){ return cancelButton; }
    //endregion

    // Додайте це всередині класу AddCategory або в окремий файл
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Встановлюємо вигляд системи для кнопок та полів
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            // Створюємо вікно в режимі додавання ("add")
            // Якщо хочете режим редагування, змініть на "edit"
            AddCategory frame = new AddCategory("add");

            // Налаштовуємо дію при натисканні "Скасувати"
            frame.getCancelButton().addActionListener(e -> frame.dispose());

            // Налаштовуємо дію при натисканні "Зберегти"
            frame.getSaveButton().addActionListener(e -> {
                String name = frame.getNameField().getText();
                if (!name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Категорію '" + name + "' збережено!");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Введіть назву категорії!", "Помилка", JOptionPane.WARNING_MESSAGE);
                }
            });

            frame.setVisible(true);
        });
    }
}