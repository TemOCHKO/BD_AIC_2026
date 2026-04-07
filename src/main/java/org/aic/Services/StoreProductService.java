package org.aic.Services;

import org.aic.DBModels.StoreProductDBModel;
import org.aic.Repositories.IStoreProductRepository;
import org.aic.Repositories.StoreProductRepository;

public class StoreProductService implements IStoreProductService {

    private IStoreProductRepository storeProductRepository;

    public StoreProductService(IStoreProductRepository storeProductRepository) {
        this.storeProductRepository = storeProductRepository;
    }


    @Override
    public Iterable<StoreProductDBModel> getAllStoreProducts() {
        return storeProductRepository.getStoreProducts();
    }

    @Override
    public StoreProductDBModel getStoreProductById(String upc) {
        return storeProductRepository.getStoreProductById(upc);
    }

}
