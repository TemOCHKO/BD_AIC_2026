package DAO;

import models.Store_Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StoreProductDAO {
    private Connection connection;

    public StoreProductDAO(Connection conn) {
        this.connection = conn;
    }

    public void addStoreProduct(Store_Product sp) {
        try {
            String sql = "INSERT INTO StoreProduct(upc, upc_prom, id_product, selling_price, product_number, promotional_product) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, sp.getUPC());
            stmt.setString(2, sp.getUPC_prom());
            stmt.setInt(3, sp.getId_product());
            stmt.setString(4, sp.getSelling_price());
            stmt.setInt(5, sp.getProduct_number());
            stmt.setString(6, sp.getPromotional_product());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}