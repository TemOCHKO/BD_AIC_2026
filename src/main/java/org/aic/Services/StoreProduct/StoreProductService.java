package org.aic.Services.StoreProduct;

import org.aic.DBModels.StoreProductDBModel;
import org.aic.Repositories.StoreProduct.IStoreProductRepository;

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
