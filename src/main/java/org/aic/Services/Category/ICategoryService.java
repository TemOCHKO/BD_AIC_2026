package org.aic.Services.Category;

import org.aic.DBModels.CategoryDBModel;

import java.util.HashMap;
import java.util.List;

public interface ICategoryService {
    List<CategoryDBModel> getAllCategories();

    HashMap<Integer, String> getCategoryMap();
}
