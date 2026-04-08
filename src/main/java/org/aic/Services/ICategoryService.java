package org.aic.Services;

import org.aic.DBModels.CategoryDBModel;

public interface ICategoryService {
    Iterable<CategoryDBModel> getAllCategories();
}
