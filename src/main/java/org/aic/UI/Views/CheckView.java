package org.aic.UI.Views;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CheckView extends JDialog {

    private static final Color BG_DARK    = new Color(0x2B2B2B);
    private static final Color BG_LIGHT   = new Color(0xD9D9D9);
    private static final Color BG_WHITE   = new Color(0xD9D9D9);
    private static final Color BG_FILTER  = new Color(0xFFFFFF);
    private static final Color TAB_ACTIVE = new Color(0xFFFFFF);
    private static final Color TAB_IDLE   = new Color(0xC8C8C8);
    private static final Color TEXT_DARK  = new Color(0x1A1A1A);
    private static final Color TEXT_LIGHT = new Color(0xE0E0E0);
    private static final Color TEXT_GRAY  = new Color(0x555555);
    private static final Color BORDER_CLR = new Color(0x1A1A1A);
    private static final Color BTN_DARK   = new Color(0x3A3A3A);
    private static final Color BTN_LIGHT  = new Color(0xE0E0E0);
    private static final Color BTN_ACCENT = new Color(0x555555);
    private static final Color BTN_GREEN  = new Color(0x3A6B3A);

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
        root.add(buildBody(), BorderLayout.CENTER);

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

        body.add(twoFields("Карта клієнта", "UPC товару"));
        body.add(Box.createVerticalStrut(15));

        body.add(buildTable());
        body.add(Box.createVerticalStrut(20));

        body.add(buildSummary());
        body.add(Box.createVerticalStrut(20));

        body.add(buildButtons());

        return body;
    }

    private JPanel twoFields(String l1, String l2) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setOpaque(false);

        panel.add(labeledField(l1));
        panel.add(labeledField(l2));

        return panel;
    }

    private JPanel labeledField(String label) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_GRAY);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JTextField tf = new JTextField();
        tf.setBackground(BG_FILTER);
        tf.setForeground(TEXT_LIGHT);
        tf.setBorder(new EmptyBorder(8, 12, 8, 12));

        p.add(lbl);
        p.add(Box.createVerticalStrut(5));
        p.add(tf);

        return p;
    }

    // 🧾 TABLE METHOD
    private JComponent buildTable() {
        String[] columns = {"ID товару", "Назва", "Кількість", "Ціна", "Сума"};
        Object[][] data = new Object[6][5];

        JTable table = new JTable(data, columns);

        table.setRowHeight(28);
        table.setBackground(BG_FILTER);
        table.setForeground(TEXT_LIGHT);
        table.setGridColor(BORDER_CLR);

        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_DARK);
        header.setForeground(TEXT_LIGHT);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        // вирівнювання чисел вправо
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

        p.add(label("ПДВ: 24.00"));
        p.add(label("Знижка: 5%"));
        p.add(label("Загальна сума: 120.56 грн"));

        return p;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l.setForeground(TEXT_DARK);
        return l;
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        p.setOpaque(false);

        JButton ok = button("Видати", BTN_ACCENT, TEXT_LIGHT);
        JButton cancel = button("Скасувати", BTN_LIGHT, TEXT_DARK);

        cancel.addActionListener(e -> dispose());

        p.add(ok);
        p.add(cancel);

        return p;
    }

    private JButton button(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}

            CheckView dialog = new CheckView(null); // Передаємо null
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            System.exit(0);
        });
    }
}