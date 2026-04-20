package org.aic.UI.Views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

public class ManagerFrame extends JFrame {

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

    // ── Tabs ──────────────────────────────────────────────────────
    public static final int TAB_EMPLOYEES = 0;
    public static final int TAB_PRODUCTS  = 1;
    public static final int TAB_STORE     = 2;
    public static final int TAB_RECEIPTS  = 3;
    public static final int TAB_CLIENTS   = 4;

    private static final String[] TABS = {"Працівники", "Товари", "Магазин", "Чеки", "Клієнти"};

    private static final String[][] COLUMNS = {
            {"ID", "Прізвище", "Ім'я", "По батькові", "Посада", "Зарплата", "Дата нар.", "Дата початку", "Телефон", "Місто", "Вулиця", "Індекс"},
            {"ID товару", "Назва", "Виробник", "Характеристики", "Категорія"},
            {"UPC", "Назва", "Ціна продажу", "К-сть", "Акційний"},
            {"Номер чека", "Дата", "ID касира", "Сума", "ПДВ", "Карта клієнта"},
            {"Номер карти", "Прізвище", "Ім'я", "По батькові", "Телефон", "Адреса", "Знижка %"}
    };

    // ── State ─────────────────────────────────────────────────────
    private int activeTab = 0;

    // ── UI: tabs & table ──────────────────────────────────────────
    private JPanel   tabBar;
    private JTable   table;
    private DefaultTableModel tableModel;
    private JPanel   filterPanel;    // змінюється при перемиканні табу
    private JPanel   contentCenter;  // містить filterPanel + tablePanel

    // ── UI: Header ────────────────────────────────────────────────
    private JTextField searchField;

    // ── UI: Filter controls (всі оголошені тут для геттерів) ──────
    private JButton[] tabButtons;

    // Працівники
    private JCheckBox  chkCashiersOnly;
    private JButton    btnSortBySurname;

    // Товари
    private JComboBox<String> cbProductCategory;
    private JButton           btnFilterByCategory;

    // Магазин
    private JComboBox<String> cbStoreSort;      // "За назвою" / "За к-стю"
    private JComboBox<String> cbStoreFilter;    // "Всі" / "Акційні" / "Не акційні"
    private JTextField        txtUpc;
    private JButton           btnFindByUpc;

    // Чеки
    private JComboBox<String> cbCashier;        // "Всі касири" або конкретний
    private JTextField        txtDateFrom;
    private JTextField        txtDateTo;
    private JButton           btnFilterReceipts;
    private JButton           btnTotalByCashier;
    private JButton           btnTotalAll;
    private JButton           btnProductQty;    // к-сть певного товару за період

    // Клієнти
    private JTextField txtDiscountFilter;
    private JButton    btnFilterByDiscount;

    // ── UI: Bottom buttons ────────────────────────────────────────
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton printButton;

    // ── Constructor ───────────────────────────────────────────────
    public ManagerFrame() {
        setTitle("ZLAGODA — Менеджер");
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

    // ═════════════════════════════════════════════════════════════
    // HEADER
    // ═════════════════════════════════════════════════════════════
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

        JLabel logo = new JLabel("ZLAGODA");
        logo.setFont(new Font("Georgia", Font.PLAIN, 30));
        logo.setForeground(TEXT_DARK);
        header.add(logo, BorderLayout.WEST);

        // Search bar
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

        searchField = new JTextField();
        searchField.setOpaque(false);
        searchField.setBorder(null);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(TEXT_DARK);
        searchField.setToolTipText("Пошук за прізвищем або UPC");
        searchWrap.add(searchField, BorderLayout.CENTER);

        JLabel icon = new JLabel("⌕");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        icon.setForeground(TEXT_GRAY);
        searchWrap.add(icon, BorderLayout.EAST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        right.setOpaque(false);
        right.add(searchWrap);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    // ═════════════════════════════════════════════════════════════
    // CONTENT
    // ═════════════════════════════════════════════════════════════
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG_DARK);
        content.setBorder(BorderFactory.createEmptyBorder(6,12,12,12));

        tabBar = buildTabBar();
        content.add(tabBar, BorderLayout.NORTH);

        // center: filterPanel on top, table below
        contentCenter = new JPanel(new BorderLayout());
        contentCenter.setBackground(BG_DARK);

        filterPanel = buildFilterPanel(activeTab);
        contentCenter.add(filterPanel, BorderLayout.NORTH);
        contentCenter.add(buildTablePanel(), BorderLayout.CENTER);

        content.add(contentCenter, BorderLayout.CENTER);
        content.add(buildButtonPanel(), BorderLayout.SOUTH);
        return content;
    }

