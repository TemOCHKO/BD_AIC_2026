package org.aic.Services.StoreProduct;

import org.aic.DBModels.StoreProductDBModel;

public interface IStoreProductService {
    Iterable<StoreProductDBModel> getAllStoreProducts();
    StoreProductDBModel getStoreProductById(String upc);
}
