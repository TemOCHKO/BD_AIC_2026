package org.aic.UI.Views;

import org.aic.DBModels.CustomerCardDBModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddCustCardView extends JDialog {

    private static final Color BG_LIGHT = new Color(0xD9D9D9);
    private static final Color BG_WHITE = new Color(0xD9D9D9);
    private static final Color BG_FIELD = new Color(0xFFFFFF);
    private static final Color TEXT_DARK = new Color(0x1A1A1A);
    private static final Color TEXT_GRAY = new Color(0x555555);
    private static final Color BTN_DARK  = new Color(0x555555);
    private static final Color BTN_LIGHT = new Color(0xE0E0E0);

    // ── Поля ─────────────────────────────────────────────────────
    private JTextField tfSurname, tfName, tfPatronymic;
    private JTextField tfCardNumber, tfPhone, tfDiscount;
    private JTextField tfCity, tfStreet, tfZip;
    private JButton    saveButton, cancelButton;

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
        root.add(buildForm(),   BorderLayout.CENTER);
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

        JLabel title = new JLabel("Додати карту клієнта", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(TEXT_DARK);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(title, g);
        g.gridwidth = 1;

        int y = 1;

        tfSurname = field(); tfName = field();
        addLabeled(panel, g, "Прізвище",  tfSurname,    0, y++, 2);
        addLabeled(panel, g, "Ім'я",      tfName,       0, y++, 2);

        tfPatronymic = field();
        addLabeled(panel, g, "По батькові", tfPatronymic, 0, y++, 2);

        tfCardNumber = field(); tfPhone = field();
        //addLabeled(panel, g, "Номер карти", tfCardNumber, 0, y);
        addLabeled(panel, g, "Телефон",     tfPhone,      0, y++, 2);

        tfCity = field(); tfStreet = field();
        addLabeled(panel, g , "Місто", tfCity, 0, y++, 2);
        addLabeled(panel, g, "Вулиця",     tfStreet,      0, y++, 2);

        tfZip = field(); tfZip = field();
        addLabeled(panel, g, "Zip",     tfZip,      0, y++, 2);

        tfDiscount = field();
        addLabeled(panel, g, "Знижка (%)", tfDiscount, 0, y++, 2);

        g.gridx = 0; g.gridy = y; g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        panel.add(buildButtons(), g);

        return panel;
    }

    private void addLabeled(JPanel panel, GridBagConstraints g,
                            String label, JTextField tf, int x, int y) {
        addLabeled(panel, g, label, tf, x, y, 1);
    }

    private void addLabeled(JPanel panel, GridBagConstraints g,
                            String label, JTextField tf, int x, int y, int width) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(TEXT_GRAY);

        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        wrapper.add(lbl);
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(tf);

        g.gridx = x; g.gridy = y; g.gridwidth = width;
        panel.add(wrapper, g);
        g.gridwidth = 1;
    }

    private JTextField field() {
        JTextField tf = new JTextField();
        tf.setBackground(BG_FIELD);
        tf.setBorder(new EmptyBorder(8, 10, 8, 10));
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return tf;
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        p.setOpaque(false);

        saveButton   = button("Зберегти",   BTN_LIGHT, Color.BLACK);
        cancelButton = button("Скасувати",  BTN_DARK,  Color.BLACK);

        cancelButton.addActionListener(e -> dispose());

        p.add(saveButton);
        p.add(cancelButton);
        return p;
    }
    // метод для передзаповнення при редагуванні
    public void fillFields(CustomerCardDBModel card) {
        tfCardNumber.setText(card.getCard_number());
        tfCardNumber.setEditable(false); // номер карти — незмінний ключ
        tfSurname.setText(card.getCust_surname());
        tfName.setText(card.getCust_name());
        tfCity.setText(card.getCity());
        tfStreet.setText(card.getStreet());
        tfZip.setText(card.getZip_code());
        tfPatronymic.setText(card.getCust_patronymic());
        tfPhone.setText(card.getPhone_number());
        tfDiscount.setText(String.valueOf(card.getPercent()));
    }

    // змінити заголовок діалогу
    public void setTitle(String title) {
        super.setTitle(title);
    }

    private JButton button(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }

    // ── Геттери для контролера ────────────────────────────────────
    public String getSurname()    { return tfSurname.getText().trim(); }
    public String getName()       { return tfName.getText().trim(); }
    public String getPatronymic() { return tfPatronymic.getText().trim(); }
    public String getCardNumber() { return tfCardNumber.getText().trim(); }
    public String getPhone()      { return tfPhone.getText().trim(); }
    public String getDiscount()   { return tfDiscount.getText().trim(); }
    public String getCity()      { return tfCity.getText().trim(); }
    public String getZip()      { return tfZip.getText().trim(); }
    public String getStreet()    { return tfStreet.getText().trim(); }
    public JButton getSaveButton()   { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }

    public static void main(String[] args) {
        AddCustCardView view = new AddCustCardView(null);
        view.setVisible(true);
    }
}