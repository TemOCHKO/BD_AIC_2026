package org.aic.Services;

import org.aic.DBModels.StoreProductDBModel;

public interface IStoreProductService {
    Iterable<StoreProductDBModel> getAllStoreProducts();
    StoreProductDBModel getStoreProductById(String upc);
}
