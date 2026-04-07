package org.aic.Repositories;

import org.aic.DBModels.StoreProductDBModel;

public interface IStoreProductRepository {
    Iterable<StoreProductDBModel> getStoreProducts();
    StoreProductDBModel getStoreProductById(String upc);
}
