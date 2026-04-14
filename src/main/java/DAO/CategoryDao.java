package DAO;

import models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private Connection connection;

    public CategoryDao(Connection connection) {
        this.connection = connection;
    }
    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO Category(category_name) VALUES (?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, category.getCategory_name());
        stmt.executeUpdate();
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> list = new ArrayList<>();

        String sql = "SELECT * FROM Category";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(new Category(
                    rs.getInt("category_number"),
                    rs.getString("category_name")
            ));
        }
        return list;
    }
    public boolean deleteCategory(int categoryNumber) throws SQLException {
        String sql = "DELETE FROM Category WHERE category_number = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, categoryNumber);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }
    public List<Category> getAllCategoriesSortedByName() throws SQLException {
        List<Category> list = new ArrayList<>();

        String sql = "SELECT * FROM Category ORDER BY category_name ASC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(new Category(
                    rs.getInt("category_number"),
                    rs.getString("category_name")
            ));
        }
        return list;
    }
    public boolean updateCategory(int categoryNumber, String newName) throws SQLException {
        String sql = "UPDATE Category SET category_name = ? WHERE category_number = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, newName);
        stmt.setInt(2, categoryNumber);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

}
