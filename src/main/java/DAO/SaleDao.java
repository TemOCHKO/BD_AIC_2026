package DAO;

import models.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDao {
    private Connection connection;

    public SaleDao(Connection connection) {
        this.connection = connection;
    }

    // =============================================
    // ДОДАТИ товар до чеку
    // =============================================
    public void addSale(Sale sale) throws SQLException {
        String sql = "INSERT INTO Sale(UPC, check_number, product_number, selling_price) " +
                "VALUES (?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, sale.getUPC());
        stmt.setString(2, sale.getCheck_number());
        stmt.setInt(3, sale.getProduct_number());
        stmt.setDouble(4, sale.getSelling_price());
        stmt.executeUpdate();
    }

    // =============================================
    // ОТРИМАТИ всі товари певного чеку
    // (з назвою товару, к-стю та ціною)
    // =============================================
    public List<Sale> getSalesByCheckNumber(String checkNumber) throws SQLException {
        List<Sale> list = new ArrayList<>();
        String sql = "SELECT s.UPC, s.check_number, s.product_number, s.selling_price, " +
                "p.product_name " +
                "FROM Sale s " +
                "JOIN Store_Product sp ON s.UPC = sp.upc " +
                "JOIN Product p ON sp.id_product = p.id_product " +
                "WHERE s.check_number = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, checkNumber);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new Sale(
                    rs.getString("UPC"),
                    rs.getString("check_number"),
                    rs.getInt("product_number"),
                    rs.getDouble("selling_price")
            ));
        }
        return list;
    }

    // =============================================
    // ВИЗНАЧИТИ загальну кількість одиниць певного товару
    // проданого за певний період часу
    // =============================================
    public int getTotalProductSoldByPeriod(String UPC, String dateFrom, String dateTo) throws SQLException {
        String sql = "SELECT SUM(s.product_number) as total " +
                "FROM Sale s " +
                "JOIN `Check` c ON s.check_number = c.check_number " +
                "WHERE s.UPC = ? " +
                "AND c.print_date BETWEEN ? AND ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, UPC);
        stmt.setString(2, dateFrom);
        stmt.setString(3, dateTo);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }

    // =============================================
    // ВИДАЛИТИ всі товари чеку
    // (викликається перед видаленням чеку)
    // =============================================
    public void deleteSalesByCheckNumber(String checkNumber) throws SQLException {
        String sql = "DELETE FROM Sale WHERE check_number = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, checkNumber);
        stmt.executeUpdate();
    }
    // Визначити загальну суму проданих товарів за період (всіма касирами)
    public double getTotalSalesSum(String dateFrom, String dateTo) throws SQLException {
        String sql = "SELECT SUM(c.sum_total) as total " +
                "FROM `Check` c " +
                "WHERE c.print_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dateFrom);
            stmt.setString(2, dateTo);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }

    // Визначити загальну суму продажів конкретного касира за період
    public double getSalesSumByEmployee(String idEmployee, String dateFrom, String dateTo) throws SQLException {
        String sql = "SELECT SUM(sum_total) as total FROM `Check` " +
                "WHERE id_employee = ? AND print_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idEmployee);
            stmt.setString(2, dateFrom);
            stmt.setString(3, dateTo);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }
}