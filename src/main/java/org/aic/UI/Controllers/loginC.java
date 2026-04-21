package org.aic.UI.Controllers;

import org.aic.Storage.DataBaseConnection;
import org.aic.UI.Views.CashierFrame;
import org.aic.UI.Views.ManagerFrame;
import org.aic.UI.Views.login;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.sql.*;

public class loginC {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            login loginView = new login();

            loginView.setLoginAction(() -> {
                String id       = loginView.getEnteredId();
                String password = loginView.getEnteredPassword();
                String role     = loginView.getSelectedRole();

                if (id.isEmpty() || password.isEmpty()) return;

                // Один запит — повертає і хеш, і роль
                AuthData data = findEmployee(id);

                if (data == null) {
                    // Ідентифікація провалилась — ID не існує
                    // Навмисно не уточнюємо "невірний ID чи пароль" — безпечніше
                    loginView.showError("Невірний ID або пароль");
                    return;
                }

                // Автентифікація — перевіряємо пароль через BCrypt
                if (!BCrypt.checkpw(password, data.hashedPassword)) {
                    loginView.showError("Невірний ID або пароль");
                    return;
                }

                // Перевірка ролі — чи обрана роль відповідає БД
                if (!data.role.equals(role)) {
                    loginView.showError("Ця роль не відповідає вашому акаунту");
                    return;
                }

                // Все ок — відкриваємо головне вікно
                loginView.dispose();

                if ("Manager".equals(role)) {
                    new ManagerFrame().setVisible(true);
                } else {
                    new CashierFrame().setVisible(true);
                }
            });

            loginView.setVisible(true);
        });
    }

    // ── Простий контейнер для даних з БД ─────────────────────────
    private static class AuthData {
        final String hashedPassword;
        final String role;

        AuthData(String hashedPassword, String role) {
            this.hashedPassword = hashedPassword;
            this.role           = role;
        }
    }

    /**
     * Ідентифікація: шукаємо працівника за ID.
     * Повертає хеш пароля + роль, або null якщо ID не знайдено.
     * Пароль у запит НЕ передається — тільки ID.
     */
    private static AuthData findEmployee(String id) {
        String sql = "SELECT empl_password, empl_role FROM Employee WHERE id_employee = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(
                            rs.getString("empl_password"),
                            rs.getString("empl_role")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Помилка з'єднання з БД: " + e.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    // ── Утиліта: хешування при створенні працівника ───────────────
    // Викликати при addEmployee() в контролері:
    // String hash = loginC.hashPassword("1234");
    // emp.setPassword(hash);
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public loginC() {}
}