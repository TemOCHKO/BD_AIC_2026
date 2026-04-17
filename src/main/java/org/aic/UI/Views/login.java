package org.aic.UI.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class login extends JFrame {

    private static final Color BG_COLOR = new Color(0xD9D9D9);
    private static final Color BTN_COLOR = new Color(0xC0C0C0);
    private static final Color BTN_HOVER = new Color(0xA8A8A8);
    private static final Color TEXT_COLOR = new Color(0x1A1A1A);

    public login() {
        setTitle("ZLAGODA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setUndecorated(false);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(BG_COLOR);
            }
        };
        mainPanel.setBackground(BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();

        // ─── Logo label
        JLabel logo = new JLabel("ZLAGODA");
        logo.setFont(new Font("Georgia", Font.PLAIN, 52));
        logo.setForeground(TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 80, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(logo, gbc);

        // ─── Менеджер button
        JButton managerBtn = createRoundedButton("Менеджер");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 60);
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(managerBtn, gbc);

        managerBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Вхід як Менеджер", "ZLAGODA",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // ─── Працівник button
        JButton workerBtn = createRoundedButton("Працівник");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 60, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(workerBtn, gbc);

        workerBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Вхід як Працівник", "ZLAGODA",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        setContentPane(mainPanel);
    }

    private JButton createRoundedButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(BTN_HOVER);
                } else {
                    g2.setColor(BTN_COLOR);
                }
                g2.fill(new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 40, 40));
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {

            }
        };

        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btn.setForeground(TEXT_COLOR);
        btn.setPreferredSize(new Dimension(220, 52));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.repaint();
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new login().setVisible(true);
        });
    }
}