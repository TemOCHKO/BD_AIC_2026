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

    public boolean deleteProductById(int idProduct) throws SQLException, SQLIntegrityConstraintViolationException {
        String deleteStoreProducts = "DELETE FROM store_product WHERE id_product = ?";
        String deleteProduct = "DELETE FROM product WHERE id_product = ?";

        connection.setAutoCommit(false);

        try (PreparedStatement stmtStore = connection.prepareStatement(deleteStoreProducts);
             PreparedStatement stmtProd = connection.prepareStatement(deleteProduct)) {

            stmtStore.setInt(1, idProduct);
            stmtStore.executeUpdate();

            stmtProd.setInt(1, idProduct);
            int rowsAffected = stmtProd.executeUpdate();

            connection.commit();
            return rowsAffected > 0;

        } catch (SQLException e) {
            connection.rollback();
            throw e;

        } finally {
            connection.setAutoCommit(true);
        }
    }
    // Пошук товарів за назвою (часткове співпадіння)
    public List<ProductDBModel> getProductsByName(String name) throws SQLException {
        List<ProductDBModel> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE product_name LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, "%" + name + "%");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(new ProductDBModel(
                    rs.getInt("id_product"),
                    rs.getString("product_name"),
                    rs.getString("producer"),
                    rs.getString("characteristics"),
                    rs.getInt("category_number")
            ));
        }
        return list;
    }

    // Пошук товарів певної категорії, відсортованих за назвою
    public List<ProductDBModel> getProductsByCategorySortedByName(int categoryNumber) throws SQLException {
        List<ProductDBModel> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE category_number = ? ORDER BY product_name ASC";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, categoryNumber);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(new ProductDBModel(
                    rs.getInt("id_product"),
                    rs.getString("product_name"),
                    rs.getString("producer"),
                    rs.getString("characteristics"),
                    rs.getInt("category_number")
            ));
        }
        return list;
    }

    @Override
    public boolean updateProduct(ProductDBModel product) {
        String sql = "UPDATE Product SET product_name = ?, producer = ?, characteristics = ?, category_number = ? WHERE id_product = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, product.getTitle());
            stmt.setString(2, product.getManufacturer());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, product.getCategoryNumber());

            stmt.setInt(5, product.getDbId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }
}