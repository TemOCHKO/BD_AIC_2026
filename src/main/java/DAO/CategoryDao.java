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
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category.getCategory_name());
            stmt.executeUpdate();
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> list = new ArrayList<>();
        // Фільтруємо видалені записи
        String sql = "SELECT * FROM Category WHERE is_deleted = FALSE";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("category_number"),
                        rs.getString("category_name")
                ));
            }
        }
        return list;
    }

    public List<Category> getAllCategoriesSortedByName() throws SQLException {
        List<Category> list = new ArrayList<>();
        // Фільтруємо перед сортуванням
        String sql = "SELECT * FROM Category WHERE is_deleted = FALSE ORDER BY category_name ASC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("category_number"),
                        rs.getString("category_name")
                ));
            }
        }
        return list;
    }

    public boolean updateCategory(int categoryNumber, String newName) throws SQLException {
        String sql = "UPDATE Category SET category_name = ? WHERE category_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, categoryNumber);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCategory(int categoryNumber) throws SQLException {
        // М'яке видалення (Soft Delete)
        String sql = "UPDATE Category SET is_deleted = TRUE WHERE category_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, categoryNumber);
            return stmt.executeUpdate() > 0;
        }
    }
}