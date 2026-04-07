package org.aic;

import org.aic.DTOModels.ProductTableDTO;
import org.aic.Repositories.IProductRepository;
import org.aic.Repositories.ProductRepository;
import org.aic.Services.IProductService;
import org.aic.Services.ProductService;
import org.aic.Storage.InMemoryStorageContext;
import org.aic.UI.Views.GroceryView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Always run Swing UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            // 1. Create the View (UI)
            GroceryView view = new GroceryView();

            // 2. Wire up the Controller Logic
            // In a real app, this would call your Service Layer
            view.getLoadDataButton().addActionListener(e -> {

                // Simulate fetching data from the Service/Repository
                InMemoryStorageContext memoryStorageContext = new InMemoryStorageContext();
                IProductRepository productRepository = new ProductRepository(memoryStorageContext);
                IProductService productService = new ProductService(productRepository);

                var list = new ArrayList<ProductTableDTO>();
                for (var prod : productService.getAllProducts()) {
                    list.add(new ProductTableDTO(prod.id, prod.dbId, prod.title, prod.manufacturer, prod.categoryNumber));
                }

                // Push data to the View
                view.displayProducts(list);
            });

            // 3. Show the View
            view.setLocationRelativeTo(null);
            view.setVisible(true);
        });
    }
}