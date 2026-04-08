package org.aic.Repositories;

import org.aic.DBModels.ProductDBModel;

import java.util.UUID;

public interface IProductRepository {
    ProductDBModel getProductByName(String name);
    ProductDBModel getProductById(int id);
    Iterable<ProductDBModel> getProducts();
    void saveNewProduct(ProductDBModel productDBModel);
}
