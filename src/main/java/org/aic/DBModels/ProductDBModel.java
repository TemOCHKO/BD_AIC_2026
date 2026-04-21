package org.aic.DBModels;
import java.util.UUID;

public class ProductDBModel {
    private UUID id;
    private int dbId;
    private String title;
    private String manufacturer;
    private String description;
    private int categoryNumber;

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

    public ProductDBModel() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(int categoryNumber) {
        this.categoryNumber = categoryNumber;
    }
}
