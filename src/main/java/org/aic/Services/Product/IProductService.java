package org.aic.Services.Product;

import org.aic.DBModels.ProductDBModel;
import org.aic.DTOModels.ProductTableDTO;

import java.util.List;

public interface IProductService {
    List<ProductDBModel> getAllProducts();
    Iterable<ProductTableDTO> getAllDTOProducts();
    ProductDBModel getProductByName(String name);
    ProductDBModel getProductById(int id);
    void saveNewProduct(ProductDBModel productDBModel);
    boolean deleteProductById(int id) throws IllegalAccessException;
}
