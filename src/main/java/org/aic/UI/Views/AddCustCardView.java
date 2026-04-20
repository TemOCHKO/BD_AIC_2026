package org.aic.UI.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddCustCardView extends JDialog {

    private static final Color BG_LIGHT = new Color(0xD9D9D9);
    private static final Color BG_WHITE = new Color(0xD9D9D9);
    private static final Color BG_FIELD = new Color(0xFFFFFF);

    private static final Color TEXT_DARK = new Color(0x1A1A1A);
    private static final Color TEXT_GRAY = new Color(0x555555);

    private static final Color BTN_DARK = new Color(0x555555);
    private static final Color BTN_LIGHT = new Color(0xE0E0E0);

    public AddCustCardView(Frame owner) {
        super(owner, "Додати карту клієнта", true);
        buildUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_LIGHT);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildForm(), BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_WHITE);

        JLabel logo = new JLabel("ZLAGODA");
        logo.setFont(new Font("Serif", Font.BOLD, 24));
        logo.setBorder(new EmptyBorder(10, 20, 10, 0));

        header.add(logo, BorderLayout.WEST);
        return header;
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_LIGHT);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        // 🔥 Заголовок по центру
        JLabel title = new JLabel("Додати карту клієнта", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(TEXT_DARK);

        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        panel.add(title, g);

        g.gridwidth = 1;

        // рядок helper
        int y = 1;

        // Прізвище
        addField(panel, g, "Прізвище", 0, y);
        addField(panel, g, "Ім’я", 1, y++);

        // По батькові
        addField(panel, g, "По батькові", 0, y++, 2);

        // Номер карти + телефон
        addField(panel, g, "Номер карти", 0, y);
        addField(panel, g, "Телефон", 1, y++);

        // Знижка
        addField(panel, g, "Знижка (%)", 0, y++, 2);

        // Кнопки
        g.gridx = 0;
        g.gridy = y;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;

        panel.add(buildButtons(), g);

        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints g, String label,
                          int x, int y) {
        addField(panel, g, label, x, y, 1);
    }

    private void addField(JPanel panel, GridBagConstraints g, String label,
                          int x, int y, int width) {

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(TEXT_GRAY);

        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tf.setBackground(BG_FIELD);
        tf.setBorder(new EmptyBorder(8, 10, 8, 10));

        wrapper.add(lbl);
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(tf);

        g.gridx = x;
        g.gridy = y;
        g.gridwidth = width;

        panel.add(wrapper, g);

        g.gridwidth = 1;
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        p.setOpaque(false);

        JButton save = button("Зберегти", BTN_LIGHT, Color.BLACK);
        JButton cancel = button("Скасувати", BTN_DARK, TEXT_GRAY);

        cancel.addActionListener(e -> dispose());

        p.add(save);
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
            AddCustCardView dialog = new AddCustCardView(null);
            dialog.setVisible(true);
        });
    }
}