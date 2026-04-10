package DAO;

import models.Product;
import org.aic.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private Connection connection;

    public ProductDao(Connection connection) {
        this.connection = connection;
    }

    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Product(category_id, name, producer, characteristics) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, product.getId_product());
        stmt.setString(2, product.getProduct_name());
        stmt.setString(3, product.getProducer());
        stmt.setString(4, product.getCharacteristics());

    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT * FROM Product";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("producer"),
                    rs.getString("characteristics")
            );
            list.add(product);
        }
        return list;

    }

}
