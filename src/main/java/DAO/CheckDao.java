package DAO;

import models.Check;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckDao {
    private Connection connection;

    public CheckDao(Connection connection) {
        this.connection = connection;
    }
    public void addCheck(Check check) throws SQLException {
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

    public List<Check> getAllChecks() throws SQLException {
        List<Check> list = new ArrayList<>();

        String sql = "SELECT * FROM `Check`";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Check c = new Check(
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
    public List<Check> getChecksForLastThreeYears() throws SQLException {
        List<Check> list = new ArrayList<>();

        String sql = "SELECT * FROM `Check` WHERE print_date >= CURDATE() - INTERVAL 3 YEAR";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Check c = new Check(
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
    public List<Check> getChecksByEmployeeAndPeriod(String idEmployee, String dateFrom, String dateTo) throws SQLException {
        List<Check> list = new ArrayList<>();
        String sql = "SELECT * FROM `Check` WHERE id_employee = ? AND print_date >= ? AND print_date <= ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idEmployee);
            stmt.setString(2, dateFrom);
            stmt.setString(3, dateTo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Check(
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
    public Check getCheckByNumber(String checkNumber) throws SQLException {
        String sql = "SELECT * FROM `Check` WHERE check_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, checkNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Check(
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
    public List<Check> getChecksByEmployeeToday(String idEmployee) throws SQLException {
        List<Check> list = new ArrayList<>();
        String sql = "SELECT * FROM `Check` WHERE id_employee = ? AND DATE(print_date) = CURDATE()";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idEmployee);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Check(
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
    public List<Check> getChecksByPeriod(String dateFrom, String dateTo) throws SQLException {
        List<Check> list = new ArrayList<>();
        String sql = "SELECT * FROM `Check` WHERE print_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dateFrom);
            stmt.setString(2, dateTo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Check(
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
}
