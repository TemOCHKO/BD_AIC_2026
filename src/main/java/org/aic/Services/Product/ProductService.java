package org.aic.Services.Product;

import org.aic.DBModels.ProductDBModel;
import org.aic.DTOModels.ProductTableDTO;
import org.aic.Repositories.Category.ICategoryRepository;
import org.aic.Repositories.Product.IProductRepository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    public HashMap<Integer, String> categoryMap;

    public ProductService(IProductRepository productRepository, ICategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        categoryMap = getCategoryMap();
    }

    @Override
    public List<ProductDBModel> getAllProducts() {
        try {
            return productRepository.getAllProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<ProductTableDTO> getAllDTOProducts() {
        var prods = getAllProducts();
        var dtos = new ArrayList<ProductTableDTO>();
        for (var prod : prods) {
            String categoryName = categoryMap.get(prod.getCategoryNumber());
            dtos.add(new ProductTableDTO(prod.getId(), prod.getDbId(), prod.getTitle(), prod.getManufacturer(), categoryName));
        }
        return dtos;
    }

    @Override
    public ProductDBModel getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    @Override
    public ProductDBModel getProductById(int id) {
        return productRepository.getProductById(id);
    }

    @Override
    public void saveNewProduct(ProductDBModel productDBModel) {
        try {
            productRepository.saveNewProduct(productDBModel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteProductById(int id) throws IllegalAccessException {
        try {
            return productRepository.deleteProductById(id);
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new IllegalAccessException("Cannot delete a product when store products and sales exist. Delete them first");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * п.4 — Пошук товарів за назвою (часткове співпадіння, регістронезалежний).
     */
    @Override
    public List<ProductDBModel> getProductsByName(String name) {
        try {
            return productRepository.getProductsByName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * п.5 — Пошук товарів певної категорії, відсортованих за назвою.
     */
    @Override
    public List<ProductDBModel> getProductsByCategorySortedByName(int categoryNumber) {
        try {
            return productRepository.getProductsByCategorySortedByName(categoryNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<Integer, String> getCategoryMap() {
        try {
            return categoryRepository.getCategoryMap();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}