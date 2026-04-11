package org.aic.Repositories;

import org.aic.DBModels.CategoryDBModel;
import org.aic.Storage.IStorageContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryRepository implements ICategoryRepository {

    private Connection connection;

    public CategoryRepository(Connection connection) {
        this.connection = connection;
    }
    public void addCategory(String name) throws SQLException {
        String sql = "INSERT INTO Category(name) VALUES (?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.executeUpdate();
    }

    public List<CategoryDBModel> getAllCategories() throws SQLException {
        List<CategoryDBModel> list = new ArrayList<>();

        String sql = "SELECT * FROM Category";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(new CategoryDBModel(rs.getInt("id"), rs.getString("name")));
        }
        return list;
    }

    @Override
    public HashMap<Integer, String> getCategoryMap() throws SQLException {
        HashMap<Integer, String> map = new HashMap<>();

        String sql = "SELECT * FROM Category";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            map.put(rs.getInt("category_number"), rs.getString("category_name"));
        }

        return map;
    }


}
