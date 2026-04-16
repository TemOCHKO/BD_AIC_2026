package org.aic.Repositories.Product;

import org.aic.DBModels.ProductDBModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductRepository implements IProductRepository {

    private Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    public void addProduct(ProductDBModel product) throws SQLException {
        String sql = "INSERT INTO Product(category_id, name, producer, characteristics) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, product.getDbId());
        stmt.setString(2, product.getTitle());
        stmt.setString(3, product.getManufacturer());
        stmt.setString(4, product.getDescription());
    }

    public List<ProductDBModel> getAllProducts() throws SQLException {
        List<ProductDBModel> list = new ArrayList<>();

        String sql = "SELECT * FROM Product";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            ProductDBModel product = new ProductDBModel(
                    rs.getInt("id_product"),
                    rs.getString("product_name"),
                    rs.getString("producer"),
                    rs.getString("characteristics"),
                    rs.getInt("category_number")
            );
            list.add(product);
        }
        return list;

    }

    @Override
    public ProductDBModel getProductByName(String name) {
        return null;
    }

    @Override
    public ProductDBModel getProductById(int id) {
        return null;
    }

    @Override
    public void saveNewProduct(ProductDBModel productDBModel) throws SQLException {
        String newId = "EMP-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        String sql = "INSERT INTO product(category_number, product_name, producer, characteristics) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, productDBModel.getCategoryNumber());
        stmt.setString(2, productDBModel.getTitle());
        stmt.setString(3, productDBModel.getManufacturer());
        stmt.setString(4, productDBModel.getDescription());

        stmt.execute();
    }
}
