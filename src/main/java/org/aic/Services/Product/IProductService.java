package org.aic.Services.Product;

import org.aic.DBModels.ProductDBModel;
import org.aic.DTOModels.ProductTableDTO;

public interface IProductService {
    Iterable<ProductDBModel> getAllProducts();
    Iterable<ProductTableDTO> getAllDTOProducts();
    ProductDBModel getProductByName(String name);
    ProductDBModel getProductById(int id);
    void saveNewProduct(ProductDBModel productDBModel);
}
