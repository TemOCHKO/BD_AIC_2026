package org.aic.UI.Controllers;

import org.aic.DBModels.ProductDBModel;
import org.aic.Services.Product.IProductService;
import org.aic.UI.Views.ManagerFrame;
import org.aic.UI.Views.ProductDialog;

import javax.swing.*;
import java.util.Map;

public class ProductsController {

    private final ManagerController parentController;
    private final IProductService productService;
    private ProductDialog dialog;

    public ProductsController(ManagerController parentController, IProductService productService) {
        this.parentController = parentController;
        this.productService = productService;
    }

    // Call this for ADDING a new product
    public void showAddDialog() {
        dialog = new ProductDialog(parentController.getManagerView(), "Додати новий товар");
        initDialog();
        dialog.setVisible(true);
    }

    // Call this for EDITING an existing product
    public void showEditDialog(ProductDBModel productToEdit) {
        dialog = new ProductDialog(parentController.getManagerView(), "Редагувати товар");
        initDialog();

        // Find the category name to pre-select it in the dropdown
        Map<Integer, String> categories = productService.getCategoryMap();
        String categoryName = categories.get(productToEdit.getCategoryNumber());

        dialog.setProductData(productToEdit, categoryName);
        dialog.setVisible(true);
    }

    private void initDialog() {
        // 1. Populate the Categories dropdown
        Map<Integer, String> categoryMap = productService.getCategoryMap();
        dialog.populateCategories(categoryMap.values().toArray(new String[0]));

        // 2. Attach Listeners
        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());
        dialog.getBtnSave().addActionListener(e -> handleSave());
    }

    private void handleSave() {
        try {
            // 1. Validate inputs
            String title = dialog.getTitleInput();
            String manufacturer = dialog.getManufacturerInput();
            String characteristics = dialog.getCharacteristicsInput();
            String selectedCategoryName = dialog.getSelectedCategory();

            if (title.isEmpty() || manufacturer.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Назва та Виробник є обов'язковими!", "Помилка", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Get Category ID from the selected name
            int categoryId = -1;
            for (Map.Entry<Integer, String> entry : productService.getCategoryMap().entrySet()) {
                if (entry.getValue().equals(selectedCategoryName)) {
                    categoryId = entry.getKey();
                    break;
                }
            }

            // 3. Create or Update the model
            ProductDBModel product = dialog.getCurrentProduct();
            boolean isNew = false;

            if (product == null) {
                product = new ProductDBModel();
                isNew = true;
            }

            product.setTitle(title);
            product.setManufacturer(manufacturer);
            product.setDescription(characteristics); // or setCharacteristics
            product.setCategoryNumber(categoryId);

            // 4. Send to Database
            if (isNew) {
                // TODO: Call your service to INSERT
                productService.saveNewProduct(product);
                JOptionPane.showMessageDialog(dialog, "Товар успішно додано!");
            } else {
                // TODO: Call your service to UPDATE
                productService.updateProduct(product);
                JOptionPane.showMessageDialog(dialog, "Зміни успішно збережено!");
            }

            // 5. Close dialog and refresh the main table
            dialog.dispose();
            parentController.handleTabSwitch(ManagerFrame.TAB_PRODUCTS);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Сталася помилка при збереженні: " + ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }
}