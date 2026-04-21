package org.aic.UI.Views;

import org.aic.DBModels.CheckDBModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CheckDialog extends JDialog {
    private JTextField txtCheckNumber;
    private JTextField txtCardNumber;
    private JTextField txtSum;

    private JButton btnSave;
    private JButton btnCancel;
    private CheckDBModel currentCheck;

    private final Color BG_MAIN = new Color(0xCCCCCC);
    private final Color FIELD_BG = new Color(0x404040);
    private final Color FIELD_FG = Color.WHITE;

    public CheckDialog(JFrame parent, String title) {
        super(parent, title, true);
        setSize(400, 350);
        setLocationRelativeTo(parent);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_MAIN);
        root.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblHeader = new JLabel(title);
        lblHeader.setFont(new Font("Georgia", Font.PLAIN, 20));
        lblHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        root.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 1, 5, 2));
        formPanel.setBackground(BG_MAIN);

        formPanel.add(createLabel("НОМЕР ЧЕКА"));
        txtCheckNumber = createTextField();
        txtCheckNumber.setEditable(false); // Забороняємо редагувати номер
        txtCheckNumber.setFocusable(false);
        formPanel.add(txtCheckNumber);

        formPanel.add(createLabel("КАРТКА КЛІЄНТА"));
        txtCardNumber = createTextField();
        formPanel.add(txtCardNumber);

        formPanel.add(createLabel("СУМА"));
        txtSum = createTextField();
        txtSum.setEditable(false);
        txtSum.setFocusable(false);
        formPanel.add(txtSum);

        root.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        btnPanel.setBackground(BG_MAIN);

        btnSave = new JButton("Зберегти");
        btnSave.setBackground(new Color(0x606060));
        btnSave.setForeground(Color.WHITE);

        btnCancel = new JButton("Скасувати");

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        root.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
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

    public void setData(CheckDBModel check) {
        this.currentCheck = check;
        txtCheckNumber.setText(check.getCheck_number());
        // Якщо картки немає (null), показуємо порожній рядок
        txtCardNumber.setText(check.getCard_number() != null ? check.getCard_number() : "");
        txtSum.setText(String.valueOf(check.getSum_total()));
    }

    public String getCardNumber() { return txtCardNumber.getText().trim(); }
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }
}