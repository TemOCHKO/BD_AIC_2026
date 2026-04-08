package org.aic.Repositories;

import org.aic.DBModels.CategoryDBModel;
import org.aic.Storage.IStorageContext;

public class CategoryRepository implements ICategoryRepository {

    private final IStorageContext storageContext;
    public CategoryRepository(IStorageContext storageContext) {
        this.storageContext = storageContext;
    }

    @Override
    public Iterable<CategoryDBModel> getCategories() {
        return storageContext.getCategories();
    }
}
