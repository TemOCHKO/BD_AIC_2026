package org.aic.UI.Views;

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
    private static final Color BTN_GREEN  = new Color(0x3A6B3A);

    // ── Tabs ──────────────────────────────────────────────────────
    public static final int TAB_PRODUCTS = 0;
    public static final int TAB_STORE    = 1;
    public static final int TAB_RECEIPTS = 2;
    public static final int TAB_CLIENTS  = 3;

    private static final String[] TABS = {"Товари", "Магазин", "Чеки", "Клієнти"};

    private static final String[][] COLUMNS = {
            {"ID товару", "Назва", "Виробник", "Характеристики", "Категорія"},
            {"UPC", "Назва", "Ціна продажу", "К-сть", "Акційний"},
            {"Номер чека", "Дата", "Сума", "ПДВ", "Карта клієнта"},
            {"Номер карти", "Прізвище", "Ім'я", "По батькові", "Телефон", "Знижка %"}
    };

    // ── State ─────────────────────────────────────────────────────
    private int activeTab = 0;

    // ── UI: structure ─────────────────────────────────────────────
    private JPanel tabBar;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel filterPanel;
    private JPanel contentCenter;

    // ── UI: Header ────────────────────────────────────────────────
    private JTextField searchField;   // пошук за назвою (п.4) або прізвищем (п.6)
    private JButton    btnSearch;
    private JLabel     lblCashierName; // п.15 — ім'я касира у хедері

    // ── Filter: Товари (п.1, п.5, п.12, п.13) ────────────────────
    private JComboBox<String> cbProductCategory;
    private JButton           btnFilterByCategory;

    // ── Filter: Магазин (п.2, п.12, п.13, п.14) ──────────────────
    private JComboBox<String> cbStoreSort;    // "За назвою" / "За к-стю"
    private JComboBox<String> cbStoreFilter;  // "Всі" / "Акційні" / "Не акційні"
    private JTextField        txtUpc;
    private JButton           btnFindByUpc;

    // ── Filter: Чеки (п.9, п.10, п.11) ───────────────────────────
    private JButton    btnToday;
    private JTextField txtDateFrom;
    private JTextField txtDateTo;
    private JButton    btnFilterReceipts;
    private JTextField txtReceiptNumber;
    private JButton    btnFindByReceipt;

    // ── Filter: Клієнти (п.3, п.6, п.8) ──────────────────────────
    private JTextField txtClientSurname;
    private JButton    btnSearchClient;

    // ── Bottom buttons ────────────────────────────────────────────
    private JButton addButton;
    private JButton editButton;
    private JButton btnNewReceipt;   // п.7 — продаж товарів
    private JButton btnMyProfile;    // п.15 — інфо про себе

    // ── Constructor ───────────────────────────────────────────────
    public CashierFrame() {
        setTitle("ZLAGODA — Касир");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
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
                BorderFactory.createMatteBorder(0,0,2,0,BORDER_CLR),
                BorderFactory.createEmptyBorder(0,20,0,20)
        ));

        // Logo + cashier name (п.15)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        JLabel logo = new JLabel("ZLAGODA");
        logo.setFont(new Font("Georgia", Font.PLAIN, 30));
        logo.setForeground(TEXT_DARK);
        leftPanel.add(logo, BorderLayout.WEST);

        lblCashierName = new JLabel("  |  Касир");
        lblCashierName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblCashierName.setForeground(TEXT_GRAY);
        leftPanel.add(lblCashierName, BorderLayout.CENTER);
        header.add(leftPanel, BorderLayout.WEST);

        // Search bar (п.4 — за назвою, п.6 — за прізвищем)
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
        searchField.setToolTipText("Пошук товару за назвою або клієнта за прізвищем");
        searchWrap.add(searchField, BorderLayout.CENTER);

        btnSearch = new JButton("⌕") {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(0xC5C5C5)); g.fillRect(0,0,getWidth(),getHeight());
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g){}
        };
        btnSearch.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnSearch.setForeground(TEXT_GRAY);
        btnSearch.setContentAreaFilled(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchWrap.add(btnSearch, BorderLayout.EAST);

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

        contentCenter = new JPanel(new BorderLayout());
        contentCenter.setBackground(BG_DARK);

        filterPanel = buildFilterPanel(activeTab);
        contentCenter.add(filterPanel,    BorderLayout.NORTH);
        contentCenter.add(buildTablePanel(), BorderLayout.CENTER);

        content.add(contentCenter,       BorderLayout.CENTER);
        content.add(buildButtonPanel(),  BorderLayout.SOUTH);
        return content;
    }

    // ═════════════════════════════════════════════════════════════
    // TAB BAR
    // ═════════════════════════════════════════════════════════════
    private JPanel buildTabBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        bar.setOpaque(false);

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
            btn.addActionListener(e -> switchTab(idx));
            bar.add(btn);
        }
        return bar;
    }

    private void switchTab(int idx) {
        activeTab = idx;
        tabBar.repaint();

        contentCenter.remove(filterPanel);
        filterPanel = buildFilterPanel(idx);
        contentCenter.add(filterPanel, BorderLayout.NORTH);
        contentCenter.revalidate();
        contentCenter.repaint();

        tableModel.setColumnIdentifiers(COLUMNS[idx]);
        tableModel.setRowCount(0);
        styleTable();

        // На табі Товари/Магазин — тільки перегляд, без Додати/Редагувати
        // На табі Клієнти — можна Додати/Редагувати (п.8)
        // На табі Чеки — тільки перегляд
        addButton.setVisible(idx == TAB_CLIENTS);
        editButton.setVisible(idx == TAB_CLIENTS);
    }

    // ═════════════════════════════════════════════════════════════
    // FILTER PANEL
    // ═════════════════════════════════════════════════════════════
    private JPanel buildFilterPanel(int tab) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(BG_FILTER);

        switch (tab) {

            case TAB_PRODUCTS -> {
                // п.1: за назвою | п.5: за категорією | п.12/13: акційні/не акційні
                cbProductCategory = new JComboBox<>();
                cbProductCategory.addItem("Всі категорії");
                styleCombo(cbProductCategory, 170);

                JComboBox<String> cbProductFilter = new JComboBox<>(
                        new String[]{"Всі товари", "Акційні", "Не акційні"});
                styleCombo(cbProductFilter, 150);

                btnFilterByCategory = makeFilterButton("Застосувати");

                panel.add(filterLabel("Категорія:"));
                panel.add(cbProductCategory);
                panel.add(filterLabel("Фільтр:"));
                panel.add(cbProductFilter);
                panel.add(btnFilterByCategory);
            }

            case TAB_STORE -> {
                // п.2: за назвою | п.12/13: акційні | п.14: за UPC
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
                panel.add(Box.createHorizontalStrut(12));
                panel.add(filterLabel("UPC:"));
                panel.add(txtUpc);
                panel.add(btnFindByUpc);
            }

            case TAB_RECEIPTS -> {
                // п.9: за сьогодні | п.10: за період | п.11: за номером чека
                btnToday = makeFilterButton("📅 Сьогодні");

                txtDateFrom = makeFilterTextField("від дд.мм.рррр", 110);
                txtDateTo   = makeFilterTextField("до дд.мм.рррр",  110);
                btnFilterReceipts = makeFilterButton("Показати");

                txtReceiptNumber = makeFilterTextField("Номер чека", 110);
                btnFindByReceipt = makeFilterButton("Деталі чека");

                panel.add(btnToday);
                panel.add(Box.createHorizontalStrut(8));
                panel.add(filterLabel("Період:"));
                panel.add(txtDateFrom);
                panel.add(filterLabel("—"));
                panel.add(txtDateTo);
                panel.add(btnFilterReceipts);
                panel.add(Box.createHorizontalStrut(12));
                panel.add(filterLabel("Чек №:"));
                panel.add(txtReceiptNumber);
                panel.add(btnFindByReceipt);
            }

            case TAB_CLIENTS -> {
                // п.3: за прізвищем | п.6: пошук за прізвищем | п.8: додати/редагувати
                txtClientSurname = makeFilterTextField("Прізвище клієнта", 160);
                btnSearchClient  = makeFilterButton("Знайти");

                panel.add(filterLabel("Пошук:"));
                panel.add(txtClientSurname);
                panel.add(btnSearchClient);
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

    private void styleTable() {
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

        // п.15 — інфо про себе
        btnMyProfile = makeActionButton("👤  Мій профіль", BTN_LIGHT, TEXT_DARK);

        // п.8 — додати клієнта (видно тільки на табі Клієнти)
        addButton  = makeActionButton("+  Додати",       BTN_DARK,  Color.WHITE);
        editButton = makeActionButton("✎  Редагувати",   BTN_LIGHT, TEXT_DARK);

        // п.7 — створити чек (завжди видно)
        btnNewReceipt = makeActionButton("🧾  Новий чек",  BTN_GREEN, Color.WHITE);

        // За замовчуванням на першому табі (Товари) — без Додати/Редагувати
        addButton.setVisible(false);
        editButton.setVisible(false);

        panel.add(btnMyProfile);
        panel.add(editButton);
        panel.add(addButton);
        panel.add(btnNewReceipt);
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
    public JTable            getTable()      { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public int               getActiveTab()  { return activeTab; }

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

    // Header
    public JTextField getSearchField()  { return searchField; }
    public JButton    getBtnSearch()    { return btnSearch; }

    // п.15 — встановити ім'я касира в хедері
    public void setCashierName(String name) {
        lblCashierName.setText("  |  " + name);
    }

    // Bottom buttons
    public JButton getAddButton()      { return addButton; }
    public JButton getEditButton()     { return editButton; }
    public JButton getBtnNewReceipt()  { return btnNewReceipt; }  // п.7
    public JButton getBtnMyProfile()   { return btnMyProfile; }   // п.15

    // Товари filter
    public JComboBox<String> getCbProductCategory()   { return cbProductCategory; }
    public JButton           getBtnFilterByCategory() { return btnFilterByCategory; }

    // Магазин filter
    public JComboBox<String> getCbStoreSort()   { return cbStoreSort; }
    public JComboBox<String> getCbStoreFilter() { return cbStoreFilter; }
    public JTextField        getTxtUpc()        { return txtUpc; }
    public JButton           getBtnFindByUpc()  { return btnFindByUpc; }

    // Чеки filter
    public JButton    getBtnToday()          { return btnToday; }
    public JTextField getTxtDateFrom()       { return txtDateFrom; }
    public JTextField getTxtDateTo()         { return txtDateTo; }
    public JButton    getBtnFilterReceipts() { return btnFilterReceipts; }
    public JTextField getTxtReceiptNumber()  { return txtReceiptNumber; }
    public JButton    getBtnFindByReceipt()  { return btnFindByReceipt; }

    // Клієнти filter
    public JTextField getTxtClientSurname() { return txtClientSurname; }
    public JButton    getBtnSearchClient()  { return btnSearchClient; }

    // Populate categories
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
        SwingUtilities.invokeLater(() -> new CashierFrame().setVisible(true));
    }
}
