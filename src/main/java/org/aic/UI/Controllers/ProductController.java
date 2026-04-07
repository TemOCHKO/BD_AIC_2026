package org.aic.UI.Controllers;

import org.aic.DTOModels.ProductTableDTO;
import org.aic.Services.IProductService;
import org.aic.UI.Views.ProductView;

import java.util.ArrayList;

public class ProductController {
    private ProductView view;
    private IProductService productService;

    public ProductController(ProductView view, IProductService productService) {
        this.view = view;
        this.productService = productService;
        initController();
    }

    private void initController() {
        view.getLoadDataButton().addActionListener(e -> loadData());

    }

    private void loadData() {
        var list = new ArrayList<ProductTableDTO>();

        for (var prod : productService.getAllProducts()) {
            list.add(new ProductTableDTO(prod.id, prod.dbId, prod.title, prod.manufacturer, prod.categoryNumber));
        }

        // Push data to the View
        view.displayProducts(list);
    }
}

