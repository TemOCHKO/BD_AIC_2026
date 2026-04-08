package org.aic;
import org.aic.Repositories.CategoryRepository;
import org.aic.Repositories.ICategoryRepository;
import org.aic.Repositories.IProductRepository;
import org.aic.Repositories.ProductRepository;
import org.aic.Services.CategoryService;
import org.aic.Services.ICategoryService;
import org.aic.Services.IProductService;
import org.aic.Services.ProductService;
import org.aic.Storage.InMemoryStorageContext;
import org.aic.UI.Controllers.ProductController;
import org.aic.UI.Views.AddNewProductView;
import org.aic.UI.Views.ProductView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Always run Swing UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            // Simulate fetching data from the Service/Repository
            InMemoryStorageContext memoryStorageContext = new InMemoryStorageContext();
            IProductRepository productRepository = new ProductRepository(memoryStorageContext);
            ICategoryRepository categoryRepository = new CategoryRepository(memoryStorageContext);
            IProductService productService = new ProductService(productRepository);
            ICategoryService categoryService = new CategoryService(categoryRepository);

            // 1. Create the View (UI)
            ProductView view = new ProductView();
            AddNewProductView addView = new AddNewProductView();

            // 2. Controller
            ProductController controller = new ProductController(view, addView, productService, categoryService);

            // 3. Show the View
            view.setLocationRelativeTo(null);
            view.setVisible(true);
        });
    }
}