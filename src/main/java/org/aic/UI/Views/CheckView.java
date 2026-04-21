package org.aic.UI.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CheckView extends JDialog {

    private static final Color BG_DARK    = new Color(0x2B2B2B);
    private static final Color BG_LIGHT   = new Color(0xD9D9D9);
    private static final Color BG_WHITE   = new Color(0xD9D9D9);
    private static final Color BG_FILTER  = new Color(0xFFFFFF);
    private static final Color TEXT_DARK  = new Color(0x1A1A1A);
    private static final Color TEXT_LIGHT = new Color(0xE0E0E0);
    private static final Color TEXT_GRAY  = new Color(0x555555);
    private static final Color BORDER_CLR = new Color(0x1A1A1A);
    private static final Color BTN_LIGHT  = new Color(0xE0E0E0);
    private static final Color BTN_ACCENT = new Color(0x555555);

    // ── Поля ──────────────────────────────────────────────────────
    private JTextField tfCardNumber, tfUPC;
    private JLabel     lblVAT, lblDiscount, lblTotal;
    private JButton    issueButton, cancelButton, addProductButton;

    private DefaultTableModel tableModel;
    private JTable            table;

    public CheckView(Frame owner) {
        super(owner, "Новий чек", true);
        buildUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_LIGHT);
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildBody(),   BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR));
        JLabel logo = new JLabel("ZLAGODA");
        logo.setFont(new Font("Serif", Font.BOLD, 24));
        logo.setForeground(TEXT_DARK);
        logo.setBorder(new EmptyBorder(10, 20, 10, 0));
        header.add(logo, BorderLayout.WEST);
        return header;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG_LIGHT);
        body.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Новий чек");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(TEXT_DARK);
        body.add(title);
        body.add(Box.createVerticalStrut(15));

        // ── Карта клієнта + UPC ───────────────────────────────────
        JPanel topFields = new JPanel(new GridLayout(1, 2, 10, 0));
        topFields.setOpaque(false);
        tfCardNumber = styledField();
        tfUPC        = styledField();
        topFields.add(labeled("Карта клієнта", tfCardNumber));
        topFields.add(labeled("UPC товару",    tfUPC));
        body.add(topFields);
        body.add(Box.createVerticalStrut(8));

        // ── Кнопка додати товар ───────────────────────────────────
        addProductButton = button("+ Додати товар", BTN_ACCENT, TEXT_LIGHT);
        addProductButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(addProductButton);
        body.add(Box.createVerticalStrut(10));

        body.add(buildTable());
        body.add(Box.createVerticalStrut(20));
        body.add(buildSummary());
        body.add(Box.createVerticalStrut(20));
        body.add(buildButtons());

        return body;
    }

    private JPanel labeled(String text, JTextField tf) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_GRAY);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        p.add(lbl);
        p.add(Box.createVerticalStrut(5));
        p.add(tf);
        return p;
    }

    private JComponent buildTable() {
        String[] columns = {"UPC", "Назва", "Кількість", "Ціна", "Сума"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setBackground(BG_FILTER);
        table.setForeground(TEXT_DARK);
        table.setGridColor(BORDER_CLR);

        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_DARK);
        header.setForeground(TEXT_LIGHT);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(right);
        table.getColumnModel().getColumn(3).setCellRenderer(right);
        table.getColumnModel().getColumn(4).setCellRenderer(right);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 180));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JPanel buildSummary() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        lblVAT      = summaryLabel("ПДВ: —");
        lblDiscount = summaryLabel("Знижка: —");
        lblTotal    = summaryLabel("Загальна сума: —");
        p.add(lblVAT);
        p.add(lblDiscount);
        p.add(lblTotal);
        return p;
    }

    private JLabel summaryLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l.setForeground(TEXT_DARK);
        return l;
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        p.setOpaque(false);
        issueButton  = button("Видати",     BTN_ACCENT, TEXT_LIGHT);
        cancelButton = button("Скасувати",  BTN_LIGHT,  TEXT_DARK);
        cancelButton.addActionListener(e -> dispose());
        p.add(issueButton);
        p.add(cancelButton);
        return p;
    }

    private JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setBackground(BG_FILTER);
        tf.setForeground(TEXT_DARK);
        tf.setBorder(new EmptyBorder(8, 12, 8, 12));
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return tf;
    }

    private JButton button(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }

    // ── Геттери ───────────────────────────────────────────────────
    public String getCardNumber()   { return tfCardNumber.getText().trim(); }
    public String getUPC()          { return tfUPC.getText().trim(); }
    public JTable getTable()        { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JButton getIssueButton()      { return issueButton; }
    public JButton getCancelButton()     { return cancelButton; }
    public JButton getAddProductButton() { return addProductButton; }

    // ── Сеттери для підсумку ──────────────────────────────────────
    public void setVAT(double vat)         { lblVAT.setText(String.format("ПДВ: %.2f грн", vat)); }
    public void setDiscount(double pct)    { lblDiscount.setText(String.format("Знижка: %.0f%%", pct)); }
    public void setTotal(double total)     { lblTotal.setText(String.format("Загальна сума: %.2f грн", total)); }

    // ── Додати рядок у таблицю ────────────────────────────────────
    public void addRow(String upc, String name, int qty, double price) {
        tableModel.addRow(new Object[]{upc, name, qty, price, qty * price});
    }

    public void clearTable() { tableModel.setRowCount(0); }
}