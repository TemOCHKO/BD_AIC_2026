package org.aic.Repositories;

import org.aic.DBModels.ProductDBModel;
import org.aic.Storage.IStorageContext;

import java.util.UUID;

public class ProductRepository implements IProductRepository {
    private final IStorageContext storageContext;
    public ProductRepository(IStorageContext storageContext)
    {
        this.storageContext = storageContext;
    }

    @Override
    public ProductDBModel getProductByName(String name) {
        return storageContext.getProductByName(name);
    }

    @Override
    public ProductDBModel getProductById(int id) {
        return storageContext.getProduct(id);
    }

    @Override
    public Iterable<ProductDBModel> getProducts() {
        return storageContext.getProducts();
    }

    @Override
    public void saveNewProduct(ProductDBModel productDBModel) {
        storageContext.saveNewProduct(productDBModel);
    }
}
