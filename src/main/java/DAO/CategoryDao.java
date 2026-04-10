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
    public void addCategory(String name) throws SQLException {
        String sql = "INSERT INTO Category(name) VALUES (?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.executeUpdate();
    }

    public List<String> getAllCategories() throws SQLException {
        List<String> list = new ArrayList<>();

        String sql = "SELECT * FROM Category";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(rs.getInt("id") + " " + rs.getString("name"));

        }
        return list;
    }
}
