package org.aic.UI.Controllers;

import org.aic.Repositories.Category.CategoryRepository;
import org.aic.Repositories.Category.ICategoryRepository;
import org.aic.Repositories.Employee.EmployeeRepository;
import org.aic.Repositories.Employee.IEmployeeRepository;
import org.aic.Repositories.Product.IProductRepository;
import org.aic.Repositories.Product.ProductRepository;
import org.aic.Services.Category.CategoryService;
import org.aic.Services.Category.ICategoryService;
import org.aic.Services.Employee.EmployeeService;
import org.aic.Services.Employee.IEmployeeService;
import org.aic.Services.Product.IProductService;
import org.aic.Services.Product.ProductService;
import org.aic.Storage.DataBaseConnection;
import org.aic.UI.Views.ManagerFrame;
import org.aic.UI.Views.TableModels.EmployeeFullTableModel;
import org.aic.UI.Views.TableModels.ProductFullTableModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class ManagerController {

    private boolean isSurnameAscending = false;
    private final ManagerFrame managerView;
    private final IEmployeeService employeeService;
    private final IProductService productService;
    public ManagerController(ManagerFrame managerFrame, IEmployeeService employeeService, IProductService productService) {
        this.managerView = managerFrame;
        this.employeeService = employeeService;
        this.productService = productService;
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
                System.out.println("Checkbox is CHECKED! Filtering for cashiers only...");
                tableModel = new EmployeeFullTableModel(employeeService.getOnlyCashiers());
                managerView.getTable().setModel(tableModel);

                managerView.styleTable();
            } else {
                isSurnameAscending = false;
                System.out.println("Checkbox is UNCHECKED! Showing all employees...");
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
    private void handleTabSwitch(int tabIndex) {
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
            case 0:
                var list = employeeService.getAllEmployees();
                EmployeeFullTableModel fullTableModel = new EmployeeFullTableModel(list);
                managerView.getTable().setModel(fullTableModel);

                initEmployeeController();
                managerView.styleTable();

                break;
            case 1:
                var prList = productService.getAllProducts();
                ProductFullTableModel productFullTableModel = new ProductFullTableModel(prList);
                managerView.getTable().setModel(productFullTableModel);

                managerView.styleTable();
                break;
            default:
                break;
        }
        // Object[][] tabData = storageService.getDataForTab(tabIndex);
        // view.setTableData(columns, tabData);
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
            IProductService prService = new ProductService(productRepository, categoryRepository);

            // 1. Create the View
            ManagerFrame view = new ManagerFrame();

            // 2. Create the Controller, passing the View as a dependency
            ManagerController controller = new ManagerController(view, emplService, prService);

            // 3. Show the View
            view.setVisible(true);
        });
    }
}
