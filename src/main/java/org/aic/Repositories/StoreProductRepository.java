package org.aic.Repositories;

import org.aic.DBModels.StoreProductDBModel;
import org.aic.Storage.IStorageContext;

public class StoreProductRepository implements IStoreProductRepository {
    private IStorageContext storageContext;

    public StoreProductRepository(IStorageContext storageContext) {
        this.storageContext = storageContext;
    }
    @Override
    public Iterable<StoreProductDBModel> getStoreProducts() {
        return storageContext.getStoreProducts();
    }

    @Override
    public StoreProductDBModel getStoreProductById(String upc) {
        return storageContext.getStoreProduct(upc);
    }
}
