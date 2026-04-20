package org.aic.UI.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AddStoreProductView extends JFrame {

    // Поля вводу для Контролера
    private final JTextField upcField;
    private final JTextField upcSaleField;
    private final JTextField productIdField;
    private final JTextField priceField;
    private final JTextField qtyField;
    private final JCheckBox promoCheckBox;

    private final JButton saveButton;
    private final JButton cancelButton;

    public AddStoreProductView() {
        // 1. Налаштування вікна
        setTitle("ZLAGODA - Товар у магазині");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLayout(new BorderLayout());

        Color bgColor = new Color(204, 204, 204);
        getContentPane().setBackground(bgColor);

        // 2. Хедер
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        headerPanel.setBackground(bgColor);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JLabel logoLabel = new JLabel("ZLAGODA");
        logoLabel.setFont(new Font("Serif", Font.PLAIN, 28));
        headerPanel.add(logoLabel);
        add(headerPanel, BorderLayout.NORTH);

        // 3. Основна панель форми
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(bgColor);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 80, 20, 80));

        JLabel titleLabel = new JLabel("Додати товар у магазин");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Ініціалізація полів
        upcField = createStyledTextField();
        upcSaleField = createStyledTextField();
        productIdField = createStyledTextField();
        priceField = createStyledTextField();
        qtyField = createStyledTextField();

        promoCheckBox = new JCheckBox("Акційний товар");
        promoCheckBox.setBackground(bgColor);
        promoCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        promoCheckBox.setFocusPainted(false);


        addFormRow(mainPanel, "UPC", upcField);
        addFormRow(mainPanel, "UPC акційний", upcSaleField);
        addFormRow(mainPanel, "ID товару ", productIdField);
        addFormRow(mainPanel, "Ціна продажу", priceField);
        addFormRow(mainPanel, "Кількість у наявності", qtyField);

        promoCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(promoCheckBox);

        add(mainPanel, BorderLayout.CENTER);

        // 4. Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        buttonPanel.setBackground(bgColor);

        saveButton = new RoundedButton("Зберегти", 48);
        styleButton(saveButton, new Color(85, 85, 85), Color.WHITE);

        cancelButton = new RoundedButton("Скасувати", 48);
        styleButton(cancelButton, new Color(230, 230, 230), Color.DARK_GRAY);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // --- Допоміжні методи ---

    private void addFormRow(JPanel panel, String labelText, JComponent input) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(panel.getBackground());
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(label);
        row.add(Box.createRigidArea(new Dimension(0, 5)));
        row.add(input);
        panel.add(row);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    private JTextField createStyledTextField() {
        JTextField field = new RoundedTextField(15); // Радіус заокруглення
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBackground(new Color(180, 180, 180));
        field.setBorder(new EmptyBorder(5, 10, 5, 10));
        return field;
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder());
    }

    public String getUpc() { return upcField.getText(); }
    public String getUpcSale() { return upcSaleField.getText(); }
    public String getProductId() { return productIdField.getText(); }
    public String getPrice() { return priceField.getText(); }
    public String getQty() { return qtyField.getText(); }
    public boolean isPromotional() { return promoCheckBox.isSelected(); }

    public JButton getSaveButton() { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }


    static class RoundedTextField extends JTextField {
        private int radius;
        public RoundedTextField(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            super.paintComponent(g);
        }
    }

    static class RoundedButton extends JButton {
        private int radius;
        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AddStoreProductView().setVisible(true);
        });
    }
}