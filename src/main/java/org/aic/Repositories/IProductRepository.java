package org.aic.Repositories;

import org.aic.DBModels.ProductDBModel;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface IProductRepository {
    List<ProductDBModel> getAllProducts() throws SQLException;
    ProductDBModel getProductByName(String name);
    ProductDBModel getProductById(int id);
    void saveNewProduct(ProductDBModel productDBModel) throws SQLException;
}
