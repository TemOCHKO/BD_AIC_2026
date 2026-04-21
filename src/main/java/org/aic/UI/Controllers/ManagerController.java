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

        // Swap filter panel
        managerView.getContentCenter().remove(managerView.getFilterPanel());
        managerView.setFilterPanel(managerView.buildFilterPanel(idx));
        managerView.getContentCenter().add(managerView.getFilterPanel(), BorderLayout.NORTH);
        managerView.getContentCenter().revalidate();
        managerView.getContentCenter().repaint();

        // Reset table columns
        managerView.getTableModel().setColumnIdentifiers(ManagerFrame.getColumns()[idx]);
        managerView.getTableModel().setRowCount(0);
        managerView.styleTable();

        // Hide "Додати" on Receipts tab (тільки касир створює чеки)
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
        // Example of where you will bind other UI actions:
        // Using an ItemListener (Recommended for Checkboxes)
        initEmployeeController();
        // view.getAddButton().addActionListener(e -> handleAddAction());
        // view.getBtnSortBySurname().addActionListener(e -> handleSortEmployees());
    }

    private void initEmployeeController() {
        managerView.getChkCashiersOnly().addActionListener(e -> {

            boolean isChecked = managerView.getChkCashiersOnly().isSelected();
            EmployeeFullTableModel tableModel;
            if (isChecked) {
                //System.out.println("Checkbox is CHECKED! Filtering for cashiers only...");
                tableModel = new EmployeeFullTableModel(employeeService.getOnlyCashiers());
                managerView.getTable().setModel(tableModel);

                managerView.styleTable();
            } else {
                isSurnameAscending = false;
                //System.out.println("Checkbox is UNCHECKED! Showing all employees...");
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

        // 4. TODO: Fetch data for this tab from your Service/Repository layers

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

                managerView.styleTable();
                break;
            case ManagerFrame.TAB_RECEIPTS:
                var checkList = checkService.getAllChecks();
                CheckFullTableModel checkFullTableModel = new CheckFullTableModel(checkList);
                managerView.getTable().setModel(checkFullTableModel);

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
        // Object[][] tabData = storageService.getDataForTab(tabIndex);
        // view.setTableData(columns, tabData);
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

        // 3. (Optional but recommended) Ask for confirmation
        int confirm = JOptionPane.showConfirmDialog(managerView,
                "Ви впевнені, що хочете видалити цей запис?",
                "Підтвердження видалення",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return; // User clicked "No" or closed the dialog
        }

        // 4. Route the delete logic based on the active tab
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

            // Add cases for TAB_STORE, TAB_RECEIPTS, TAB_CLIENTS as needed
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
                checkController = new CheckController(null, checkService);
                checkController.show();

                handleTabSwitch(ManagerFrame.TAB_RECEIPTS);
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

    // ═══════════════════════════════════════════════
    // ПОШУК (п.4 — за назвою товару, п.6 — за прізвищем клієнта)
    // ═══════════════════════════════════════════════

    /**
     * Головний диспетчер пошуку — визначає що шукати залежно від активного табу.
     */
    private void handleSearch() {
        int tab = managerView.getActiveTab();
        String query = managerView.getSearchField().getText().trim();

        if (tab == ManagerFrame.TAB_PRODUCTS) {
            searchProductsByName(query);
        } else if (tab == ManagerFrame.TAB_CLIENTS) {
            searchClientsBySurname(query);
        }
    }

    /**
     * п.4 — Пошук товарів за назвою (часткове співпадіння).
     * Якщо поле порожнє — показує всі товари.
     */
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

    /**
     * п.6 — Пошук постійних клієнтів за прізвищем.
     * Якщо поле порожнє — показує всіх клієнтів.
     */
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

    /**
     * п.5 — Пошук товарів певної категорії, відсортованих за назвою.
     * п.4 — Пошук товарів за назвою (через searchField у хедері).
     */
    private void initProductController() {
        // Заповнити дропдаун категорій
        var categoryNames = productService.getCategoryMap().values().toArray(String[]::new);
        managerView.setCategories(categoryNames);

        // п.5 — кнопка "Фільтр за категорією"
        managerView.getBtnFilterByCategory().addActionListener(e -> filterProductsByCategory());
    }

    /**
     * п.5 — Фільтрація товарів за категорією, відсортованих за назвою.
     */
    private void filterProductsByCategory() {
        String selected = (String) managerView.getCbProductCategory().getSelectedItem();

        if (selected == null || selected.equals("Всі категорії")) {
            // Показати всі товари
            var list = productService.getAllProducts();
            managerView.getTable().setModel(new ProductFullTableModel(list));
            managerView.styleTable();
            return;
        }

        // Знайти id категорії по назві
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
        // Очищаємо всі попередні слухачі, щоб уникнути дублювання
        for (ActionListener al : managerView.getBtnFilterByDiscount().getActionListeners()) {
            managerView.getBtnFilterByDiscount().removeActionListener(al);
        }

        // Додаємо слухача на кнопку "Фільтр"
        managerView.getBtnFilterByDiscount().addActionListener(e -> filterClientsByDiscount());
    }

    private void filterClientsByDiscount() {
        String discountText = managerView.getTxtDiscountFilter().getText().trim();

        // Якщо поле порожнє або містить текст-підказку, показуємо всіх клієнтів
        if (discountText.isEmpty() || discountText.equals("Знижка %")) {
            var list = customerCardService.getAllCustomerCards();
            managerView.getTable().setModel(new CustomerCardFullTableModel(list));
            managerView.styleTable();
            return;
        }

        try {
            // Перетворюємо введений текст на число
            int percent = Integer.parseInt(discountText);

            // Викликаємо сервіс для отримання клієнтів з конкретною знижкою
            var list = customerCardService.getCustomersByPercent(percent);

            // Оновлюємо таблицю
            managerView.getTable().setModel(new CustomerCardFullTableModel(list));
            managerView.styleTable();

        } catch (NumberFormatException ex) {
            // Якщо користувач ввів літери замість цифр
            showInputErrorMessage("Будь ласка, введіть коректне число (відсоток) для фільтрації.");
        }
    }


}