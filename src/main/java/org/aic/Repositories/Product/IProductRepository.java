package org.aic.Repositories.Product;

import org.aic.DBModels.ProductDBModel;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface IProductRepository {

    void addProduct(ProductDBModel product) throws SQLException;

    List<ProductDBModel> getAllProducts() throws SQLException;

    ProductDBModel getProductByName(String name);

    ProductDBModel getProductById(int id);

    void saveNewProduct(ProductDBModel productDBModel) throws SQLException;

    boolean deleteProductById(int idProduct) throws SQLException, SQLIntegrityConstraintViolationException;

    List<ProductDBModel> getProductsByName(String name) throws SQLException;

    List<ProductDBModel> getProductsByCategorySortedByName(int categoryNumber) throws SQLException;
    boolean updateProduct(ProductDBModel productDBModel) throws SQLException;
}
