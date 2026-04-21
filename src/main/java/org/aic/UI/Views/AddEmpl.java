package org.aic.UI.Views;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;

public class AddEmpl extends JDialog {

    // ── Colors ───────────────────────────────────────────────────────────────
    private static final Color BG_HEADER     = new Color(0xD9D9D9);
    private static final Color HEADER_BORDER = new Color(0xD0, 0xD0, 0xD0);
    private static final Color BG_BODY       = new Color(0x979797);
    private static final Color BG_CARD       = new Color(0xD9D9D9);
    private static final Color BG_FIELD      = new Color(0x3A, 0x3A, 0x3A);
    private static final Color BG_FIELD_FOC  = new Color(0x4A, 0x4A, 0x4A);
    private static final Color FG_TITLE      = new Color(0x000000);
    private static final Color FG_LABEL      = new Color(0, 0, 0);
    private static final Color FG_FIELD      = new Color(0xFFFFFF);
    private static final Color FG_DIVIDER    = new Color(0x444444);
    private static final Color BTN_SAVE_BG   = new Color(0x5A, 0x5A, 0x5A);
    private static final Color BTN_SAVE_FG   = new Color(0xFFFFFF);
    private static final Color BTN_CANCEL_FG = new Color(0x3A, 0x3A, 0x3A);
    private static final Color BTN_CANCEL_BD = new Color(0xCCCCCC);

