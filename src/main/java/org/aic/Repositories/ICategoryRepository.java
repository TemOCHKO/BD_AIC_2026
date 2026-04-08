package org.aic.Repositories;

import org.aic.DBModels.CategoryDBModel;

public interface ICategoryRepository {
    Iterable<CategoryDBModel> getCategories();
}
