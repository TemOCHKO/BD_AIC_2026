package org.aic.Services.StoreProduct;

import org.aic.DBModels.StoreProductDBModel;
import org.aic.Repositories.StoreProduct.IStoreProductRepository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class StoreProductService implements IStoreProductService {

    private IStoreProductRepository storeProductRepository;

    public StoreProductService(IStoreProductRepository storeProductRepository) {
        this.storeProductRepository = storeProductRepository;
    }


    @Override
    public List<StoreProductDBModel> getAllSortedByName() {
        try {
            return storeProductRepository.getAllSortedByName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteStoreProduct(String upc) throws IllegalAccessException {
        try {
            return storeProductRepository.deleteStoreProduct(upc);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalAccessException("Неможливо видалити цей товар, оскільки він вже фігурує у продажах (чеках). Історія продажів має бути збережена.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateStoreProduct(StoreProductDBModel sp) {
        try {
            return storeProductRepository.updateStoreProduct(sp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addStoreProduct(StoreProductDBModel sp) {
        try {
            storeProductRepository.addStoreProduct(sp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
