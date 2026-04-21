package org.aic.UI.Controllers;

import org.aic.DBModels.*;
import org.aic.Repositories.Category.CategoryRepository;
import org.aic.Repositories.Category.ICategoryRepository;
import org.aic.Repositories.Check.CheckRepository;
import org.aic.Repositories.Check.ICheckRepository;
import org.aic.Repositories.CustomerCard.CustomerCardRepository;
import org.aic.Repositories.CustomerCard.ICustomerCardRepository;
import org.aic.Repositories.Employee.EmployeeRepository;
import org.aic.Repositories.Employee.IEmployeeRepository;
import org.aic.Repositories.Product.IProductRepository;
import org.aic.Repositories.Product.ProductRepository;
import org.aic.Repositories.StoreProduct.IStoreProductRepository;
import org.aic.Repositories.StoreProduct.StoreProductRepository;
import org.aic.Services.Category.ICategoryService;
import org.aic.Services.Check.CheckService;
import org.aic.Services.Check.ICheckService;
import org.aic.Services.CustomerCard.CustomerCardService;
import org.aic.Services.CustomerCard.ICustomerCardService;
import org.aic.Services.Employee.EmployeeService;
import org.aic.Services.Employee.IEmployeeService;
import org.aic.Services.Product.IProductService;
import org.aic.Services.Product.ProductService;
import org.aic.Services.StoreProduct.IStoreProductService;
import org.aic.Services.StoreProduct.StoreProductService;
import org.aic.Storage.DataBaseConnection;
import org.aic.UI.Views.CashierFrame;
import org.aic.UI.Views.ManagerFrame;
import org.aic.UI.Views.TableModels.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Collator;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CashierController {

    private final CashierFrame cashierView;
    private final IProductService productService;
    private final IStoreProductService storeProductService;
    private final ICheckService checkService;
    private final ICustomerCardService customerCardService;
    private final IEmployeeService employeeService;

    private AddCustomerCardController addCustomerCardController;
    public CashierController(CashierFrame cashierView, IEmployeeService employeeService, IProductService productService, IStoreProductService storeProductService, ICheckService checkService, ICustomerCardService customerCardService) {
        this.cashierView = cashierView;
        this.productService = productService;
        this.storeProductService = storeProductService;
        this.checkService = checkService;
        this.customerCardService = customerCardService;
        this.employeeService = employeeService;

        initController();
    }

    private void initController() {
        JButton[] tabButtons = cashierView.getTabButtons();
        for (int i = 0; i < tabButtons.length; i++) {
            final int tabIndex = i;
            tabButtons[i].addActionListener(e -> handleTabSwitch(tabIndex));
        }

        cashierView.getAddButton().addActionListener(e -> handleAddAction());
        cashierView.getEditButton().addActionListener(e -> handleEditAction());
        cashierView.getSearchField().addActionListener(e -> handleSearch());

        handleTabSwitch(CashierFrame.TAB_PRODUCTS);
    }

    public void handleTabSwitch(int tabIndex) {
        cashierView.updateViewForTab(tabIndex);
        String[] columns = CashierFrame.getColumns()[tabIndex];
        cashierView.getTableModel().setColumnIdentifiers(columns);
        cashierView.getTableModel().setRowCount(0);
        cashierView.styleTable();

        boolean canAdd = (tabIndex == CashierFrame.TAB_RECEIPTS || tabIndex == CashierFrame.TAB_CLIENTS);
        cashierView.getAddButton().setVisible(canAdd);

        boolean canEdit = (tabIndex == CashierFrame.TAB_CLIENTS);
        cashierView.getEditButton().setVisible(canEdit);
        // ═══════════════════════════════

        switch (tabIndex) {
            case CashierFrame.TAB_PRODUCTS -> {
                var prList = productService.getAllProducts();
                cashierView.getTable().setModel(new ProductFullTableModel(prList));
                initProductController();
                cashierView.styleTable();
            }
            case CashierFrame.TAB_STORE -> {
                var storeProductList = storeProductService.getAllSortedByName();
                cashierView.getTable().setModel(new StoreProductFullTableModel(storeProductList));
                initStoreController();
                cashierView.styleTable();
            }
            case CashierFrame.TAB_RECEIPTS -> {
                var checkList = checkService.getAllChecks();
                cashierView.getTable().setModel(new CheckFullTableModel(checkList));
                cashierView.getAddButton().setVisible(true);
                initReceiptController();
                cashierView.styleTable();
            }
            case CashierFrame.TAB_CLIENTS -> {
                var customerList = customerCardService.getAllCustomerCards();
                cashierView.getTable().setModel(new CustomerCardFullTableModel(customerList));
                initClientController();
                cashierView.getAddButton().setVisible(false);
                cashierView.styleTable();
            }
        }
    }

    private void handleAddAction() {
        int currentTab = cashierView.getActiveTab();
        switch (currentTab) {
            case CashierFrame.TAB_RECEIPTS -> {
                var addCheckController = new AddCheckController(null, checkService, employeeService, customerCardService);
                addCheckController.show();

                handleTabSwitch(CashierFrame.TAB_RECEIPTS);
            }
            case CashierFrame.TAB_CLIENTS -> {
                addCustomerCardController = new AddCustomerCardController(null, customerCardService);
                addCustomerCardController.show();
                handleTabSwitch(CashierFrame.TAB_CLIENTS);
            }
        }
    }

    private void handleEditAction() {
        int currentTab = cashierView.getActiveTab();
        int selectedRow = cashierView.getTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(cashierView, "Будь ласка, оберіть запис для редагування", "Помилка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentTab == CashierFrame.TAB_CLIENTS) {
            CustomerCardFullTableModel model = (CustomerCardFullTableModel) cashierView.getTable().getModel();
            addCustomerCardController = new AddCustomerCardController(null, customerCardService, model.getClientAt(selectedRow));
            addCustomerCardController.show();
            handleTabSwitch(CashierFrame.TAB_CLIENTS);
        }
    }


    private void handleSearch() {
        int tab = cashierView.getActiveTab();
        String query = cashierView.getSearchField().getText().trim();
        if (tab == CashierFrame.TAB_PRODUCTS) {
            var list = query.isEmpty() ? productService.getAllProducts() : productService.getProductsByName(query);
            cashierView.getTable().setModel(new ProductFullTableModel(list));
        } else if (tab == CashierFrame.TAB_CLIENTS) {
            var list = query.isEmpty() ? customerCardService.getAllCustomerCards() : customerCardService.getCustomerBySurname(query);
            cashierView.getTable().setModel(new CustomerCardFullTableModel(list));
        }
        cashierView.styleTable();
    }

    private void initProductController() {
        cashierView.setCategories(productService.getCategoryMap().values().toArray(String[]::new));
        for (ActionListener al : cashierView.getBtnFilterByCategory().getActionListeners())
            cashierView.getBtnFilterByCategory().removeActionListener(al);

        cashierView.getBtnFilterByCategory().addActionListener(e -> {
            String selected = (String) cashierView.getCbProductCategory().getSelectedItem();
            if (selected == null || selected.equals("Всі категорії")) {
                cashierView.getTable().setModel(new ProductFullTableModel(productService.getAllProducts()));
            } else {
                int catId = productService.getCategoryMap().entrySet().stream().filter(entry -> entry.getValue().equals(selected)).map(java.util.Map.Entry::getKey).findFirst().orElse(-1);
                if (catId != -1) cashierView.getTable().setModel(new ProductFullTableModel(productService.getProductsByCategorySortedByName(catId)));
            }
            cashierView.styleTable();
        });
    }

    private void initStoreController() {
        for (ActionListener al : cashierView.getCbStoreFilter().getActionListeners()) cashierView.getCbStoreFilter().removeActionListener(al);
        for (ActionListener al : cashierView.getCbStoreSort().getActionListeners()) cashierView.getCbStoreSort().removeActionListener(al);
        for (ActionListener al : cashierView.getBtnFindByUpc().getActionListeners()) cashierView.getBtnFindByUpc().removeActionListener(al);

        ActionListener listener = e -> {
            Stream<StoreProductDBModel> stream = storeProductService.getAllSortedByName().stream();
            String upc = cashierView.getTxtUpc().getText().trim();
            if (!upc.isEmpty() && !upc.equals("UPC товару")) stream = stream.filter(p -> p.getUPC().contains(upc));

            int filter = cashierView.getCbStoreFilter().getSelectedIndex();
            if (filter == 1) stream = stream.filter(StoreProductDBModel::getPromotional_product);
            else if (filter == 2) stream = stream.filter(p -> !p.getPromotional_product());

            if (cashierView.getCbStoreSort().getSelectedIndex() == 0) {
                Collator uk = Collator.getInstance(new Locale("uk", "UA"));
                stream = stream.sorted((p1, p2) -> uk.compare(p1.getProduct_name(), p2.getProduct_name()));
            } else {
                stream = stream.sorted(Comparator.comparingInt(StoreProductDBModel::getProducts_number));
            }
            cashierView.getTable().setModel(new StoreProductFullTableModel(stream.collect(Collectors.toList())));
            cashierView.styleTable();
        };

        cashierView.getCbStoreFilter().addActionListener(listener);
        cashierView.getCbStoreSort().addActionListener(listener);
        cashierView.getBtnFindByUpc().addActionListener(listener);
    }

    private void initReceiptController() {
        for (ActionListener al : cashierView.getBtnFilterReceipts().getActionListeners()) cashierView.getBtnFilterReceipts().removeActionListener(al);
        cashierView.getBtnFilterReceipts().addActionListener(e -> {
            Stream<CheckDBModel> stream = checkService.getAllChecks().stream();
            String cashier = (String) cashierView.getCbCashier().getSelectedItem();
            if (cashier != null && !cashier.equals("Всі касири")) stream = stream.filter(c -> c.getId_employee().equals(cashier));

            LocalDate dFrom = cashierView.getDpDateFrom().getDate();
            LocalDate dTo = cashierView.getDpDateTo().getDate();

            stream = stream.filter(c -> {
                try {
                    LocalDate d = java.sql.Timestamp.valueOf(c.getPrint_date()).toLocalDateTime().toLocalDate();
                    if (dFrom != null && d.isBefore(dFrom)) return false;
                    if (dTo != null && d.isAfter(dTo)) return false;
                    return true;
                } catch (Exception ex) { return false; }
            });
            cashierView.getTable().setModel(new CheckFullTableModel(stream.collect(Collectors.toList())));
            cashierView.styleTable();
        });
    }

    private void initClientController() {
        for (ActionListener al : cashierView.getBtnFilterByDiscount().getActionListeners()) cashierView.getBtnFilterByDiscount().removeActionListener(al);
        cashierView.getBtnFilterByDiscount().addActionListener(e -> {
            String txt = cashierView.getTxtDiscountFilter().getText().trim();
            if (txt.isEmpty() || txt.equals("Знижка %")) {
                cashierView.getTable().setModel(new CustomerCardFullTableModel(customerCardService.getAllCustomerCards()));
            } else {
                try {
                    cashierView.getTable().setModel(new CustomerCardFullTableModel(customerCardService.getCustomersByPercent(Integer.parseInt(txt))));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(cashierView, "Введіть коректне число");
                }
            }
            cashierView.styleTable();
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Connection connection;
            try {
                connection = DataBaseConnection.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            IEmployeeRepository employeeRepository = new EmployeeRepository(connection);
            IEmployeeService emplService = new EmployeeService(employeeRepository);

            IProductRepository productRepository = new ProductRepository(connection);
            ICategoryRepository categoryRepository = new CategoryRepository(connection);
            IStoreProductRepository storeProductRepository = new StoreProductRepository(connection);
            ICheckRepository checkRepository = new CheckRepository(connection);
            ICustomerCardRepository customerCardRepository = new CustomerCardRepository(connection);

            IProductService prService = new ProductService(productRepository, categoryRepository);
            IStoreProductService storeProductService = new StoreProductService(storeProductRepository);
            ICheckService checkService = new CheckService(checkRepository);
            ICustomerCardService customerCardService = new CustomerCardService(customerCardRepository);
            // 1. Create the View
            CashierFrame view = new CashierFrame();

            // 2. Create the Controller, passing the View as a dependency
            CashierController controller = new CashierController(view, emplService, prService, storeProductService, checkService, customerCardService);

            // 3. Show the View
            view.setVisible(true);
        });
    }

}