package org.aic;
import DAO.EmplDao;
import models.Employee;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {

        // Використовуєш свій клас підключення
        Connection connection = DBConnection.getConnection();

        EmplDao dao = new EmplDao(connection);

        // Тест — отримати всіх працівників
        System.out.println("=== Всі працівники ===");
        dao.getAllEmployees().forEach(e ->
                System.out.println(e.getEmpl_surname() + " " + e.getEmpl_name())
        );

        // Тест 2 — додати нового працівника
        Employee newEmp = new Employee(
                "E005", "Чубак", "Анна", "Олександрівна",
                "Cashier", 15000.0,
                "2000-01-01", "2024-01-01",
                "+380991234567", "Київ", "вул. Шевченка 1", "01001"
        );
        dao.addEmployee(newEmp);
        System.out.println("Працівника додано!");

        // Тест 3 — перевірити що додався
        System.out.println("=== Після додавання ===");
        dao.getAllEmployees().forEach(e ->
                System.out.println(e.getEmpl_surname() + " " + e.getEmpl_name())
        );
        connection.close();
    }
}