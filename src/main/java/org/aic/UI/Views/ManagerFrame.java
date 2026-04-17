package org.aic.UI.Views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class ManagerFrame extends JFrame {

    // ── Colors ────────────────────────────────────────────────────
    private static final Color BG_DARK    = new Color(0x2B2B2B);
    private static final Color BG_LIGHT   = new Color(0xD9D9D9);
    private static final Color BG_WHITE   = new Color(0xFFFFFF);
    private static final Color TAB_ACTIVE = new Color(0xFFFFFF);
    private static final Color TAB_IDLE   = new Color(0xC8C8C8);
    private static final Color TEXT_DARK  = new Color(0x1A1A1A);
    private static final Color TEXT_GRAY  = new Color(0x555555);
    private static final Color BORDER_CLR = new Color(0x1A1A1A);
    private static final Color BTN_ADD    = new Color(0x3A3A3A);
    private static final Color BTN_LIGHT  = new Color(0xE0E0E0);

    // ── Tab config ────────────────────────────────────────────────
    private static final String[] TABS = {"Працівники", "Товари", "Магазин", "Чеки", "Клієнти"};

    private static final String[][] COLUMNS = {
            {"ID працівника", "Прізвище", "Ім'я", "Посада", "Зарплата", "Телефон"},
            {"ID товару", "Назва", "Виробник", "Характеристики", "Категорія"},
            {"UPC", "Назва", "Ціна продажу", "К-сть", "Акційний"},
            {"Номер чека", "Дата", "Касир", "Сума", "ПДВ"},
            {"Номер карти", "Прізвище", "Ім'я", "Телефон", "Знижка %"}
    };

    // ── State ─────────────────────────────────────────────────────
    private int activeTab = 0;

    // ── UI components exposed for Controller ──────────────────────
    private JPanel tabBar;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public ManagerFrame() {
        setTitle("ZLAGODA — Менеджер");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);

        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);

        setContentPane(root);
    }

    // ── Header
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BG_LIGHT);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_CLR),
                BorderFactory.createEmptyBorder(0, 20, 0, 20)
        ));

        JLabel logo = new JLabel("ZLAGODA");
        logo.setFont(new Font("Georgia", Font.PLAIN, 30));
        logo.setForeground(TEXT_DARK);
        header.add(logo, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xC5C5C5));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.dispose();
            }
        };
        searchPanel.setOpaque(false);
        searchPanel.setPreferredSize(new Dimension(280, 38));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 10));

        searchField = new JTextField();
        searchField.setOpaque(false);
        searchField.setBorder(null);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(TEXT_DARK);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JLabel searchIcon = new JLabel("⌕");
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        searchIcon.setForeground(TEXT_GRAY);
        searchPanel.add(searchIcon, BorderLayout.EAST);

        JPanel searchWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        searchWrapper.setOpaque(false);
        searchWrapper.add(searchPanel);
        header.add(searchWrapper, BorderLayout.EAST);

        return header;
    }

    // ── Content ──────────────────────────────────────────────────
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 0));
        content.setBackground(BG_DARK);
        content.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));

        tabBar = buildTabBar();
        content.add(tabBar,            BorderLayout.NORTH);
        content.add(buildTablePanel(), BorderLayout.CENTER);
        content.add(buildButtonPanel(),BorderLayout.SOUTH);

        return content;
    }

    // ── Tab Bar ──────────────────────────────────────────────────
    private JPanel buildTabBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        bar.setOpaque(false);

        for (int i = 0; i < TABS.length; i++) {
            final int idx = i;
            JButton btn = new JButton(TABS[i]) {
                boolean hover = false;
                {
                    addMouseListener(new MouseAdapter() {
                        public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                        public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                    });
                }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean active = (activeTab == idx);
                    Color fill = active ? TAB_ACTIVE : (hover ? new Color(0xD8D8D8) : TAB_IDLE);
                    g2.setColor(fill);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                    g2.dispose();
                    super.paintComponent(g);
                }
                @Override protected void paintBorder(Graphics g) {}
            };
            btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
            btn.setForeground(TEXT_DARK);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(140, 38));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                activeTab = idx;
                bar.repaint();
                onTabChanged();
            });
            bar.add(btn);
        }
        return bar;
    }

    // ── Table Panel ──────────────────────────────────────────────
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BG_WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));

        tableModel = new DefaultTableModel(COLUMNS[activeTab], 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_WHITE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ── Button Panel ─────────────────────────────────────────────
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setOpaque(false);

        refreshButton = makeButton("↻  Оновити",    BTN_LIGHT, TEXT_DARK);
        deleteButton  = makeButton("✕  Видалити",   BTN_LIGHT, TEXT_DARK);
        editButton    = makeButton("✎  Редагувати", BTN_LIGHT, TEXT_DARK);
        addButton     = makeButton("+  Додати",      BTN_ADD,   Color.WHITE);

        panel.add(refreshButton);
        panel.add(deleteButton);
        panel.add(editButton);
        panel.add(addButton);

        return panel;
    }

    private JButton makeButton(String text, Color bg, Color fg) {
        JButton btn = new RoundedButton(text, 40);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setPreferredSize(new Dimension(150, 38));
        btn.setMinimumSize(new Dimension(150, 38));
        btn.setMaximumSize(new Dimension(150, 38));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Table styling ────────────────────────────────────────────
    private void styleTable() {
        table.setRowHeight(48);
        table.setShowGrid(true);
        table.setGridColor(BORDER_CLR);
        table.setBackground(BG_WHITE);
        table.setForeground(TEXT_DARK);
        table.setSelectionBackground(new Color(0xE8E8E8));
        table.setSelectionForeground(TEXT_DARK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setFocusable(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_WHITE);
        header.setForeground(TEXT_DARK);
        header.setFont(new Font("Courier New", Font.PLAIN, 14));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_CLR));
        header.setPreferredSize(new Dimension(0, 44));
        header.setReorderingAllowed(false);

        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 4));
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    // ── Internal tab change (Controller перевизначить через listener) ──
    private void onTabChanged() {
        tableModel.setColumnIdentifiers(COLUMNS[activeTab]);
        tableModel.setRowCount(0);
        tabBar.repaint();
    }

    // ── Getters for Controller ────────────────────────────────────
    public JTable            getTable()         { return table; }
    public DefaultTableModel getTableModel()    { return tableModel; }
    public JTextField        getSearchField()   { return searchField; }
    public JButton           getAddButton()     { return addButton; }
    public JButton           getEditButton()    { return editButton; }
    public JButton           getDeleteButton()  { return deleteButton; }
    public JButton           getRefreshButton() { return refreshButton; }
    public int               getActiveTab()     { return activeTab; }

    public Object getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        return tableModel.getValueAt(row, 0);
    }

    // Контролер викликає це щоб заповнити таблицю даними з БД
    public void setTableData(String[] columns, Object[][] rows) {
        tableModel.setColumnIdentifiers(columns);
        tableModel.setRowCount(0);
        for (Object[] row : rows) tableModel.addRow(row);
        styleTable();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new ManagerFrame().setVisible(true));
    }
}