package org.aic.DBModels;

import java.util.UUID;

public class CategoryDBModel {
    public UUID id;
    public int dbId;
    public String categoryName;

    public CategoryDBModel(int dbId, String categoryName) {
        this(UUID.randomUUID(), dbId, categoryName);
    }

    private CategoryDBModel(UUID id, int dbId, String categoryName) {
        this.id = id;
        this.dbId = dbId;
        this.categoryName = categoryName;
    }

    public String toString() {
        return categoryName;
    }
}
