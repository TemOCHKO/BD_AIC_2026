package org.aic.Repositories.Check;

import org.aic.DBModels.CheckDBModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckRepository implements ICheckRepository {
    private Connection connection;

    public CheckRepository(Connection connection) {
        this.connection = connection;
    }
    public void addCheck(CheckDBModel check) throws SQLException {
        String sql = "INSERT INTO `Check` VALUES(?,?,?,?,?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, check.getCheck_number());
        stmt.setString(2, check.getId_employee());
        if (check.getCard_number() != null) {
            stmt.setString(3, check.getCard_number());
        } else {
            stmt.setNull(3, java.sql.Types.VARCHAR);
        }
        stmt.setString(4, check.getPrint_date());
        stmt.setDouble(5, check.getSum_total());
        stmt.setDouble(6, check.getVat());

        stmt.executeUpdate();
    }

    public List<CheckDBModel> getAllChecks() throws SQLException {
        List<CheckDBModel> list = new ArrayList<>();

        String sql = "SELECT * FROM `Check`";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            CheckDBModel c = new CheckDBModel(
                    rs.getString("check_number"),
                    rs.getString("Id_employee"),
                    rs.getString("card_number"),
                    rs.getString("print_date"),
                    rs.getDouble("sum_total"),
                    rs.getDouble("vat")
            );
            list.add(c);
        }
        return list;
    }
    public List<CheckDBModel> getChecksForLastThreeYears() throws SQLException {
        List<CheckDBModel> list = new ArrayList<>();

        String sql = "SELECT * FROM `Check` WHERE print_date >= CURDATE() - INTERVAL 3 YEAR";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            CheckDBModel c = new CheckDBModel(
                    rs.getString("check_number"),
                    rs.getString("Id_employee"),
                    rs.getString("card_number"),
                    rs.getString("print_date"),
                    rs.getDouble("sum_total"),
                    rs.getDouble("vat")
            );
            list.add(c);
        }
        return list;
    }
    public boolean deleteCheck(String checkNumber) throws SQLException {
        String sql = "DELETE FROM `Check` WHERE check_number = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, checkNumber);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    // Список чеків конкретного касира за певний період
    public List<CheckDBModel> getChecksByEmployeeAndPeriod(String idEmployee, String dateFrom, String dateTo) throws SQLException {
        List<CheckDBModel> list = new ArrayList<>();
        String sql = "SELECT * FROM `Check` WHERE id_employee = ? AND print_date >= ? AND print_date <= ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idEmployee);
            stmt.setString(2, dateFrom);
            stmt.setString(3, dateTo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new CheckDBModel(
                        rs.getString("check_number"),
                        rs.getString("id_employee"),
                        rs.getString("card_number"),
                        rs.getString("print_date"),
                        rs.getDouble("sum_total"),
                        rs.getDouble("vat")
                ));
            }
        }
        return list;
    }


    // Пошук чека за номером з усіма деталями
    public CheckDBModel getCheckByNumber(String checkNumber) throws SQLException {
        String sql = "SELECT * FROM `Check` WHERE check_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, checkNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CheckDBModel(
                        rs.getString("check_number"),
                        rs.getString("id_employee"),
                        rs.getString("card_number"),
                        rs.getString("print_date"),
                        rs.getDouble("sum_total"),
                        rs.getDouble("vat")
                );
            }
        }
        return null;
    }

    // Чеки касира за сьогодні
    public List<CheckDBModel> getChecksByEmployeeToday(String idEmployee) throws SQLException {
        List<CheckDBModel> list = new ArrayList<>();
        String sql = "SELECT * FROM `Check` WHERE id_employee = ? AND DATE(print_date) = CURDATE()";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idEmployee);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new CheckDBModel(
                        rs.getString("check_number"),
                        rs.getString("id_employee"),
                        rs.getString("card_number"),
                        rs.getString("print_date"),
                        rs.getDouble("sum_total"),
                        rs.getDouble("vat")
                ));
            }
        }
        return list;
    }

    // Всі чеки всіх касирів за певний період
    public List<CheckDBModel> getChecksByPeriod(String dateFrom, String dateTo) throws SQLException {
        List<CheckDBModel> list = new ArrayList<>();
        String sql = "SELECT * FROM `Check` WHERE print_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dateFrom);
            stmt.setString(2, dateTo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new CheckDBModel(
                        rs.getString("check_number"),
                        rs.getString("id_employee"),
                        rs.getString("card_number"),
                        rs.getString("print_date"),
                        rs.getDouble("sum_total"),
                        rs.getDouble("vat")
                ));
            }
        }
        return list;
    }

    public boolean updateCheck(CheckDBModel check) {
        // Назву таблиці беремо в зворотні апострофи (backticks), бо check - це зарезервоване слово в SQL
        String sql = "UPDATE `check` SET id_employee = ?, card_number = ?, sum_total = ?, vat = ? WHERE check_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // 1. Касир
            stmt.setString(1, check.getId_employee());

            // 2. Картка клієнта (може бути null, якщо клієнт не має картки)
            if (check.getCard_number() != null && !check.getCard_number().trim().isEmpty()) {
                stmt.setString(2, check.getCard_number());
            } else {
                // Передаємо правильний SQL NULL, якщо картки немає
                stmt.setNull(2, java.sql.Types.VARCHAR);
            }

            // 3. Сума
            stmt.setDouble(3, check.getSum_total());

            // 4. ПДВ
            stmt.setDouble(4, check.getVat());

            // 5. Умова WHERE: шукаємо за номером чека
            stmt.setString(5, check.getCheck_number());

            // Виконуємо запит і перевіряємо, чи був оновлений хоча б один рядок
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Помилка при оновленні чека в базі: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}