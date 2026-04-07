package org.aic.DTOModels;

import org.aic.ProductType;

import java.util.UUID;

public class ProductTableDTO {

    private UUID id;
    private int dbId;
    private String title;
    private String manufacturer;
    private ProductType category;

    public ProductTableDTO(UUID id, int dbId, String title, String manufacturer, int productTypeId) {
        this.id = id;
        this.dbId = dbId;
        this.title = title;
        this.manufacturer = manufacturer;

        // TODO fix this kostyl
        this.category = ProductType.values() [productTypeId];

    }

    // Only getters
    public UUID getId() {
        return id;
    }

    public long getSmallId() {
        return id.getMostSignificantBits();
    }

    public int getDbId() {
        return dbId;
    }

    public String getTitle() {
        return title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public ProductType getCategory() {
        return category;
    }
}
