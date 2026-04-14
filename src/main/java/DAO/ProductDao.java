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
        String sql = "INSERT INTO Product(id_product, category_number, product_name, producer, characteristics) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, product.getId_product());
        stmt.setInt(2, product.getCategory_number());
        stmt.setString(3, product.getProduct_name());
        stmt.setString(4, product.getProducer());
        stmt.setString(5, product.getCharacteristics());

        stmt.executeUpdate();

    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT * FROM Product";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("id_product"),
                    rs.getInt("category_number"),
                    rs.getString("product_name"),
                    rs.getString("producer"),
                    rs.getString("characteristics")
            );
            list.add(product);
        }
        return list;

    }
    public boolean deleteProduct(int idProduct) throws SQLException {
        String sql = "DELETE FROM Product WHERE id_product = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, idProduct);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }
    // оновлення товару
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE Product SET category_number = ?, product_name = ?, producer = ?, characteristics = ? WHERE id_product = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, product.getCategory_number());
        stmt.setString(2, product.getProduct_name());
        stmt.setString(3, product.getProducer());
        stmt.setString(4, product.getCharacteristics());
        stmt.setInt(5, product.getId_product());

        return stmt.executeUpdate() > 0;
    }

    // всі товари відсортовані за назвою (вимога менеджера №9 і касира №1)
    public List<Product> getAllProductsSortedByName() throws SQLException {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT * FROM Product ORDER BY product_name ASC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(new Product(
                    rs.getInt("id_product"),
                    rs.getInt("category_number"),
                    rs.getString("product_name"),
                    rs.getString("producer"),
                    rs.getString("characteristics")
            ));
        }
        return list;
    }

    // пошук товарів за категорією (вимога менеджера №13 і касира №5)
    public List<Product> getProductsByCategory(int categoryNumber) throws SQLException {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT * FROM Product WHERE category_number = ? ORDER BY product_name ASC";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, categoryNumber);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new Product(
                    rs.getInt("id_product"),
                    rs.getInt("category_number"),
                    rs.getString("product_name"),
                    rs.getString("producer"),
                    rs.getString("characteristics")
            ));
        }
        return list;
    }

    // пошук за назвою (вимога касира №4)
    public List<Product> getProductsByName(String name) throws SQLException {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT * FROM Product WHERE product_name LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, "%" + name + "%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new Product(
                    rs.getInt("id_product"),
                    rs.getInt("category_number"),
                    rs.getString("product_name"),
                    rs.getString("producer"),
                    rs.getString("characteristics")
            ));
        }
        return list;
    }
}