    // ── Fonts ────────────────────────────────────────────────────────────────
    private static final Font FONT_TITLE = new Font("Serif",     Font.PLAIN, 20);
    private static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 10);
    private static final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BTN   = new Font("SansSerif", Font.PLAIN, 13);

    // ── UI Fields ────────────────────────────────────────────────────────────
    JLabel title;
    private JButton           save;
    private JButton           cancel;
    private JTextField        tfSurname, tfName, tfPatronymic;
    private JComboBox<String> cbRole;
    private JTextField        tfSalary;
    private JPasswordField    tfPassword;        // ← новий пароль
    private JPasswordField    tfPasswordConfirm; // ← підтвердження пароля
    private DatePicker        spBirth, spStart;
    private JTextField        tfPhone, tfCity, tfStreet, tfZip;

    // Режим: true = додавання (показуємо поля пароля),
    //        false = редагування (поля пароля приховані)
    private final boolean isAddMode;

    public AddEmpl(Frame owner) {
        this(owner, true);
    }

    public AddEmpl(Frame owner, boolean isAddMode) {
        super(owner, isAddMode ? "Додати нового працівника" : "Редагувати працівника", true);
        this.isAddMode = isAddMode;
        buildUI();
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    // ── Build UI ─────────────────────────────────────────────────────────────
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_BODY);
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildBody(),   BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setBackground(BG_HEADER);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, HEADER_BORDER));
        JLabel logo = new JLabel("ZLAGODA");
        logo.setFont(new Font("Serif", Font.BOLD, 24));
        header.add(logo);
        return header;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(BG_BODY);
        body.setBorder(new EmptyBorder(28, 24, 28, 24));
        body.add(buildCard());
        return body;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new EmptyBorder(10, 10, 22, 10));
        card.setPreferredSize(new Dimension(500, isAddMode ? 700 : 620));

        title = new JLabel(isAddMode ? "Додати нового працівника" : "Редагувати працівника");
        title.setFont(FONT_TITLE);
        title.setForeground(FG_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(vGap(20));

        card.add(twoCol(
                labeled("ПРІЗВИЩЕ", tfSurname = darkField()),
                labeled("ІМ'Я",     tfName    = darkField())
        ));
        card.add(vGap(14));

        card.add(labeled("ПО БАТЬКОВІ", tfPatronymic = darkField()));
        card.add(vGap(14));

        cbRole = new JComboBox<>(new String[]{"Manager", "Cashier"});
        styleCombo(cbRole);
        card.add(twoCol(
                labeled("ПОСАДА",   cbRole),
                labeled("ЗАРПЛАТА", tfSalary = darkField())
        ));
        card.add(vGap(14));

        spBirth = new DatePicker();
        spStart = new DatePicker();
        spBirth.setDateToToday();
        spStart.setDateToToday();

        card.add(twoCol(
                labeled("ДАТА НАРОДЖЕННЯ", spBirth),
                labeled("ДАТА ПОЧАТКУ",    spStart)
        ));
        card.add(vGap(18));

        card.add(divider());
        card.add(vGap(18));

        card.add(labeled("ТЕЛЕФОН", tfPhone = darkField()));
        card.add(vGap(14));

        card.add(twoCol(
                labeled("МІСТО",  tfCity = darkField()),
                labeled("ІНДЕКС", tfZip  = darkField())
        ));
        card.add(vGap(14));

        card.add(labeled("ВУЛИЦЯ", tfStreet = darkField()));
        card.add(vGap(18));

        // ── Поля пароля — тільки при додаванні ───────────────────
        if (isAddMode) {
            card.add(divider());
            card.add(vGap(18));

            tfPassword        = darkPasswordField();
            tfPasswordConfirm = darkPasswordField();

            card.add(twoCol(
                    labeled("ПАРОЛЬ",              tfPassword),
                    labeled("ПІДТВЕРДЖЕННЯ ПАРОЛЯ", tfPasswordConfirm)
            ));
            card.add(vGap(18));
        }

        card.add(buildButtons());
        return card;
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        save   = pillButton(isAddMode ? "Зберегти" : "Оновити", BTN_SAVE_BG, BTN_SAVE_FG, true);
        cancel = pillButton("Скасувати", BG_CARD, BTN_CANCEL_FG, false);
        cancel.addActionListener(e -> dispose());

        p.add(save);
        p.add(cancel);
        return p;
    }

    // ── Валідація пароля (викликається з контролера) ──────────────
    public boolean validatePassword() {
        if (!isAddMode) return true; // при редагуванні пароль не міняємо

        String pass    = new String(tfPassword.getPassword()).trim();
        String confirm = new String(tfPasswordConfirm.getPassword()).trim();

        if (pass.isEmpty()) {
            showError("Введіть пароль");
            return false;
        }
        if (pass.length() < 4) {
            showError("Пароль має бути не менше 4 символів");
            return false;
        }
        if (!pass.equals(confirm)) {
            showError("Паролі не співпадають");
            return false;
        }
        return true;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Помилка", JOptionPane.ERROR_MESSAGE);
    }

    // ── UI helpers ────────────────────────────────────────────────────────────
    private JPanel labeled(String text, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(FG_LABEL);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(field);
        return p;
    }

    private JPanel twoCol(JPanel left, JPanel right) {
        JPanel p = new JPanel(new GridLayout(1, 2, 12, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        p.add(left);
        p.add(right);
        return p;
    }

    private JTextField darkField() {
        JTextField tf = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hasFocus() ? BG_FIELD_FOC : BG_FIELD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(FONT_FIELD);
        tf.setForeground(FG_FIELD);
        tf.setCaretColor(FG_FIELD);
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(8, 14, 8, 14));
        tf.setBackground(BG_FIELD);
        tf.setPreferredSize(new Dimension(200, 40));
        return tf;
    }

    private JPasswordField darkPasswordField() {
        JPasswordField pf = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hasFocus() ? BG_FIELD_FOC : BG_FIELD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pf.setFont(FONT_FIELD);
        pf.setForeground(FG_FIELD);
        pf.setCaretColor(FG_FIELD);
        pf.setOpaque(false);
        pf.setBorder(new EmptyBorder(8, 14, 8, 14));
        pf.setBackground(BG_FIELD);
        pf.setEchoChar('●');
        pf.setPreferredSize(new Dimension(200, 40));
        return pf;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(FONT_FIELD);
        cb.setBackground(BG_FIELD);
        cb.setForeground(FG_FIELD);
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value,
                                                                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? BG_FIELD_FOC : BG_FIELD);
                setForeground(FG_FIELD);
                setBorder(new EmptyBorder(4, 12, 4, 12));
                return this;
            }
        });
        cb.setPreferredSize(new Dimension(200, 40));
        cb.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
    }

    private JButton pillButton(String text, Color bg, Color fg, boolean filled) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed()  ? bg.darker()   :
                        getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                if (!filled) {
                    g2.setColor(BTN_CANCEL_BD);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, getHeight(), getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 26, 10, 26));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JComponent divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(FG_DIVIDER);
        sep.setBackground(FG_DIVIDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }

    private Component vGap(int h) { return Box.createVerticalStrut(h); }

    // ── Getters ──────────────────────────────────────────────────────────────
    public JButton            getSaveButton()      { return save; }
    public JButton            getCancelButton()    { return cancel; }
    public JTextField         getSurnameField()    { return tfSurname; }
    public JTextField         getNameField()       { return tfName; }
    public JTextField         getPatronymicField() { return tfPatronymic; }
    public JComboBox<String>  getRoleField()       { return cbRole; }
    public JTextField         getSalaryField()     { return tfSalary; }
    public JTextField         getPhoneField()      { return tfPhone; }
    public JTextField         getCityField()       { return tfCity; }
    public JTextField         getStreetField()     { return tfStreet; }
    public JTextField         getZipField()        { return tfZip; }
    public DatePicker         getDobField()        { return spBirth; }
    public DatePicker         getDosField()        { return spStart; }
    public JLabel             getTitleLabel()      { return title; }

    // Пароль у вигляді plain text — одразу після отримання хешуємо в контролері!
    public String getPassword() {
        if (tfPassword == null) return null;
        return new String(tfPassword.getPassword()).trim();
    }

    public void setEmployeeData(String surname, String name, String patronymic,
                                String role, String salary, LocalDate dob, LocalDate dos,
                                String phone, String city, String street, String zip) {
        tfSurname.setText(surname);
        tfName.setText(name);
        tfPatronymic.setText(patronymic);
        cbRole.setSelectedItem(role);
        tfSalary.setText(salary);
        if (dob != null) spBirth.setDate(dob);
        if (dos != null) spStart.setDate(dos);
        tfPhone.setText(phone);
        tfCity.setText(city);
        tfStreet.setText(street);
        tfZip.setText(zip);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            AddEmpl dialog = new AddEmpl(null, true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            System.exit(0);
        });
    }
}