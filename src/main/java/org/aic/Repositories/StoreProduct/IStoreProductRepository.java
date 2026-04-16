package org.aic.Repositories.StoreProduct;

import org.aic.DBModels.StoreProductDBModel;

public interface IStoreProductRepository {
    Iterable<StoreProductDBModel> getStoreProducts();
    StoreProductDBModel getStoreProductById(String upc);
}
