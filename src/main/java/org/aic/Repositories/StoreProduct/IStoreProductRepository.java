package org.aic.Repositories.StoreProduct;

import org.aic.DBModels.StoreProductDBModel;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface IStoreProductRepository {
    List<StoreProductDBModel> getAllSortedByName() throws SQLException;
    boolean deleteStoreProduct(String upc) throws SQLException, SQLIntegrityConstraintViolationException;
    void addStoreProduct(StoreProductDBModel sp) throws SQLException;
    boolean updateStoreProduct(StoreProductDBModel sp) throws SQLException;
}
