/*
package org.aic.UI.Controllers;

import org.aic.UI.Views.login;

import java.sql.Connection;

public class LoginController {

    private final login view;
    private final Connection connection;
    private final EmplуDao emplDao;

    public LoginC(login view, Connection connection) {
        this.view       = view;
        this.connection = connection;
        this.emplDao    = new EmplDao(connection);

        // підключаємо логіку до кнопки
        view.setLoginAction(this::handleLogin);
    }

    private void handleLogin() {
        String id       = view.getEnteredId();
        String password = view.getEnteredPassword();
        String role     = view.getSelectedRole();

        try {
            Employee emp = emplDao.login(id, password);

            if (emp == null) {
                view.showError("Невірний ID або пароль");
                return;
            }

            if (!emp.getEmpl_role().equals(role)) {
                view.showError("Невірна роль для цього акаунту");
                return;
            }

            // зберігаємо залогіненого юзера
            Session.setCurrentEmployee(emp);
            view.dispose();

            // відкриваємо потрібний фрейм
            openMainFrame(emp);

        } catch (SQLException e) {
            view.showError("Помилка БД: " + e.getMessage());
        }
    }

   */
/* private void openMainFrame(Employee emp) {
        if (emp.getEmpl_role().equals("Manager")) {
            ManagerFrame mf = new ManagerFrame();
            new ManagerController(mf, connection);
            mf.setVisible(true);
        } else {
            CashierFrame cf = new CashierFrame();
            cf.setCashierName(emp.getEmpl_surname() + " " + emp.getEmpl_name());
            new CashierController(cf, connection);
            cf.setVisible(true);
        }
    }*//*

}*/
