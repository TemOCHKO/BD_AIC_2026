package org.aic;
import org.aic.DTOModels.Employee.EmployeeListDTO;
import org.aic.Repositories.CategoryRepository;
import org.aic.Repositories.ICategoryRepository;
import org.aic.Repositories.IProductRepository;
import org.aic.Repositories.ProductRepository;
import org.aic.Services.CategoryService;
import org.aic.Services.ICategoryService;
import org.aic.Services.IProductService;
import org.aic.Services.ProductService;
import org.aic.Storage.DataBaseConnection;
import org.aic.Storage.InMemoryStorageContext;
import org.aic.UI.Controllers.ProductController;
import org.aic.UI.Views.AddNewProductView;
import org.aic.UI.Views.EmployeesListView;
import org.aic.UI.Views.ProductView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Always run Swing UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            // 1. Fetch or create a list of your Summary DTOs
            // In a real app, this would come from your database via a DAO/Repository
            List<EmployeeListDTO> employeeList = Arrays.asList(
                    new EmployeeListDTO("Smith", "John", "Software Engineer", "1990-05-15"),
                    new EmployeeListDTO("Shevchenko", "Andriy", "Project Manager", "1985-09-29"),
                    new EmployeeListDTO("Doe", "Jane", "UX Designer", "1992-11-03"),
                    new EmployeeListDTO("Kovalenko", "Olena", "Data Analyst", "1994-02-18")
            );

            // 2. Pass the list to the page and show it
            EmployeesListView listPage = new EmployeesListView(employeeList);
            listPage.setVisible(true);
/*
            Connection connection;
            try {
                connection = DataBaseConnection.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            IProductRepository productRepository = new ProductRepository(connection);
            ICategoryRepository categoryRepository = new CategoryRepository(connection);
            IProductService productService = new ProductService(productRepository, categoryRepository);
            ICategoryService categoryService = new CategoryService(categoryRepository);

            // 1. Create the View (UI)
            ProductView view = new ProductView();
            AddNewProductView addView = new AddNewProductView();

            // 2. Controller
            ProductController controller = new ProductController(view, addView, productService, categoryService);

            // 3. Show the View
            view.setLocationRelativeTo(null);
            view.setVisible(true);*/
        });
    }
}