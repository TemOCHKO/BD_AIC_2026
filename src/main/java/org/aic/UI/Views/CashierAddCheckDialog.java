package org.aic.UI.Views;

import org.aic.UI.Controllers.ComboItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CashierAddCheckDialog extends JDialog {

    private JComboBox<ComboItem> cbCashier;
    private JComboBox<ComboItem> cbClientCard;
    private JTextField txtSumTotal;
    private JTextField txtVat;

    private JButton btnSave;
    private JButton btnCancel;

    private final Color BG_MAIN = new Color(0xCCCCCC);
    private final Color FIELD_BG = new Color(0x404040);
    private final Color FIELD_FG = Color.WHITE;

    public CashierAddCheckDialog(JFrame parent) {
        super(parent, "Створити новий чек", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_MAIN);
        root.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblHeader = new JLabel("Новий чек");
        lblHeader.setFont(new Font("Georgia", Font.PLAIN, 24));
        lblHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        root.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(8, 1, 5, 2));
        formPanel.setBackground(BG_MAIN);

        // Касир
        formPanel.add(createLabel("ОБЕРІТЬ КАСИРА"));
        cbCashier = new JComboBox<>();
        cbCashier.setBackground(Color.WHITE);
        formPanel.add(cbCashier);

        // Картка клієнта
        formPanel.add(createLabel("КАРТКА КЛІЄНТА (Необов'язково)"));
        cbClientCard = new JComboBox<>();
        cbClientCard.setBackground(Color.WHITE);
        formPanel.add(cbClientCard);

        // Сума
        formPanel.add(createLabel("ЗАГАЛЬНА СУМА (грн)"));
        txtSumTotal = createTextField();
        formPanel.add(txtSumTotal);

        // ПДВ
        formPanel.add(createLabel("ПДВ (20% - розраховується автоматично)"));
        txtVat = createTextField();
        txtVat.setEditable(false); // Забороняємо ручне введення ПДВ
        txtVat.setBackground(new Color(0x505050)); // Трохи світліший колір для неактивного поля
        formPanel.add(txtVat);

        root.add(formPanel, BorderLayout.CENTER);

        // Кнопки
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        btnPanel.setBackground(BG_MAIN);

        btnSave = new JButton("Зберегти чек");
        btnSave.setBackground(new Color(0x606060));
        btnSave.setForeground(Color.BLACK);

        btnCancel = new JButton("Скасувати");

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

    // Геттери
    public JComboBox<ComboItem> getCbCashier() { return cbCashier; }
    public JComboBox<ComboItem> getCbClientCard() { return cbClientCard; }
    public JTextField getTxtSumTotal() { return txtSumTotal; }
    public JTextField getTxtVat() { return txtVat; }
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }
}
