package org.aic.UI.Views;

import org.aic.UI.Controllers.ComboItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CheckEditDialog extends JDialog {

    private JTextField txtCheckNumber;
    private JComboBox<ComboItem> cbCashier;
    private JComboBox<ComboItem> cbClientCard;
    private JTextField txtSumTotal;
    private JTextField txtVat;

    private JButton btnSave;
    private JButton btnCancel;

    private final Color BG_MAIN = new Color(0xCCCCCC);
    private final Color FIELD_BG = new Color(0x404040);
    private final Color FIELD_FG = Color.WHITE;

    public CheckEditDialog(JFrame parent) {
        super(parent, "Редагування чека (Менеджер)", true);
        setSize(420, 500);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_MAIN);
        root.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblHeader = new JLabel("Редагування чека");
        lblHeader.setFont(new Font("Georgia", Font.PLAIN, 24));
        lblHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        root.add(lblHeader, BorderLayout.NORTH);

        // 10 рядків (5 полів + 5 лейблів)
        JPanel formPanel = new JPanel(new GridLayout(10, 1, 5, 2));
        formPanel.setBackground(BG_MAIN);

        // Номер чека (Read-only)
        formPanel.add(createLabel("НОМЕР ЧЕКА "));
        txtCheckNumber = createTextField();
        txtCheckNumber.setEditable(false);
        txtCheckNumber.setBackground(new Color(0x505050));
        formPanel.add(txtCheckNumber);

        // Касир
        formPanel.add(createLabel("КАСИР"));
        cbCashier = new JComboBox<>();
        cbCashier.setBackground(Color.WHITE);
        formPanel.add(cbCashier);

        // Картка клієнта
        formPanel.add(createLabel("КАРТКА КЛІЄНТА"));
        cbClientCard = new JComboBox<>();
        cbClientCard.setBackground(Color.WHITE);
        formPanel.add(cbClientCard);

        // Сума
        formPanel.add(createLabel("ЗАГАЛЬНА СУМА (грн)"));
        txtSumTotal = createTextField();
        formPanel.add(txtSumTotal);

        // ПДВ
        formPanel.add(createLabel("ПДВ "));
        txtVat = createTextField();
        txtVat.setEditable(false);
        txtVat.setBackground(new Color(0x505050));
        formPanel.add(txtVat);

        root.add(formPanel, BorderLayout.CENTER);

        // Кнопки
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        btnPanel.setBackground(BG_MAIN);

        btnSave = new JButton("Зберегти зміни");
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
    public JTextField getTxtCheckNumber() { return txtCheckNumber; }
    public JComboBox<ComboItem> getCbCashier() { return cbCashier; }
    public JComboBox<ComboItem> getCbClientCard() { return cbClientCard; }
    public JTextField getTxtSumTotal() { return txtSumTotal; }
    public JTextField getTxtVat() { return txtVat; }
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }
}