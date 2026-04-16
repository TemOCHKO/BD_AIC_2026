package org.aic;
import org.aic.DTOModels.Employee.EmployeeListDTO;
import org.aic.Repositories.Category.CategoryRepository;
import org.aic.Repositories.Category.ICategoryRepository;
import org.aic.Repositories.Employee.EmployeeRepository;
import org.aic.Repositories.Employee.IEmployeeRepository;
import org.aic.Repositories.Product.IProductRepository;
import org.aic.Repositories.Product.ProductRepository;
import org.aic.Services.Category.CategoryService;
import org.aic.Services.Category.ICategoryService;
import org.aic.Services.Employee.EmployeeService;
import org.aic.Services.Employee.IEmployeeService;
import org.aic.Services.Product.IProductService;
import org.aic.Services.Product.ProductService;
import org.aic.Storage.DataBaseConnection;
import org.aic.UI.Controllers.EmployeeController;
import org.aic.UI.Controllers.ProductController;
import org.aic.UI.Views.AddNewProductView;
import org.aic.UI.Views.Employee.AddEmployeeView;
import org.aic.UI.Views.Employee.EmployeesListView;
import org.aic.UI.Views.ProductView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        // Always run Swing UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {


            Connection connection;
            try {
                connection = DataBaseConnection.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

          /*  IProductRepository productRepository = new ProductRepository(connection);
            ICategoryRepository categoryRepository = new CategoryRepository(connection);
            IProductService productService = new ProductService(productRepository, categoryRepository);
            ICategoryService categoryService = new CategoryService(categoryRepository);*/

            IEmployeeRepository employeeRepository = new EmployeeRepository(connection);
            IEmployeeService employeeService = new EmployeeService(employeeRepository);

            // 1. Create the View (UI)
            ProductView view = new ProductView();
            AddNewProductView addView = new AddNewProductView();

            // 2. Controller
            //ProductController controller = new ProductController(view, addView, productService, categoryService);

            EmployeesListView listView = new EmployeesListView();
            AddEmployeeView addEmployeeView = new AddEmployeeView();

            EmployeeController employeeController = new EmployeeController(employeeService, listView, addEmployeeView);
            /*
            // 3. Show the View
            view.setLocationRelativeTo(null);
            view.setVisible(true);*/
        });
    }
}