    // ═════════════════════════════════════════════════════════════
    // TAB BAR
    // ═════════════════════════════════════════════════════════════
    private JPanel buildTabBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        bar.setOpaque(false);

        tabButtons = new JButton[TABS.length]; // Initialize the array

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

            // REMOVED: btn.addActionListener(e -> switchTab(idx));

            tabButtons[i] = btn; // Store in array
            bar.add(btn);
        }
        return bar;
    }

    // Rename this and remove the table/button logic
    public void updateViewForTab(int idx) {
        activeTab = idx;
        tabBar.repaint();

        // Swap filter panel visually
        contentCenter.remove(filterPanel);
        filterPanel = buildFilterPanel(idx);
        contentCenter.add(filterPanel, BorderLayout.NORTH);
        contentCenter.revalidate();
        contentCenter.repaint();
    }

    // ═════════════════════════════════════════════════════════════
    // FILTER PANEL  (різний для кожного табу)
    // ═════════════════════════════════════════════════════════════
    public JPanel buildFilterPanel(int tab) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(BG_FILTER);
        panel.setBorder(BorderFactory.createEmptyBorder(0,4,0,4));

        switch (tab) {

            case TAB_EMPLOYEES -> {
                // п.5: всі за прізвищем | п.6: тільки касири
                chkCashiersOnly = new JCheckBox("Тільки касири");
                styleCheckbox(chkCashiersOnly);
                btnSortBySurname = makeFilterButton("↕ За прізвищем");
                panel.add(filterLabel("Працівники:"));
                panel.add(btnSortBySurname);
                panel.add(chkCashiersOnly);
            }

            case TAB_PRODUCTS -> {
                // п.9: за назвою | п.13: за категорією
                cbProductCategory = new JComboBox<>();
                styleCombo(cbProductCategory, 180);
                cbProductCategory.addItem("Всі категорії");
                btnFilterByCategory = makeFilterButton("Фільтр");
                panel.add(filterLabel("Категорія:"));
                panel.add(cbProductCategory);
                panel.add(btnFilterByCategory);
            }

            case TAB_STORE -> {
                // п.10: за к-стю | п.15/16: акційні/не акційні | п.14: за UPC
                cbStoreFilter = new JComboBox<>(new String[]{"Всі товари","Акційні","Не акційні"});
                styleCombo(cbStoreFilter, 150);

                cbStoreSort = new JComboBox<>(new String[]{"За назвою","За к-стю"});
                styleCombo(cbStoreSort, 130);

                txtUpc = makeFilterTextField("UPC товару", 120);
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
                // п.17-21: касир, діапазон дат, суми
                cbCashier = new JComboBox<>();
                cbCashier.addItem("Всі касири");
                styleCombo(cbCashier, 160);

                txtDateFrom = makeFilterTextField("від дд.мм.рррр", 110);
                txtDateTo   = makeFilterTextField("до дд.мм.рррр",  110);

                btnFilterReceipts  = makeFilterButton("Показати чеки");
                btnTotalByCashier  = makeFilterButton("Σ Касира");
                btnTotalAll        = makeFilterButton("Σ Всіх");
                btnProductQty      = makeFilterButton("К-сть товару");

                panel.add(filterLabel("Касир:"));
                panel.add(cbCashier);
                panel.add(filterLabel("Період:"));
                panel.add(txtDateFrom);
                panel.add(filterLabel("—"));
                panel.add(txtDateTo);
                panel.add(btnFilterReceipts);
                panel.add(Box.createHorizontalStrut(8));
                panel.add(btnTotalByCashier);
                panel.add(btnTotalAll);
                panel.add(btnProductQty);
            }

            case TAB_CLIENTS -> {
                // п.7: за прізвищем | п.12: за відсотком знижки
                txtDiscountFilter  = makeFilterTextField("Знижка %", 80);
                btnFilterByDiscount = makeFilterButton("Фільтр");
                panel.add(filterLabel("Знижка %:"));
                panel.add(txtDiscountFilter);
                panel.add(btnFilterByDiscount);
            }
        }
        return panel;
    }

    // ═════════════════════════════════════════════════════════════
    // TABLE
    // ═════════════════════════════════════════════════════════════
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BG_WHITE); g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        panel.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));

        tableModel = new DefaultTableModel(COLUMNS[activeTab], 0) {
            @Override public boolean isCellEditable(int r, int c){return false;}
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

    public void styleTable() {
        table.setRowHeight(46);
        table.setShowGrid(true);
        table.setGridColor(BORDER_CLR);
        table.setBackground(BG_WHITE);
        table.setForeground(TEXT_DARK);
        table.setSelectionBackground(new Color(0xE8E8E8));
        table.setSelectionForeground(TEXT_DARK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setFocusable(true);

        JTableHeader h = table.getTableHeader();
        h.setBackground(BG_WHITE);
        h.setForeground(TEXT_DARK);
        h.setFont(new Font("Courier New", Font.PLAIN, 13));
        h.setBorder(BorderFactory.createMatteBorder(0,0,2,0,BORDER_CLR));
        h.setPreferredSize(new Dimension(0, 42));
        h.setReorderingAllowed(false);
        ((DefaultTableCellRenderer)h.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer cr = new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(
                    JTable t,Object v,boolean s,boolean f,int r,int c){
                super.getTableCellRendererComponent(t,v,s,f,r,c);
                setBorder(BorderFactory.createEmptyBorder(0,12,0,4));
                return this;
            }
        };
        for(int i=0;i<table.getColumnCount();i++)
            table.getColumnModel().getColumn(i).setCellRenderer(cr);
    }

    // ═════════════════════════════════════════════════════════════
    // BOTTOM BUTTONS
    // ═════════════════════════════════════════════════════════════
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setOpaque(false);

        printButton  = makeActionButton("🖨  Друк",       BTN_LIGHT, TEXT_DARK);
        deleteButton = makeActionButton("✕  Видалити",    BTN_LIGHT, TEXT_DARK);
        editButton   = makeActionButton("✎  Редагувати",  BTN_LIGHT, TEXT_DARK);
        addButton    = makeActionButton("+  Додати",       BTN_DARK,  Color.WHITE);

        panel.add(printButton);
        panel.add(deleteButton);
        panel.add(editButton);
        panel.add(addButton);
        return panel;
    }

    // ═════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════
    private JButton makeActionButton(String text, Color bg, Color fg) {
        JButton btn = new RoundedButton(text, 40);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setPreferredSize(new Dimension(155, 38));
        btn.setMinimumSize(new Dimension(155, 38));
        btn.setMaximumSize(new Dimension(155, 38));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton makeFilterButton(String text) {
        JButton btn = new RoundedButton(text, 30);
        btn.setBackground(BTN_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setPreferredSize(new Dimension(140, 30));
        btn.setMinimumSize(new Dimension(140, 30));
        btn.setMaximumSize(new Dimension(140, 30));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTextField makeFilterTextField(String placeholder, int width) {
        JTextField f = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x555555));
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),16,16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        f.setOpaque(false);
        f.setForeground(new Color(0xCCCCCC));
        f.setCaretColor(Color.WHITE);
        f.setFont(new Font("SansSerif", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
        f.setPreferredSize(new Dimension(width, 30));
        f.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){
                if(f.getText().equals(placeholder)){f.setText("");f.setForeground(Color.WHITE);}
            }
            public void focusLost(FocusEvent e){
                if(f.getText().isEmpty()){f.setText(placeholder);f.setForeground(new Color(0xCCCCCC));}
            }
        });
        return f;
    }

    private void styleCombo(JComboBox<String> box, int width) {
        box.setFont(new Font("SansSerif", Font.PLAIN, 12));
        box.setBackground(new Color(0x555555));
        box.setForeground(Color.WHITE);
        box.setPreferredSize(new Dimension(width, 30));
        box.setFocusable(false);
    }

    private void styleCheckbox(JCheckBox chk) {
        chk.setOpaque(false);
        chk.setForeground(TEXT_LIGHT);
        chk.setFont(new Font("SansSerif", Font.PLAIN, 13));
        chk.setFocusPainted(false);
    }

    private JLabel filterLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(0xAAAAAA));
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }

    // ═════════════════════════════════════════════════════════════
    // GETTERS FOR CONTROLLER
    // ═════════════════════════════════════════════════════════════

    // Table
    public JTable            getTable()       { return table; }
    public DefaultTableModel getTableModel()  { return tableModel; }
    public int               getActiveTab()   { return activeTab; }
    public void              setActiveTab(int idx) { activeTab = idx; }
    public JTextField        getSearchField() { return searchField; }

    public Object getSelectedId() {
        int row = table.getSelectedRow();
        return row == -1 ? null : tableModel.getValueAt(row, 0);
    }

    public void setTableData(String[] columns, Object[][] rows) {
        tableModel.setColumnIdentifiers(columns);
        tableModel.setRowCount(0);
        for (Object[] row : rows) tableModel.addRow(row);
        styleTable();
    }

    // Bottom buttons
    public JButton getAddButton()     { return addButton; }
    public JButton getEditButton()    { return editButton; }
    public JButton getDeleteButton()  { return deleteButton; }
    public JButton getPrintButton()   { return printButton; }

    // Employees filter
    public JCheckBox getChkCashiersOnly()  { return chkCashiersOnly; }
    public JButton   getBtnSortBySurname() { return btnSortBySurname; }

    // Products filter
    public JComboBox<String> getCbProductCategory()   { return cbProductCategory; }
    public JButton           getBtnFilterByCategory() { return btnFilterByCategory; }

    // Store filter
    public JComboBox<String> getCbStoreSort()   { return cbStoreSort; }
    public JComboBox<String> getCbStoreFilter() { return cbStoreFilter; }
    public JTextField        getTxtUpc()        { return txtUpc; }
    public JButton           getBtnFindByUpc()  { return btnFindByUpc; }

    // Receipts filter
    public JComboBox<String> getCbCashier()         { return cbCashier; }
    public JTextField        getTxtDateFrom()        { return txtDateFrom; }
    public JTextField        getTxtDateTo()          { return txtDateTo; }
    public JButton           getBtnFilterReceipts()  { return btnFilterReceipts; }
    public JButton           getBtnTotalByCashier()  { return btnTotalByCashier; }
    public JButton           getBtnTotalAll()        { return btnTotalAll; }
    public JButton           getBtnProductQty()      { return btnProductQty; }

    // Clients filter
    public JTextField getTxtDiscountFilter()   { return txtDiscountFilter; }
    public JButton    getBtnFilterByDiscount() { return btnFilterByDiscount; }

    // Populate cashier combo (controller calls this after loading employees)
    public void setCashiers(String[] cashiers) {
        if (cbCashier == null) return;
        cbCashier.removeAllItems();
        cbCashier.addItem("Всі касири");
        for (String c : cashiers) cbCashier.addItem(c);
    }

    // Populate category combo
    public void setCategories(String[] categories) {
        if (cbProductCategory == null) return;
        cbProductCategory.removeAllItems();
        cbProductCategory.addItem("Всі категорії");
        for (String c : categories) cbProductCategory.addItem(c);
    }

    // ═════════════════════════════════════════════════════════════
    // MAIN
    // ═════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new ManagerFrame().setVisible(true));
    }

    public Component getTabBar() {
        return  tabBar;
    }

    public JPanel getContentCenter() {
        return contentCenter;
    }

    public JPanel getFilterPanel() {
        return filterPanel;
    }

    public void setFilterPanel(JPanel jPanel) {
        filterPanel = jPanel;
    }

    public static String[][] getColumns() {
        return COLUMNS;
    }

    public JButton[] getTabButtons() {
        return tabButtons;
    }
}