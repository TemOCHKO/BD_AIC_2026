package org.aic.Services.StoreProduct;

import org.aic.DBModels.StoreProductDBModel;

import java.sql.SQLException;
import java.util.List;

public interface IStoreProductService {
    List<StoreProductDBModel> getAllSortedByName();
    boolean deleteStoreProduct(String upc) throws IllegalAccessException;
    boolean updateStoreProduct(StoreProductDBModel sp) ;
    void addStoreProduct(StoreProductDBModel sp);
}
