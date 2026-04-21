package org.aic.Services.Product;

import org.aic.DBModels.ProductDBModel;
import org.aic.DTOModels.ProductTableDTO;

import java.util.HashMap;
import java.util.List;

public interface IProductService {

    List<ProductDBModel> getAllProducts();

    Iterable<ProductTableDTO> getAllDTOProducts();

    ProductDBModel getProductByName(String name);

    ProductDBModel getProductById(int id);

    void saveNewProduct(ProductDBModel productDBModel);

    boolean deleteProductById(int id) throws IllegalAccessException;

    /**
     * п.4 — Пошук товарів за назвою (часткове співпадіння).
     */
    List<ProductDBModel> getProductsByName(String name);

    /**
     * п.5 — Пошук товарів певної категорії, відсортованих за назвою.
     */
    List<ProductDBModel> getProductsByCategorySortedByName(int categoryNumber);
    HashMap<Integer, String> getCategoryMap();
    boolean updateProduct(ProductDBModel productDBModel);
}
