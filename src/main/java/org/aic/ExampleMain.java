package org.aic;
import org.aic.DBConnection;
import org.aic.AddEmployeeForm;
import DAO.EmplDao;

import javax.swing.*;
import java.sql.Connection;

public class ExampleMain {
    public static void main(String[] args) throws Exception {
        Connection connection = DBConnection.getConnection();
        EmplDao emplDao = new EmplDao(connection);

        // Створюємо порожнє головне вікно
        JFrame frame = new JFrame();

        // Відкриваємо форму
        SwingUtilities.invokeLater(() -> {
            new AddEmployeeForm(frame, emplDao).setVisible(true);
        });
    }
}