package org.aic.UI.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class login extends JFrame {

    private static final Color BG_COLOR   = new Color(0xD9D9D9);
    private static final Color BTN_COLOR  = new Color(0xC0C0C0);
    private static final Color BTN_HOVER  = new Color(0xA8A8A8);
    private static final Color BTN_DARK   = new Color(0x3A3A3A);
    private static final Color BTN_DARK_H = new Color(0x555555);
    private static final Color FIELD_BG   = new Color(0xB8B8B8);
    private static final Color TEXT_COLOR = new Color(0x1A1A1A);
    private static final Color TEXT_WHITE = new Color(0xFFFFFF);
    private static final Color ERROR_CLR  = new Color(0xCC3333);

    // Поля що передаються в контролер
    private JTextField     idField;
    private JPasswordField passField;
    private JLabel         errorLabel;
    private JButton        loginButton;
    private JButton        cancelButton;

    // Яку роль обрали (для контролера)
    private String selectedRole = "";

    public login() {
        setTitle("ZLAGODA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(BG_COLOR);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBackground(BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();

        // ── Logo ─────────────────────────────────────────────────
        JLabel logo = new JLabel("ZLAGODA");
        logo.setFont(new Font("Georgia", Font.PLAIN, 52));
        logo.setForeground(TEXT_COLOR);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 60, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(logo, gbc);

        // ── Login form panel ──────────────────────────────────────
        JPanel formPanel = buildFormPanel();
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(formPanel, gbc);

        setContentPane(mainPanel);
    }

    // ── Form Panel ────────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xC8C8C8));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        panel.setPreferredSize(new Dimension(380, 300));

        // ── Role selector ─────────────────────────────────────────
        JPanel rolePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        rolePanel.setOpaque(false);
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JToggleButton managerToggle = createToggle("Менеджер");
        JToggleButton cashierToggle = createToggle("Касир");

        ButtonGroup group = new ButtonGroup();
        group.add(managerToggle);
        group.add(cashierToggle);

        managerToggle.addActionListener(e -> selectedRole = "Manager");
        cashierToggle.addActionListener(e -> selectedRole = "Cashier");

        // за замовчуванням — менеджер
        managerToggle.setSelected(true);
        selectedRole = "Manager";

        rolePanel.add(managerToggle);
        rolePanel.add(cashierToggle);

        // ── ID field ──────────────────────────────────────────────
        JLabel idLabel = makeLabel("ID працівника");
        idField = new JTextField();
        styleTextField(idField);

        // ── Password field ────────────────────────────────────────
        JLabel passLabel = makeLabel("Пароль");
        passField = new JPasswordField();
        styleTextField(passField);
        passField.addActionListener(e -> doLogin()); // Enter → логін

        // ── Error label ───────────────────────────────────────────
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errorLabel.setForeground(ERROR_CLR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Buttons ───────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        loginButton  = createRoundedButton("Увійти",     BTN_DARK,  TEXT_WHITE, BTN_DARK_H);
        cancelButton = createRoundedButton("Скасувати",  BTN_COLOR, TEXT_COLOR, BTN_HOVER);

        loginButton.addActionListener(e -> doLogin());
        cancelButton.addActionListener(e -> System.exit(0));

        btnPanel.add(loginButton);
        btnPanel.add(cancelButton);

        // ── Assemble ──────────────────────────────────────────────
        panel.add(rolePanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(idLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(idField);
        panel.add(Box.createVerticalStrut(14));
        panel.add(passLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(passField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(errorLabel);
        panel.add(Box.createVerticalStrut(16));
        panel.add(btnPanel);

        return panel;
    }

    // ── Login logic (контролер перевизначить через setLoginAction) ─
    private Runnable loginAction;

    public void setLoginAction(Runnable action) {
        this.loginAction = action;
    }

    private void doLogin() {
        if (idField.getText().trim().isEmpty()) {
            showError("Введіть ID працівника");
            return;
        }
        if (passField.getPassword().length == 0) {
            showError("Введіть пароль");
            return;
        }
        errorLabel.setText(" "); // скинути помилку
        if (loginAction != null) loginAction.run();
    }

    public void showError(String message) {
        errorLabel.setText(message);
    }

    // ── Getters for Controller ────────────────────────────────────
    public String getEnteredId()       { return idField.getText().trim(); }
    public String getEnteredPassword() { return new String(passField.getPassword()); }
    public String getSelectedRole()    { return selectedRole; }

    public void clearFields() {
        idField.setText("");
        passField.setText("");
        errorLabel.setText(" ");
    }

    // ── Helpers ───────────────────────────────────────────────────
    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Courier New", Font.PLAIN, 12));
        l.setForeground(TEXT_COLOR);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(6, 12, 6, 12));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JToggleButton createToggle(String text) {
        JToggleButton btn = new JToggleButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isSelected() ? BTN_DARK : BTN_COLOR);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(TEXT_COLOR);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addChangeListener(e ->
                btn.setForeground(btn.isSelected() ? TEXT_WHITE : TEXT_COLOR));
        return btn;
    }

    private JButton createRoundedButton(String text, Color bg, Color fg, Color hover) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new login().setVisible(true));
    }
}