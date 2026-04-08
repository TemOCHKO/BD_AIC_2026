package org.aic.UI.Controllers;

import org.aic.DBModels.CategoryDBModel;
import org.aic.DBModels.ProductDBModel;
import org.aic.DTOModels.ProductTableDTO;
import org.aic.ProductType;
import org.aic.Services.ICategoryService;
import org.aic.Services.IProductService;
import org.aic.UI.Views.AddNewProductView;
import org.aic.UI.Views.ProductView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductController {
    private ProductView view;
    private AddNewProductView addNewProductView;
    private IProductService productService;
    private ICategoryService categoryService;
    private HashMap<Integer, String> categories;

    public ProductController(ProductView view, AddNewProductView addNewProductView, IProductService productService, ICategoryService categoryService) {
        this.view = view;
        this.addNewProductView = addNewProductView;
        this.productService = productService;
        initController();
        prepareToShow(view);
    }

    private void initController() {
        view.getLoadDataButton().addActionListener(e -> loadData());
        view.getCreateNewProductButton().addActionListener(e -> goToNewWindow(addNewProductView));
        addNewProductView.getSaveButton().addActionListener(e -> saveNewProduct());
        addNewProductView.getCancelButton().addActionListener(e -> goBack());
    }

    private void goToNewWindow(JFrame view) {
        view.setVisible(false);
        prepareToShow(view);
    }

    private void goBack() {
        cancelShowing(addNewProductView);
    }

    private void saveNewProduct() {
        productService.saveNewProduct(new ProductDBModel(addNewProductView.getNameField().getText(),
                addNewProductView.getManufacturerField().getText(),
                addNewProductView.getCharacteristicsField().getText(),
                categories.addNewProductView.getCategoryComboBox().getSelectedItem());
    }

    private void loadData() {

        // TODO Come up with smth smarter
        categories = new HashMap<Integer, String>();

        for (var category : categoryService.getAllCategories())
            categories.put(category.dbId, category.categoryName);

        // Products
        var list = new ArrayList<ProductTableDTO>();

        for (var prod : productService.getAllProducts()) {
            list.add(new ProductTableDTO(prod.id, prod.dbId, prod.title, prod.manufacturer, categories.get(prod.categoryNumber)));
        }

        // Push data to the View
        view.displayProducts(list);
    }

    private void prepareToShow(JFrame view) {
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    private void cancelShowing(JFrame view) {
        view.setVisible(false);
        view = null;
    }
}

