package org.aic.UI.Views;

import org.aic.DBModels.StoreProductDBModel;
import org.aic.DBModels.ProductDBModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class StoreProductDialog extends JDialog {

    private JLabel lblLabel;
    private JTextField txtUPC;
    private JTextField txtUPCProm;
    private JComboBox<ProductItem> cbProducts;
    private JTextField txtPrice;
    private JTextField txtQuantity;
    private JCheckBox chkPromotional;

    private JButton btnSave;
    private JButton btnCancel;

    private final Color BG_MAIN = new Color(0xCCCCCC);
    private final Color FIELD_BG = new Color(0x404040);
    private final Color FIELD_FG = Color.WHITE;

    private StoreProductDBModel currentItem;

    public StoreProductDialog(JFrame parent, String title) {
        super(parent, title, true);
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_MAIN);
        root.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblHeader = new JLabel(title);
        lblHeader.setFont(new Font("Georgia", Font.PLAIN, 24));
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        root.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(11, 1, 5, 2));
        formPanel.setBackground(BG_MAIN);

        // UPC (Primary Key)
        lblLabel = new JLabel("UPC ТОВАРУ (12 цифр)");
        formPanel.add(lblLabel);
        txtUPC = createTextField();
        txtUPC.setEditable(false);
        txtUPC.setFocusable(false);
        formPanel.add(txtUPC);

        // Базовий товар
        formPanel.add(createLabel("ОБЕРІТЬ ТОВАР"));
        cbProducts = new JComboBox<>();
        cbProducts.setBackground(Color.WHITE);
        formPanel.add(cbProducts);

        // Ціна
        formPanel.add(createLabel("ЦІНА ПРОДАЖУ"));
        txtPrice = createTextField();
        formPanel.add(txtPrice);

        // Кількість
        formPanel.add(createLabel("КІЛЬКІСТЬ НА СКЛАДІ"));
        txtQuantity = createTextField();
        formPanel.add(txtQuantity);

        // Акційний чекбокс
        chkPromotional = new JCheckBox("Акційний товар");
        chkPromotional.setBackground(BG_MAIN);
        chkPromotional.setVisible(false);
        formPanel.add(chkPromotional);

        // UPC Акційного (якщо є)
        //formPanel.add(createLabel("UPC АКЦІЙНОГО (необов'язково)"));
        txtUPCProm = createTextField();
        txtUPCProm.setForeground(Color.WHITE);
        txtUPCProm.setVisible(false);
        formPanel.add(txtUPCProm);

        // Створюємо панель-обгортку, щоб поля не розтягувалися по всій висоті екрану
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(BG_MAIN);
        formWrapper.add(formPanel, BorderLayout.NORTH); // NORTH притискає всі елементи догори!

        root.add(formWrapper, BorderLayout.CENTER);

        // Кнопки
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        btnPanel.setBackground(BG_MAIN);

        btnSave = new JButton("Зберегти");
        btnSave.setPreferredSize(new Dimension(120, 40));
        btnSave.setBackground(new Color(0x606060));
        //btnSave.setForeground(Color.WHITE);

        btnCancel = new JButton("Скасувати");
        btnCancel.setPreferredSize(new Dimension(120, 40));

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        root.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(FIELD_BG);
        field.setForeground(FIELD_FG);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    // Допоміжний клас для JComboBox, щоб зберігати і назву, і ID
    public static class ProductItem {
        public int id;
        public String name;
        public ProductItem(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }

    // Геттери/сеттери
    public void setProductsList(List<ProductItem> items) {
        cbProducts.removeAllItems();
        for (ProductItem i : items) cbProducts.addItem(i);
    }

    public void setData(StoreProductDBModel item) {
        this.currentItem = item;
        txtUPC.setText(item.getUPC());
        txtUPC.setEditable(false); // UPC зазвичай не редагують після створення
        txtUPCProm.setText(item.getUPC_prom());
        txtPrice.setText(String.valueOf(item.getSelling_price()));
        txtQuantity.setText(String.valueOf(item.getProducts_number()));
        chkPromotional.setSelected(item.getPromotional_product());

        // Вибір товару в комбобоксі за ID
        for (int i = 0; i < cbProducts.getItemCount(); i++) {
            if (cbProducts.getItemAt(i).id == item.getId_product()) {
                cbProducts.setSelectedIndex(i);
                break;
            }
        }
    }
    public JTextField getTxtUPC() {
        return txtUPC;
    }

    public String getUPC() { return txtUPC.getText().trim(); }
    public String getUPCProm() { return txtUPCProm.getText().trim(); }
    public int getSelectedProductId() { return ((ProductItem) cbProducts.getSelectedItem()).id; }
    public double getPrice() { return Double.parseDouble(txtPrice.getText()); }
    public int getQuantity() { return Integer.parseInt(txtQuantity.getText()); }
    public boolean isPromotional() { return chkPromotional.isSelected(); }
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }
    public void setUPCVisible(boolean isVisible) {
        lblLabel.setVisible(isVisible);
        txtUPC.setVisible(isVisible);
    }
}