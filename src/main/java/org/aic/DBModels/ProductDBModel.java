package org.aic.DBModels;
import java.util.UUID;

public class ProductDBModel {
    public UUID id;
    public int dbId;
    public String title;
    public String manufacturer;
    public String description;
    public int categoryNumber;

    // No entity in database yet
    public ProductDBModel(String title, String manufacturer, String description, int categoryNumber)  {
        this(UUID.randomUUID(), title, manufacturer, description, categoryNumber);
        this.dbId = -1;
    }

    public ProductDBModel(UUID id, String title, String manufacturer, String description, int categoryNumber) {
        this.id = id;
        this.title = title;
        this.manufacturer = manufacturer;
        this.description = description;
        this.categoryNumber = categoryNumber;
    }


    // From DB
    public ProductDBModel(int dbId, String title, String manufacturer, String description, int categoryNumber)  {
        this(UUID.randomUUID(), title, manufacturer, description, categoryNumber);
        this.dbId = dbId;
    }
}
