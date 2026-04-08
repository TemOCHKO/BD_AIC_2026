package org.aic.Services;

import org.aic.DBModels.CategoryDBModel;
import org.aic.Repositories.ICategoryRepository;

public class CategoryService implements ICategoryService{

    private final ICategoryRepository categoryRepository;
    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Iterable<CategoryDBModel> getAllCategories() {
        return categoryRepository.getCategories();
    }
}
