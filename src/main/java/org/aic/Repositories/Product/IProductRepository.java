package org.aic.Repositories.Product;

import org.aic.DBModels.ProductDBModel;

import java.sql.SQLException;
import java.util.List;

public interface IProductRepository {
    List<ProductDBModel> getAllProducts() throws SQLException;
    ProductDBModel getProductByName(String name);
    ProductDBModel getProductById(int id);
    void saveNewProduct(ProductDBModel productDBModel) throws SQLException;
}
