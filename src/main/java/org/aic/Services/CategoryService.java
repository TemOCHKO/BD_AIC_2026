package org.aic.Services;

import org.aic.DBModels.CategoryDBModel;
import org.aic.Repositories.ICategoryRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class CategoryService implements ICategoryService{

    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDBModel> getAllCategories() {
        try {
            return categoryRepository.getAllCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<Integer, String> getCategoryMap() {
        try {
            return categoryRepository.getCategoryMap();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
