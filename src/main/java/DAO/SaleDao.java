package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaleDao {
    private Connection connection;

    public SaleDao(Connection conn) {
        this.connection = conn;
    }

    public void addItem(String UPC, int checkNum, int productNum, String sellingPrice) throws SQLException {
        try {
            String sql = "INSERT INTO ReceiptItem(upc, check_number, product_number, selling_price) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, UPC);
            stmt.setInt(2, checkNum);
            stmt.setInt(3, productNum);
            stmt.setString(4, sellingPrice);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}