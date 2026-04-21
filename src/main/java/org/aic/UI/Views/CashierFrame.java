package org.aic.UI.Views;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class CashierFrame extends JFrame {

    // ── Colors ────────────────────────────────────────────────────
    private static final Color BG_DARK    = new Color(0x2B2B2B);
    private static final Color BG_LIGHT   = new Color(0xD9D9D9);
    private static final Color BG_WHITE   = new Color(0xFFFFFF);
    private static final Color BG_FILTER  = new Color(0x3A3A3A);
    private static final Color TAB_ACTIVE = new Color(0xFFFFFF);
    private static final Color TAB_IDLE   = new Color(0xC8C8C8);
    private static final Color TEXT_DARK  = new Color(0x1A1A1A);
    private static final Color TEXT_LIGHT = new Color(0xE0E0E0);
    private static final Color TEXT_GRAY  = new Color(0x555555);
    private static final Color BORDER_CLR = new Color(0x1A1A1A);
    private static final Color BTN_DARK   = new Color(0x3A3A3A);
    private static final Color BTN_LIGHT  = new Color(0xE0E0E0);
    private static final Color BTN_ACCENT = new Color(0x555555);

    // ── Tabs (Без Працівників) ────────────────────────────────────
    public static final int TAB_PRODUCTS  = 0;
    public static final int TAB_STORE     = 1;
    public static final int TAB_RECEIPTS  = 2;
    public static final int TAB_CLIENTS   = 3;

    private static final String[] TABS = {"Товари", "Магазин", "Чеки", "Клієнти"};

    private static final String[][] COLUMNS = {
            {"ID товару", "Назва", "Виробник", "Характеристики", "Категорія"},
            {"UPC", "Назва", "Ціна продажу", "К-сть", "Акційний"},
            {"Номер чека", "Дата", "ID касира", "Сума", "ПДВ", "Карта клієнта"},
            {"Номер карти", "Прізвище", "Ім'я", "По батькові", "Телефон", "Адреса", "Знижка %"}
    };

    // ── State ─────────────────────────────────────────────────────
    private int activeTab = 0;

    // ── UI ────────────────────────────────────────────────────────
    private JButton btnLogout;
    private JPanel   tabBar;
    private JTable   table;
    private DefaultTableModel tableModel;
    private JPanel   filterPanel;
    private JPanel   contentCenter;
    private JTextField searchField;
    private JButton[] tabButtons;

    // Filters
    private JComboBox<String> cbProductCategory;
    private JButton           btnFilterByCategory;
    private JComboBox<String> cbStoreSort;
    private JComboBox<String> cbStoreFilter;
    private JTextField        txtUpc;
    private JButton           btnFindByUpc;
    private JComboBox<String> cbCashier;
    private DatePicker        dpDateFrom;
    private DatePicker        dpDateTo;
    private JButton           btnFilterReceipts;
    private JTextField        txtDiscountFilter;
    private JButton           btnFilterByDiscount;

    // Bottom buttons (БЕЗ DELETE)
    private JButton addButton;
    private JButton editButton;
    private JButton printButton;

    public CashierFrame() {
        setTitle("ZLAGODA — Касир");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BG_LIGHT); g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,2,0, BORDER_CLR),
                BorderFactory.createEmptyBorder(0,20,0,20)
        ));

        JLabel logo = new JLabel("ZLAGODA (Касир)");
        logo.setFont(new Font("Georgia", Font.PLAIN, 28));
        logo.setForeground(TEXT_DARK);
        header.add(logo, BorderLayout.WEST);

        JPanel searchWrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xC5C5C5));
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),24,24));
                g2.dispose();
            }
        };
        searchWrap.setOpaque(false);
        searchWrap.setPreferredSize(new Dimension(300, 38));
        searchWrap.setBorder(BorderFactory.createEmptyBorder(4,14,4,10));

        btnLogout = new JButton("🚪 Вийти");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogout.setForeground(new Color(0xCC3333)); // Темно-червоний колір
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchField = new JTextField();
        searchField.setOpaque(false);
        searchField.setBorder(null);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(TEXT_DARK);
        searchWrap.add(btnLogout, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        right.setOpaque(false);
        right.add(searchWrap);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG_DARK);
        content.setBorder(BorderFactory.createEmptyBorder(6,12,12,12));

        tabBar = buildTabBar();
        content.add(tabBar, BorderLayout.NORTH);

        contentCenter = new JPanel(new BorderLayout());
        contentCenter.setBackground(BG_DARK);

        filterPanel = buildFilterPanel(activeTab);
        contentCenter.add(filterPanel, BorderLayout.NORTH);
        contentCenter.add(buildTablePanel(), BorderLayout.CENTER);

        content.add(contentCenter, BorderLayout.CENTER);
        content.add(buildButtonPanel(), BorderLayout.SOUTH);
        return content;
    }

    private JPanel buildTabBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        bar.setOpaque(false);
        tabButtons = new JButton[TABS.length];

        for (int i = 0; i < TABS.length; i++) {
            final int idx = i;
            JButton btn = new JButton(TABS[i]) {
                boolean hover = false;
                { addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e){hover=true; repaint();}
                    public void mouseExited(MouseEvent e) {hover=false;repaint();}
                }); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean active=(activeTab==idx);
                    g2.setColor(active?TAB_ACTIVE:(hover?new Color(0xD8D8D8):TAB_IDLE));
                    g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),20,20));
                    g2.dispose();
                    super.paintComponent(g);
                }
                @Override protected void paintBorder(Graphics g){}
            };
            btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
            btn.setForeground(TEXT_DARK);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(140, 38));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            tabButtons[i] = btn;
            bar.add(btn);
        }
        return bar;
    }

    public void updateViewForTab(int idx) {
        activeTab = idx;
        tabBar.repaint();
        contentCenter.remove(filterPanel);
        filterPanel = buildFilterPanel(idx);
        contentCenter.add(filterPanel, BorderLayout.NORTH);
        contentCenter.revalidate();
        contentCenter.repaint();
    }

    public JPanel buildFilterPanel(int tab) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(BG_FILTER);
        panel.setBorder(BorderFactory.createEmptyBorder(0,4,0,4));

        switch (tab) {
            case TAB_PRODUCTS -> {
                cbProductCategory = new JComboBox<>();
                styleCombo(cbProductCategory, 180);
                cbProductCategory.addItem("Всі категорії");
                btnFilterByCategory = makeFilterButton("Фільтр");
                panel.add(filterLabel("Категорія:"));
                panel.add(cbProductCategory);
                panel.add(btnFilterByCategory);
            }
            case TAB_STORE -> {
                cbStoreFilter = new JComboBox<>(new String[]{"Всі товари","Акційні","Не акційні"});
                styleCombo(cbStoreFilter, 150);
                cbStoreSort = new JComboBox<>(new String[]{"За назвою","За к-стю"});
                styleCombo(cbStoreSort, 130);
                txtUpc = makeFilterTextField("", 120);
                btnFindByUpc = makeFilterButton("Знайти за UPC");
                panel.add(filterLabel("Фільтр:"));
                panel.add(cbStoreFilter);
                panel.add(filterLabel("Сорт.:"));
                panel.add(cbStoreSort);
                panel.add(Box.createHorizontalStrut(16));
                panel.add(filterLabel("UPC:"));
                panel.add(txtUpc);
                panel.add(btnFindByUpc);
            }
            case TAB_RECEIPTS -> {
                cbCashier = new JComboBox<>();
                cbCashier.addItem("Всі касири");
                styleCombo(cbCashier, 160);

                DatePickerSettings settingsFrom = new DatePickerSettings();
                settingsFrom.setFormatForDatesCommonEra("yyyy-MM-dd");
                dpDateFrom = new DatePicker(settingsFrom);

                DatePickerSettings settingsTo = new DatePickerSettings();
                settingsTo.setFormatForDatesCommonEra("yyyy-MM-dd");
                dpDateTo = new DatePicker(settingsTo);

                btnFilterReceipts  = makeFilterButton("Показати чеки");

                panel.add(filterLabel("Касир:"));
                panel.add(cbCashier);
                panel.add(filterLabel("Період:"));
                panel.add(dpDateFrom);
                panel.add(filterLabel("—"));
                panel.add(dpDateTo);
                panel.add(btnFilterReceipts);
            }
            case TAB_CLIENTS -> {
                txtDiscountFilter  = makeFilterTextField("", 80);
                btnFilterByDiscount = makeFilterButton("Фільтр");
                panel.add(filterLabel("Знижка %:"));
                panel.add(txtDiscountFilter);
                panel.add(btnFilterByDiscount);
            }
        }
        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(COLUMNS[activeTab], 0) {
            @Override public boolean isCellEditable(int r, int c){return false;}
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_WHITE);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    public void styleTable() {
        table.setRowHeight(46);
        table.setBackground(BG_WHITE);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JTableHeader h = table.getTableHeader();
        h.setBackground(BG_WHITE);
        h.setFont(new Font("Courier New", Font.PLAIN, 13));
        h.setPreferredSize(new Dimension(0, 42));
        ((DefaultTableCellRenderer)h.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setOpaque(false);

        printButton  = makeActionButton("🖨  Друк",       BTN_LIGHT, TEXT_DARK);
        // ВИДАЛЕНО КНОПКУ "ВИДАЛИТИ"
        editButton   = makeActionButton("✎  Редагувати",  BTN_LIGHT, TEXT_DARK);
        addButton    = makeActionButton("+  Додати",       BTN_DARK,  Color.WHITE);

        panel.add(printButton);
        panel.add(editButton);
        panel.add(addButton);
        return panel;
    }

    private JButton makeActionButton(String text, Color bg, Color fg) {
        JButton btn = new RoundedButton(text, 40); // Переконайтеся, що клас RoundedButton є у вашому проєкті
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setPreferredSize(new Dimension(155, 38));
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton makeFilterButton(String text) {
        JButton btn = new RoundedButton(text, 30);
        btn.setBackground(BTN_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(140, 30));
        btn.setFocusPainted(false);
        return btn;
    }

    private JTextField makeFilterTextField(String placeholder, int width) {
        JTextField f = new JTextField(placeholder);
        f.setPreferredSize(new Dimension(width, 30));
        return f;
    }

    private void styleCombo(JComboBox<String> box, int width) {
        box.setPreferredSize(new Dimension(width, 30));
    }

    private JLabel filterLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        return l;
    }

    // ── Getters ───────────────────────────────────────────────────
    public JTable            getTable()       { return table; }
    public DefaultTableModel getTableModel()  { return tableModel; }
    public int               getActiveTab()   { return activeTab; }
    public JTextField        getSearchField() { return searchField; }
    public JButton[]         getTabButtons()  { return tabButtons; }

    public JButton getAddButton()     { return addButton; }
    public JButton getEditButton()    { return editButton; }
    public JButton getPrintButton()   { return printButton; }

    // Filters Getters
    public JComboBox<String> getCbProductCategory()   { return cbProductCategory; }
    public JButton           getBtnFilterByCategory() { return btnFilterByCategory; }
    public JComboBox<String> getCbStoreSort()   { return cbStoreSort; }
    public JComboBox<String> getCbStoreFilter() { return cbStoreFilter; }
    public JTextField        getTxtUpc()        { return txtUpc; }
    public JButton           getBtnFindByUpc()  { return btnFindByUpc; }
    public JComboBox<String> getCbCashier()         { return cbCashier; }
    public DatePicker        getDpDateFrom()        { return dpDateFrom; }
    public DatePicker        getDpDateTo()          { return dpDateTo; }
    public JButton           getBtnFilterReceipts() { return btnFilterReceipts; }
    public JTextField        getTxtDiscountFilter()   { return txtDiscountFilter; }
    public JButton           getBtnFilterByDiscount() { return btnFilterByDiscount; }
    public JButton             getLogoutButton() { return btnLogout; }


    public void setCategories(String[] categories) {
        if (cbProductCategory == null) return;
        cbProductCategory.removeAllItems();
        cbProductCategory.addItem("Всі категорії");
        for (String c : categories) cbProductCategory.addItem(c);
    }
    public void setCashiers(String[] cashiers) {
        if (cbCashier == null) return;
        cbCashier.removeAllItems();
        cbCashier.addItem("Всі касири");
        for (String c : cashiers) cbCashier.addItem(c);
    }
    public static String[][] getColumns() { return COLUMNS; }

    public static void main(String[] args) {
        CashierFrame frame = new CashierFrame();

    }
}