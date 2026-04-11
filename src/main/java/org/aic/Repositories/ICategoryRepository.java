package org.aic.Repositories;

import org.aic.DBModels.CategoryDBModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface ICategoryRepository {
    void addCategory(String name) throws SQLException;
    List<CategoryDBModel> getAllCategories() throws SQLException;
    HashMap<Integer, String> getCategoryMap() throws SQLException;
}
