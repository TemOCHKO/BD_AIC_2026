package org.aic;

import org.aic.Repositories.Category.CategoryRepository;
import org.aic.Repositories.Check.CheckRepository;
import org.aic.Repositories.CustomerCard.CustomerCardRepository;
import org.aic.Repositories.Employee.EmployeeRepository;
import org.aic.Repositories.Product.ProductRepository;
import org.aic.Repositories.StoreProduct.StoreProductRepository;
import org.aic.Services.Category.CategoryService;
import org.aic.Services.Check.CheckService;
import org.aic.Services.CustomerCard.CustomerCardService;
import org.aic.Services.Employee.EmployeeService;
import org.aic.Services.Product.ProductService;
import org.aic.Services.StoreProduct.StoreProductService;
import org.aic.Storage.DataBaseConnection;
import org.aic.UI.Controllers.CashierController;
import org.aic.UI.Controllers.ManagerController;
import org.aic.UI.Controllers.loginC;
import org.aic.UI.Views.CashierFrame;
import org.aic.UI.Views.ManagerFrame;
import org.aic.UI.Views.login;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    // Створюємо статичні сервіси, щоб ініціалізувати їх лише 1 раз при старті
    private static EmployeeService employeeService;
    private static ProductService productService;
    private static StoreProductService storeProductService;
    private static CheckService checkService;
    private static CustomerCardService customerCardService;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // 1. Ініціалізація бази даних та сервісів
        initServices();

        // 2. Запуск вікна логіну
        SwingUtilities.invokeLater(Main::showLoginScreen);
    }

    private static void initServices() {
        try {
            Connection connection = DataBaseConnection.getConnection();

            employeeService = new EmployeeService(new EmployeeRepository(connection));
            productService = new ProductService(new ProductRepository(connection), new CategoryRepository(connection));
            storeProductService = new StoreProductService(new StoreProductRepository(connection));
            checkService = new CheckService(new CheckRepository(connection));
            customerCardService = new CustomerCardService(new CustomerCardRepository(connection));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Не вдалося підключитися до бази даних!", "Критична помилка", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    // Цей метод ми зробили public static, щоб його можна було викликати при натисканні "Вийти"
    public static void showLoginScreen() {
        initServices();
        login loginView = new login();

        loginView.setLoginAction(() -> {
            String id = loginView.getEnteredId();
            String password = loginView.getEnteredPassword();
            String role = loginView.getSelectedRole();

            if (id.isEmpty() || password.isEmpty()) return;

            loginC.AuthData data = loginC.findEmployee(id);

            try {
                if (data == null || !BCrypt.checkpw(password, data.getHashedPassword())) {
                    loginView.showError("Невірний ID або пароль");
                    return;
                }
            } catch (Exception e) {
                loginView.showError("Невірний ID або пароль");
                return;
            }

            if (!data.getRole().equals(role)) {
                loginView.showError("Ця роль не відповідає вашому акаунту");
                return;
            }

            // Якщо логін успішний — закриваємо вікно авторизації
            loginView.dispose();

            // Відкриваємо КОНТРОЛЕР, а не просто View!
            if ("Manager".equals(role)) {
                ManagerFrame managerFrame = new ManagerFrame();
                new ManagerController(managerFrame, employeeService, productService, storeProductService, checkService, customerCardService);
                managerFrame.setVisible(true);
            } else {
                CashierFrame cashierFrame = new CashierFrame();
                new CashierController(cashierFrame, employeeService, productService, storeProductService, checkService, customerCardService);
                cashierFrame.setVisible(true);
            }
        });

        loginView.setVisible(true);
    }
}