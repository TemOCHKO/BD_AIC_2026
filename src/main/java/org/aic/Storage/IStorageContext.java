package org.aic.Storage;

import org.aic.DBModels.ProductDBModel;
import org.aic.DBModels.StoreProductDBModel;

public interface IStorageContext {
    Iterable<ProductDBModel> getProducts();
    Iterable<StoreProductDBModel> getStoreProducts();
    ProductDBModel getProduct(int id);
    StoreProductDBModel getStoreProduct(String upc);
    ProductDBModel getProductByName(String name);
}
