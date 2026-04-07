package org.aic.DBModels;

import java.util.UUID;

public class StoreProductDBModel {
    public UUID id;
    public String upc;
    public String upcSale;
    public int productId;
    public double price;
    public int numberOfProducts;
    public boolean promotionalProduct;

    // Not a promotional product
    public StoreProductDBModel(String upc, int productId, double price, int numberOfProducts, boolean promotionalProduct) {
        this(UUID.randomUUID(), upc, null, productId, price, numberOfProducts, promotionalProduct);
    }

    // Promotional
    public StoreProductDBModel(String upc, String upcSale, int productId, double price, int numberOfProducts, boolean promotionalProduct) {
        this(UUID.randomUUID(), upc, upcSale, productId, price, numberOfProducts, promotionalProduct);
    }

    public StoreProductDBModel(UUID id, String upc, String upcSale, int productId, double price, int numberOfProducts, boolean promotionalProduct) {
        this.id = id;
        this.upc = upc;
        this.upcSale = upcSale;
        this.productId = productId;
        this.price = price;
        this.numberOfProducts = numberOfProducts;
        this.promotionalProduct = promotionalProduct;
    }


}
