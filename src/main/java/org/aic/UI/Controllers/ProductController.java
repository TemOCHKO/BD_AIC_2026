package org.aic.UI.Controllers;

import org.aic.DBModels.ProductDBModel;
import org.aic.Services.Category.ICategoryService;
import org.aic.Services.Product.IProductService;
import org.aic.UI.Views.AddNewProductView;
import org.aic.UI.Views.ProductView;

import javax.swing.*;
import java.util.Map;
import java.util.Objects;

public class ProductController {
    private ProductView view;
    private AddNewProductView addNewProductView;
    private IProductService productService;
    private ICategoryService categoryService;

    public ProductController(ProductView view, AddNewProductView addNewProductView, IProductService productService, ICategoryService categoryService) {
        this.view = view;
        this.addNewProductView = addNewProductView;
        this.productService = productService;
        this.categoryService = categoryService;
        initController();
        prepareToShow(view);
    }

    private void initController() {
        view.getLoadDataButton().addActionListener(e -> loadData());
        view.getCreateNewProductButton().addActionListener(e -> goToAddNewProductView());
        addNewProductView.getSaveButton().addActionListener(e -> saveNewProduct());
        addNewProductView.getCancelButton().addActionListener(e -> goBack());
    }

    private void goToAddNewProductView() {
        //view.setVisible(false);
        setCategoryComboBox();
        prepareToShow(addNewProductView);
    }

    private void goBack() {
        cancelShowing(addNewProductView);
    }

    private void saveNewProduct() {
        int key = -1;
        var map = categoryService.getCategoryMap();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), addNewProductView.getCategoryComboBox().getSelectedItem().toString())) {
                key = entry.getKey();
            }
        }

        productService.saveNewProduct(new ProductDBModel(addNewProductView.getNameField().getText(),
                addNewProductView.getManufacturerField().getText(),
                addNewProductView.getCharacteristicsField().getText(),
                key));

        loadData();
        goBack();
    }

    private void loadData() {
        view.displayProducts(productService.getAllDTOProducts());
    }

    private void prepareToShow(JFrame view) {
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    private void cancelShowing(JFrame view) {
        view.setVisible(false);
        view = null;
    }

    private void setCategoryComboBox() {
        addNewProductView.changeCategoryComboBox(categoryService.getCategoryMap().values().toArray(String[]::new));
    }
}

