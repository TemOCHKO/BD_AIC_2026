package DAO;

import models.Store_Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreProductDAO {
    private Connection connection;

    public StoreProductDAO(Connection conn) {
        this.connection = conn;
    }

    public void addStoreProduct(Store_Product sp) throws SQLException {
        try {
            String sql = "INSERT INTO Store_Product(upc, upc_prom, id_product, selling_price, products_number, promotional_product) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, sp.getUPC());
            stmt.setString(2, sp.getUPC_prom());
            stmt.setInt(3, sp.getId_product());
            stmt.setString(4, sp.getSelling_price());
            stmt.setInt(5, sp.getProducts_number());
            stmt.setBoolean(6, sp.getPromotional_product());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean deleteStoreProduct(String upc) throws SQLException {
        String sql = "DELETE FROM Store_Product WHERE upc = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, upc);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }
    public List<Store_Product> getAllStoreProducts() throws SQLException {
        List<Store_Product> list = new ArrayList<>();

        String sql = "SELECT * FROM Store_Product";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Store_Product sp = new Store_Product(
                    rs.getString("upc"),
                    rs.getString("upc_prom"),
                    rs.getInt("id_product"),
                    rs.getString("selling_price"),
                    rs.getInt("products_number"),
                    rs.getBoolean("promotional_product")
            );
            list.add(sp);
        }
        return list;
    }
    // оновлення товару в магазині
    public boolean updateStoreProduct(Store_Product sp) throws SQLException {
        String sql = "UPDATE Store_Product SET upc_prom = ?, id_product = ?, selling_price = ?, products_number = ?, promotional_product = ? WHERE upc = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, sp.getUPC_prom());
        stmt.setInt(2, sp.getId_product());
        stmt.setString(3, sp.getSelling_price());
        stmt.setInt(4, sp.getProducts_number());
        stmt.setBoolean(5, sp.getPromotional_product());
        stmt.setString(6, sp.getUPC());

        return stmt.executeUpdate() > 0;
    }

    // відсортовані за кількістю (вимога менеджера №10)
    public List<Store_Product> getAllStoreProductsSortedByQuantity() throws SQLException {
        List<Store_Product> list = new ArrayList<>();

        String sql = "SELECT * FROM Store_Product ORDER BY products_number ASC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(new Store_Product(
                    rs.getString("upc"),
                    rs.getString("upc_prom"),
                    rs.getInt("id_product"),
                    rs.getString("selling_price"),
                    rs.getInt("products_number"),
                    rs.getBoolean("promotional_product")
            ));
        }
        return list;
    }

    // тільки акційні товари (вимога менеджера №15 і касира №12)
    public List<Store_Product> getPromotionalProducts(String sortBy) throws SQLException {
        List<Store_Product> list = new ArrayList<>();

        String order = sortBy.equals("name") ? "id_product ASC" : "products_number ASC";
        String sql = "SELECT * FROM Store_Product WHERE promotional_product = true ORDER BY " + order;

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(new Store_Product(
                    rs.getString("upc"),
                    rs.getString("upc_prom"),
                    rs.getInt("id_product"),
                    rs.getString("selling_price"),
                    rs.getInt("products_number"),
                    rs.getBoolean("promotional_product")
            ));
        }
        return list;
    }

    // тільки не акційні товари (вимога менеджера №16 і касира №13)
    public List<Store_Product> getNonPromotionalProducts(String sortBy) throws SQLException {
        List<Store_Product> list = new ArrayList<>();

        String order = sortBy.equals("name") ? "id_product ASC" : "products_number ASC";
        String sql = "SELECT * FROM Store_Product WHERE promotional_product = false ORDER BY " + order;

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(new Store_Product(
                    rs.getString("upc"),
                    rs.getString("upc_prom"),
                    rs.getInt("id_product"),
                    rs.getString("selling_price"),
                    rs.getInt("products_number"),
                    rs.getBoolean("promotional_product")
            ));
        }
        return list;
    }

    // пошук за UPC (вимога менеджера №14 і касира №14)
    public Store_Product getStoreProductByUPC(String upc) throws SQLException {
        String sql = "SELECT * FROM Store_Product WHERE upc = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, upc);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Store_Product(
                    rs.getString("upc"),
                    rs.getString("upc_prom"),
                    rs.getInt("id_product"),
                    rs.getString("selling_price"),
                    rs.getInt("products_number"),
                    rs.getBoolean("promotional_product")
            );
        }
        return null;
    }
}