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
        stmt.setString(3, check.getCard_number());
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

        String sql = "SELECT * FROM `Check` WHERE print_date >= DATE_SUB(NOW(), INTERVAL 3 YEAR)";

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
}
