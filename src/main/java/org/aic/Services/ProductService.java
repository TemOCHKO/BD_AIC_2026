package org.aic.Services;

import org.aic.DBModels.ProductDBModel;
import org.aic.Repositories.IProductRepository;

public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    public ProductService(IProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @Override
    public Iterable<ProductDBModel> getAllProducts() {
        return productRepository.getProducts();
    }

    @Override
    public ProductDBModel getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    @Override
    public ProductDBModel getProductById(int id) {
        return productRepository.getProductById(id);
    }
}

