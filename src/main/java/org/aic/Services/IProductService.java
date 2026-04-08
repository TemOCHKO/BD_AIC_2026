package org.aic.Services;

import org.aic.DBModels.ProductDBModel;

public interface IProductService {
    Iterable<ProductDBModel> getAllProducts();
    ProductDBModel getProductByName(String name);
    ProductDBModel getProductById(int id);

    void saveNewProduct(ProductDBModel productDBModel);
}
