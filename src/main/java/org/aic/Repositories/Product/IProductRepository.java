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

    /**
     * п.4 — Пошук товарів за назвою (часткове співпадіння).
     */
    List<ProductDBModel> getProductsByName(String name) throws SQLException;

    /**
     * п.5 — Пошук товарів певної категорії, відсортованих за назвою.
     */
    List<ProductDBModel> getProductsByCategorySortedByName(int categoryNumber) throws SQLException;
}
