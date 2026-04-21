package org.aic.UI.Controllers;

import org.aic.DBModels.StoreProductDBModel;
import org.aic.DBModels.ProductDBModel;
import org.aic.Services.StoreProduct.IStoreProductService;
import org.aic.Services.Product.IProductService;
import org.aic.UI.Views.StoreProductDialog;
import org.aic.UI.Views.ManagerFrame;

import javax.swing.*;
import java.util.stream.Collectors;

public class StoreProductController {

    private final ManagerController parent;
    private final IStoreProductService storeProductService;
    private final IProductService productService;
    private StoreProductDialog dialog;

    public StoreProductController(ManagerController parent, IStoreProductService spService, IProductService pService) {
        this.parent = parent;
        this.storeProductService = spService;
        this.productService = pService;
    }

    public void showAddDialog() {
        dialog = new StoreProductDialog(parent.getManagerView(), "Додати товар у магазин");
        dialog.setUPCVisible(false); // <--- Приховуємо поле UPC при додаванні!
        setupDialog(null);
        dialog.setVisible(true);
    }

    public void showEditDialog(StoreProductDBModel item) {
        dialog = new StoreProductDialog(parent.getManagerView(), "Редагувати товар у магазині");
        setupDialog(item);
        dialog.setVisible(true);
    }

    private void setupDialog(StoreProductDBModel item) {
        // Завантажуємо список базових товарів
        var products = productService.getAllProducts().stream()
                .map(p -> new StoreProductDialog.ProductItem(p.getDbId(), p.getTitle()))
                .collect(Collectors.toList());
        dialog.setProductsList(products);

        if (item != null) dialog.setData(item);

        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());
        dialog.getBtnSave().addActionListener(e -> handleSave(item == null));
    }

    private void handleSave(boolean isNew) {
        try {
            StoreProductDBModel model = new StoreProductDBModel(
                    dialog.getUPC(),
                    dialog.getUPCProm().isEmpty() ? null : dialog.getUPCProm(),
                    dialog.getSelectedProductId(),
                    "", // назва підтягнеться в базі
                    dialog.getPrice(),
                    dialog.getQuantity(),
                    dialog.isPromotional()
            );

            if (isNew) {
                model.setUPC(generateRandomUPC());
                storeProductService.addStoreProduct(model);
            } else {
                storeProductService.updateStoreProduct(model);
            }

            dialog.dispose();
            parent.handleTabSwitch(ManagerFrame.TAB_STORE); // Оновити таблицю

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Помилка: " + ex.getMessage());
        }
    }

    public StoreProductDialog getDialog() {
        return dialog;
    }

    private String generateRandomUPC() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append((int) (Math.random() * 10)); // Генерує цифру від 0 до 9
        }
        return sb.toString();
    }
}
