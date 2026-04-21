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
import org.aic.Services.Category.CategoryService;
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
import org.aic.UI.Views.ManagerFrame;
import org.aic.UI.Views.TableModels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.Collator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManagerController {
    private boolean isSurnameAscending = false;
    private final ManagerFrame managerView;
    private final IEmployeeService employeeService;
    private final IProductService productService;
    private final IStoreProductService storeProductService;
    private final ICheckService checkService;
    private final ICustomerCardService customerCardService;
    private EmployeesController employeesController;
    private AddCustomerCardController addCustomerCardController;
    private CheckController checkController;
    public ManagerController(ManagerFrame managerFrame, IEmployeeService employeeService, IProductService productService, IStoreProductService storeProductService, ICheckService checkService, ICustomerCardService customerCardService) {
        this.managerView = managerFrame;
        this.employeeService = employeeService;
        this.productService = productService;
        this.storeProductService = storeProductService;
        this.checkService = checkService;
        this.customerCardService = customerCardService;

        initController();
    }

    private void switchTab(int idx) {
        managerView.setActiveTab(idx);
        managerView.getTabBar().repaint();

        managerView.getContentCenter().remove(managerView.getFilterPanel());
        managerView.setFilterPanel(managerView.buildFilterPanel(idx));
        managerView.getContentCenter().add(managerView.getFilterPanel(), BorderLayout.NORTH);
        managerView.getContentCenter().revalidate();
        managerView.getContentCenter().repaint();

        managerView.getTableModel().setColumnIdentifiers(ManagerFrame.getColumns()[idx]);
        managerView.getTableModel().setRowCount(0);
        managerView.styleTable();

        managerView.getAddButton().setVisible(idx != ManagerFrame.TAB_RECEIPTS);
    }

    private void initController() {
        // Bind action listeners to the tab buttons
        JButton[] tabButtons = managerView.getTabButtons();
        for (int i = 0; i < tabButtons.length; i++) {
            final int tabIndex = i;
            tabButtons[i].addActionListener(e -> handleTabSwitch(tabIndex));
        }

        var empl = employeeService.getAllEmployeesSortedBySurname();
        EmployeeFullTableModel tableModel = new EmployeeFullTableModel(empl);
        managerView.getTable().setModel(tableModel);
        managerView.styleTable();

        managerView.getDeleteButton().addActionListener(e -> handleDeleteAction());
        managerView.getAddButton().addActionListener(e -> handleAddAction());
        managerView.getEditButton().addActionListener(e -> handleEditAction());
        managerView.getSearchField().addActionListener(e -> handleSearch());

        managerView.getLogoutButton().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(managerView, "Ви дійсно хочете вийти з акаунту?", "Вихід", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                managerView.dispose(); // Закриваємо поточне вікно
                org.aic.Main.showLoginScreen(); // Викликаємо вікно логіну з нашого нового класу Main
            }
        });

        initEmployeeController();

    }

    private void initEmployeeController() {
        managerView.getChkCashiersOnly().addActionListener(e -> {

            boolean isChecked = managerView.getChkCashiersOnly().isSelected();
            EmployeeFullTableModel tableModel;
            if (isChecked) {
                tableModel = new EmployeeFullTableModel(employeeService.getOnlyCashiers());
                managerView.getTable().setModel(tableModel);

                managerView.styleTable();
            } else {
                isSurnameAscending = false;
                tableModel = new EmployeeFullTableModel(employeeService.getAllEmployees());
                managerView.getTable().setModel(tableModel);

                managerView.styleTable();
            }
        });
        managerView.getBtnSortBySurname().addActionListener(e -> {


            EmployeeFullTableModel tableModel;
            if (managerView.getChkCashiersOnly().isSelected()) {
                var cashiers = employeeService.getOnlyCashiers();

                if (isSurnameAscending) {
                    tableModel = new EmployeeFullTableModel(cashiers);
                    managerView.getTable().setModel(tableModel);

                    managerView.styleTable();
                    isSurnameAscending = false;
                } else {

                    tableModel = new EmployeeFullTableModel(cashiers.reversed());
                    managerView.getTable().setModel(tableModel);

                    managerView.styleTable();
                    isSurnameAscending = true;
                }
            }
            else if (isSurnameAscending) {
                var empl = employeeService.getAllEmployeesSortedBySurname();

                tableModel = new EmployeeFullTableModel(empl);
                managerView.getTable().setModel(tableModel);
                managerView.styleTable();
                isSurnameAscending = false;
            } else {
                var empl = employeeService.getAllEmployeesSortedBySurname();
                empl = empl.reversed();

                tableModel = new EmployeeFullTableModel(empl);
                managerView.getTable().setModel(tableModel);
                managerView.styleTable();
                isSurnameAscending = true;
            }

        });

    }

    /**
     * Handles the logic of switching tabs: updating UI state, changing columns,
     * and managing specific component visibility.
     */
    public void handleTabSwitch(int tabIndex) {
        // 1. Tell the view to redraw its specific panels for the new tab
        managerView.updateViewForTab(tabIndex);

        // 2. Update the table's data model columns based on the selected tab
        String[] columns = ManagerFrame.getColumns()[tabIndex];
        managerView.getTableModel().setColumnIdentifiers(columns);
        managerView.getTableModel().setRowCount(0);
        managerView.styleTable();

        // 3. Controller logic: "Add" button is hidden on the Receipts tab
        managerView.getAddButton().setVisible(tabIndex != ManagerFrame.TAB_RECEIPTS);

        switch (tabIndex) {
            case ManagerFrame.TAB_EMPLOYEES:
                var list = employeeService.getAllEmployeesSortedBySurname();
                EmployeeFullTableModel fullTableModel = new EmployeeFullTableModel(list);
                managerView.getTable().setModel(fullTableModel);

                initEmployeeController();
                managerView.styleTable();

                break;
            case ManagerFrame.TAB_PRODUCTS:
                var prList = productService.getAllProducts();
                ProductFullTableModel productFullTableModel = new ProductFullTableModel(prList);
                managerView.getTable().setModel(productFullTableModel);

                initProductController();
                managerView.styleTable();
                break;
            case ManagerFrame.TAB_STORE:
                var storeProductList = storeProductService.getAllSortedByName();
                StoreProductFullTableModel storeProductFullTableModel = new StoreProductFullTableModel(storeProductList);
                managerView.getTable().setModel(storeProductFullTableModel);

                initStoreController();

                managerView.styleTable();
                break;
            case ManagerFrame.TAB_RECEIPTS:
                var checkList = checkService.getAllChecks();
                CheckFullTableModel checkFullTableModel = new CheckFullTableModel(checkList);
                managerView.getTable().setModel(checkFullTableModel);

                initReceiptController();

                managerView.styleTable();
                break;
            case ManagerFrame.TAB_CLIENTS:
                var customerList = customerCardService.getAllCustomerCards();
                CustomerCardFullTableModel customerCardFullTableModel = new CustomerCardFullTableModel(customerList);
                managerView.getTable().setModel(customerCardFullTableModel);

                initClientController();

                managerView.styleTable();
                break;
            default:
                break;
        }
    }

    private void handleDeleteAction() {
        // 1. Get the currently active tab and selected row
        int currentTab = managerView.getActiveTab();
        int selectedRow = managerView.getTable().getSelectedRow();

        // 2. Prevent crashes if they click Delete without selecting anything
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(managerView,
                    "Будь ласка, оберіть запис для видалення.",
                    "Помилка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(managerView,
                "Ви впевнені, що хочете видалити цей запис?",
                "Підтвердження видалення",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        switch (currentTab) {
            case ManagerFrame.TAB_EMPLOYEES -> {
                EmployeeFullTableModel model = (EmployeeFullTableModel) managerView.getTable().getModel();
                EmployeeDBModel selectedEmp = model.getEmployeeAt(selectedRow);

                employeeService.deleteEmployee(selectedEmp.getId_employee());

                JOptionPane.showMessageDialog(managerView, "Працівника успішно видалено!");

                // Refresh the table data
                handleTabSwitch(ManagerFrame.TAB_EMPLOYEES);
            }

            case ManagerFrame.TAB_PRODUCTS -> {
                ProductFullTableModel model = (ProductFullTableModel) managerView.getTable().getModel();
                ProductDBModel selectedProd = model.getProductAt(selectedRow);

                try {
                    productService.deleteProductById(selectedProd.getDbId());
                } catch (IllegalAccessException e) {
                    showInputErrorMessage(e.getMessage());
                    return;
                }

                JOptionPane.showMessageDialog(managerView, "Товар успішно видалено!");

                // Refresh the table data
                handleTabSwitch(ManagerFrame.TAB_PRODUCTS);
            }

            case ManagerFrame.TAB_STORE -> {
                StoreProductFullTableModel model = (StoreProductFullTableModel) managerView.getTable().getModel();
                StoreProductDBModel selectedStoreProd = model.getStoreProductAt(selectedRow);

                try {
                    storeProductService.deleteStoreProduct(selectedStoreProd.getUPC());
                } catch (IllegalAccessException e) {
                    showInputErrorMessage(e.getMessage());
                    return;
                }

                JOptionPane.showMessageDialog(managerView, "Товар в магазині успішно видалено!");

                // Refresh the table data
                handleTabSwitch(ManagerFrame.TAB_STORE);
            }

            case ManagerFrame.TAB_RECEIPTS -> {
                CheckFullTableModel model = (CheckFullTableModel) managerView.getTable().getModel();
                CheckDBModel selectedCheck = model.getCheckAt(selectedRow);

                checkService.deleteCheck(selectedCheck.getCheck_number());

                JOptionPane.showMessageDialog(managerView, "Чек успішно видалено!");

                // Refresh the table data
                handleTabSwitch(ManagerFrame.TAB_RECEIPTS);
            }

            case ManagerFrame.TAB_CLIENTS -> {
                CustomerCardFullTableModel customerCardFullTableModel = (CustomerCardFullTableModel) managerView.getTable().getModel();
                CustomerCardDBModel selectedCustomer = customerCardFullTableModel.getClientAt(selectedRow);

                customerCardService.deleteCustomerCard(selectedCustomer.getCard_number());

                JOptionPane.showMessageDialog(managerView, "Карту Клієнта успішно видалено!");

                // Refresh the table data
                handleTabSwitch(ManagerFrame.TAB_CLIENTS);
            }

            default -> {
                System.out.println("Видалення не підтримується для цієї вкладки.");
            }
        }
    }

    private void handleAddAction() {
        int currentTab = managerView.getActiveTab();

        switch (currentTab) {
            case ManagerFrame.TAB_EMPLOYEES -> {
                employeesController = new EmployeesController(this, employeeService);
                employeesController.showAddEmployeeDialog();

                handleTabSwitch(ManagerFrame.TAB_EMPLOYEES);
                break;
            }
            case ManagerFrame.TAB_CLIENTS -> {
                addCustomerCardController = new AddCustomerCardController(null, customerCardService);
                addCustomerCardController.show();

                handleTabSwitch(ManagerFrame.TAB_CLIENTS);
                break;
            }
            case ManagerFrame.TAB_PRODUCTS -> {
                ProductsController prodController = new ProductsController(this, productService);
                prodController.showAddDialog();

                handleTabSwitch(ManagerFrame.TAB_PRODUCTS);
                break;
            }
            case ManagerFrame.TAB_STORE -> {
                StoreProductController spCtrl = new StoreProductController(this, storeProductService, productService);
                spCtrl.showAddDialog();
                break;
            }

        }
    }

    private void handleEditAction() {
        // 1. Get the currently active tab and selected row
        int currentTab = managerView.getActiveTab();
        int selectedRow = managerView.getTable().getSelectedRow();

        // 2. Prevent crashes if they click Delete without selecting anything
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(managerView,
                    "Будь ласка, оберіть запис для редагування",
                    "Помилка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        switch (currentTab) {
            case ManagerFrame.TAB_EMPLOYEES -> {
                employeesController = new EmployeesController(this, employeeService);
                employeesController.showEditEmployeeDialog();

                handleTabSwitch(ManagerFrame.TAB_EMPLOYEES);
                break;
            }
            case ManagerFrame.TAB_CLIENTS -> {
                CustomerCardFullTableModel model = (CustomerCardFullTableModel) managerView.getTable().getModel();
                addCustomerCardController = new AddCustomerCardController(null, customerCardService, model.getClientAt(selectedRow));
                addCustomerCardController.show();

                handleTabSwitch(ManagerFrame.TAB_CLIENTS);
                break;
            }
            case ManagerFrame.TAB_RECEIPTS -> {
                CheckFullTableModel model = (CheckFullTableModel) managerView.getTable().getModel();
                CheckDBModel selectedCheck = model.getCheckAt(selectedRow);

                CheckEditController editController = new CheckEditController(
                        this,
                        checkService,
                        employeeService,
                        customerCardService
                );

                editController.showEditDialog(selectedCheck);
                break;
            }
            case ManagerFrame.TAB_PRODUCTS -> {
                ProductFullTableModel model = (ProductFullTableModel) managerView.getTable().getModel();
                ProductDBModel selectedProd = model.getProductAt(selectedRow);

                ProductsController prodController = new ProductsController(this, productService);
                prodController.showEditDialog(selectedProd);
                break;
            }
            case ManagerFrame.TAB_STORE -> {
                StoreProductFullTableModel model = (StoreProductFullTableModel) managerView.getTable().getModel();
                StoreProductDBModel selected = model.getStoreProductAt(selectedRow);

                StoreProductController spCtrl = new StoreProductController(this, storeProductService, productService);
                spCtrl.showEditDialog(selected);
                break;
            }
        }


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
            ManagerFrame view = new ManagerFrame();

            // 2. Create the Controller, passing the View as a dependency
            ManagerController controller = new ManagerController(view, emplService, prService, storeProductService, checkService, customerCardService);

            // 3. Show the View
            view.setVisible(true);
        });
    }

    private void showInputErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(managerView,
                errorMessage,
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(managerView,
                message,
                "",
                JOptionPane.PLAIN_MESSAGE);
    }

    public ManagerFrame getManagerView() {
        return managerView;
    }

    private void handleSearch() {
        int tab = managerView.getActiveTab();
        String query = managerView.getSearchField().getText().trim();

        if (tab == ManagerFrame.TAB_PRODUCTS) {
            searchProductsByName(query);
        } else if (tab == ManagerFrame.TAB_CLIENTS) {
            searchClientsBySurname(query);
        }
    }


    private void searchProductsByName(String name) {
        if (name.isEmpty()) {
            var list = productService.getAllProducts();
            managerView.getTable().setModel(new ProductFullTableModel(list));
        } else {
            var list = productService.getProductsByName(name);
            managerView.getTable().setModel(new ProductFullTableModel(list));
        }
        managerView.styleTable();
    }


    private void searchClientsBySurname(String surname) {
        if (surname.isEmpty()) {
            var list = customerCardService.getAllCustomerCards();
            managerView.getTable().setModel(new CustomerCardFullTableModel(list));
        } else {
            var list = customerCardService.getCustomerBySurname(surname);
            managerView.getTable().setModel(new CustomerCardFullTableModel(list));
        }
        managerView.styleTable();
    }


    private void initProductController() {

        var categoryNames = productService.getCategoryMap().values().toArray(String[]::new);
        managerView.setCategories(categoryNames);

        managerView.getBtnFilterByCategory().addActionListener(e -> filterProductsByCategory());
    }


    private void filterProductsByCategory() {
        String selected = (String) managerView.getCbProductCategory().getSelectedItem();

        if (selected == null || selected.equals("Всі категорії")) {

            var list = productService.getAllProducts();
            managerView.getTable().setModel(new ProductFullTableModel(list));
            managerView.styleTable();
            return;
        }

        int categoryId = -1;
        for (var entry : productService.getCategoryMap().entrySet()) {
            if (entry.getValue().equals(selected)) {
                categoryId = entry.getKey();
                break;
            }
        }
        if (categoryId == -1) return;

        var list = productService.getProductsByCategorySortedByName(categoryId);
        managerView.getTable().setModel(new ProductFullTableModel(list));
        managerView.styleTable();
    }

    private void initClientController() {
        for (ActionListener al : managerView.getBtnFilterByDiscount().getActionListeners()) {
            managerView.getBtnFilterByDiscount().removeActionListener(al);
        }


        managerView.getBtnFilterByDiscount().addActionListener(e -> filterClientsByDiscount());
    }

    private void filterClientsByDiscount() {
        String discountText = managerView.getTxtDiscountFilter().getText().trim();

        if (discountText.isEmpty() || discountText.equals("Знижка %")) {
            var list = customerCardService.getAllCustomerCards();
            managerView.getTable().setModel(new CustomerCardFullTableModel(list));
            managerView.styleTable();
            return;
        }

        try {

            int percent = Integer.parseInt(discountText);


            var list = customerCardService.getCustomersByPercent(percent);


            managerView.getTable().setModel(new CustomerCardFullTableModel(list));
            managerView.styleTable();

        } catch (NumberFormatException ex) {
            showInputErrorMessage("Будь ласка, введіть коректне число (відсоток) для фільтрації.");
        }
    }


    private void initStoreController() {
        for (java.awt.event.ActionListener al : managerView.getCbStoreFilter().getActionListeners()) {
            managerView.getCbStoreFilter().removeActionListener(al);
        }
        for (java.awt.event.ActionListener al : managerView.getCbStoreSort().getActionListeners()) {
            managerView.getCbStoreSort().removeActionListener(al);
        }
        for (java.awt.event.ActionListener al : managerView.getBtnFindByUpc().getActionListeners()) {
            managerView.getBtnFindByUpc().removeActionListener(al);
        }

        managerView.getCbStoreFilter().addActionListener(e -> applyStoreFiltersAndSort());
        managerView.getCbStoreSort().addActionListener(e -> applyStoreFiltersAndSort());

        managerView.getBtnFindByUpc().addActionListener(e -> applyStoreFiltersAndSort());
    }

    private void applyStoreFiltersAndSort() {

        java.util.List<StoreProductDBModel> allStoreProducts = storeProductService.getAllSortedByName();

        int filterIndex = managerView.getCbStoreFilter().getSelectedIndex(); // 0: Всі, 1: Акційні, 2: Не акційні
        int sortIndex = managerView.getCbStoreSort().getSelectedIndex();     // 0: За назвою, 1: За к-стю
        String upcQuery = managerView.getTxtUpc().getText().trim();

        Stream<StoreProductDBModel> stream = allStoreProducts.stream();

        if (!upcQuery.isEmpty() && !upcQuery.equals("UPC товару")) {
            stream = stream.filter(p -> p.getUPC().contains(upcQuery));
        }

        if (filterIndex == 1) {
            stream = stream.filter(StoreProductDBModel::getPromotional_product);
        } else if (filterIndex == 2) { // Не акційні
            stream = stream.filter(p -> !p.getPromotional_product());
        }

        if (sortIndex == 0) {
            Collator ukCollator = Collator.getInstance(new Locale("uk", "UA"));
            stream = stream.sorted((p1, p2) -> ukCollator.compare(p1.getProduct_name(), p2.getProduct_name()));
        } else if (sortIndex == 1) {
            stream = stream.sorted(Comparator.comparingInt(StoreProductDBModel::getProducts_number));

        }

        java.util.List<StoreProductDBModel> filteredList = stream.collect(Collectors.toList());
        managerView.getTable().setModel(new StoreProductFullTableModel(filteredList));
        managerView.styleTable(); // Відновлюємо дизайн таблиці
    }

    private void initReceiptController() {
        for (java.awt.event.ActionListener al : managerView.getBtnFilterReceipts().getActionListeners()) {
            managerView.getBtnFilterReceipts().removeActionListener(al);
        }

        managerView.getBtnFilterReceipts().addActionListener(e -> filterReceipts());
    }

    private void filterReceipts() {
        List<CheckDBModel> allChecks = checkService.getAllChecks();
        Stream<CheckDBModel> stream = allChecks.stream();

        String selectedCashier = (String) managerView.getCbCashier().getSelectedItem();
        if (selectedCashier != null && !selectedCashier.equals("Всі касири")) {
            stream = stream.filter(c -> c.getId_employee().equals(selectedCashier));
        }

        LocalDate dateFrom = managerView.getDpDateFrom().getDate();
        LocalDate dateTo = managerView.getDpDateTo().getDate();

        stream = stream.filter(c -> {
            try {
                LocalDate checkDate = java.sql.Timestamp.valueOf(c.getPrint_date()).toLocalDateTime().toLocalDate();

                if (dateFrom != null && checkDate.isBefore(dateFrom)) {
                    return false;
                }

                if (dateTo != null && checkDate.isAfter(dateTo)) {
                    return false;
                }

                return true;

            } catch (IllegalArgumentException e) {
                System.err.println("Помилка дати чека: " + c.getPrint_date());
                return false;
            }
        });

        List<CheckDBModel> filteredChecks = stream.collect(Collectors.toList());
        managerView.getTable().setModel(new CheckFullTableModel(filteredChecks));
        managerView.styleTable();
    }
}