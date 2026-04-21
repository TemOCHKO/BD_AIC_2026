package org.aic.UI.Views;

import org.aic.DBModels.ProductDBModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class ProductDialog extends JDialog {

    // ── UI Components ───────────────────────────────────────────────
    private JTextField txtTitle;
    private JTextField txtManufacturer;
    private JTextArea txtCharacteristics;
    private JComboBox<String> cbCategory;

    private JButton btnSave;
    private JButton btnCancel;

    // ── Colors from your theme ─────────────────────────────────────
    private final Color BG_MAIN = new Color(0xCCCCCC); // Light gray background
    private final Color FIELD_BG = new Color(0x404040); // Dark gray fields
    private final Color FIELD_FG = Color.WHITE;
    private final Color BTN_SAVE_BG = new Color(0x606060);

    private ProductDBModel currentProduct;

    public ProductDialog(JFrame parent, String title) {
        super(parent, title, true); // true makes it Modal
        setSize(500, 550);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Root panel
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_MAIN);
        root.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Header
        JLabel lblHeader = new JLabel(title);
        lblHeader.setFont(new Font("Georgia", Font.PLAIN, 24));
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        root.add(lblHeader, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 1, 10, 5));
        formPanel.setBackground(BG_MAIN);

        // Назва (Title)
        formPanel.add(createLabel("НАЗВА"));
        txtTitle = createTextField();
        formPanel.add(txtTitle);

        // Виробник (Manufacturer)
        formPanel.add(createLabel("ВИРОБНИК"));
        txtManufacturer = createTextField();
        formPanel.add(txtManufacturer);

        // Характеристики (Characteristics)
        formPanel.add(createLabel("ХАРАКТЕРИСТИКИ"));
        txtCharacteristics = new JTextArea(3, 20);
        txtCharacteristics.setBackground(FIELD_BG);
        txtCharacteristics.setForeground(FIELD_FG);
        txtCharacteristics.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtCharacteristics.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        txtCharacteristics.setLineWrap(true);
        formPanel.add(new JScrollPane(txtCharacteristics));

        // Категорія (Category)
        formPanel.add(createLabel("КАТЕГОРІЯ"));
        cbCategory = new JComboBox<>();
        cbCategory.setBackground(Color.WHITE);
        cbCategory.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(cbCategory);

        root.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        btnPanel.setBackground(BG_MAIN);

        btnSave = new JButton("Зберегти");
        btnSave.setPreferredSize(new Dimension(120, 40));
        btnSave.setBackground(BTN_SAVE_BG);
        //btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);

        btnCancel = new JButton("Скасувати");
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.setBackground(new Color(0xE0E0E0));
        btnCancel.setFocusPainted(false);

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        root.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Helper Methods for Styling ─────────────────────────────────
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(FIELD_BG);
        field.setForeground(FIELD_FG);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    // ── Getters & Setters for the Controller ───────────────────────
    public void populateCategories(String[] categories) {
        cbCategory.removeAllItems();
        for (String c : categories) cbCategory.addItem(c);
    }

    public void setProductData(ProductDBModel p, String categoryName) {
        this.currentProduct = p;
        txtTitle.setText(p.getTitle());
        txtManufacturer.setText(p.getManufacturer());
        txtCharacteristics.setText(p.getDescription()); // Or getCharacteristics()
        if (categoryName != null) {
            cbCategory.setSelectedItem(categoryName);
        }
    }

    public ProductDBModel getCurrentProduct() { return currentProduct; }
    public String getTitleInput() { return txtTitle.getText().trim(); }
    public String getManufacturerInput() { return txtManufacturer.getText().trim(); }
    public String getCharacteristicsInput() { return txtCharacteristics.getText().trim(); }
    public String getSelectedCategory() { return (String) cbCategory.getSelectedItem(); }

    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }
}