package org.aic.UI.Views;
import org.aic.DTOModels.ProductTableDTO;
import org.aic.DTOModels.ProductTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductView extends JFrame {

    private final ProductTableModel tableModel;
    private final JButton loadDataButton;
    private final JButton createNewProductButton;

    public ProductView() {
        setTitle("Product Catalog Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10)); // 10px padding

        // Setup Table
        tableModel = new ProductTableModel();
        JTable productTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Database Products"));
        add(scrollPane, BorderLayout.CENTER);

        // Setup Button Panel
        JPanel bottomPanel = new JPanel();
        loadDataButton = new RoundedButton("Load Products", 48);
        createNewProductButton = new RoundedButton("Create New Product", 48);
        bottomPanel.add(loadDataButton);
        bottomPanel.add(createNewProductButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- MVC METHODS: How the Controller talks to the View ---

    // Expose the button so the Controller can attach an ActionListener
    public ProductTableModel getTableModel() { return tableModel; }
    public JButton getLoadDataButton() {
        return loadDataButton;
    }
    public JButton getCreateNewProductButton() { return createNewProductButton; }

    // Method to populate the top table
    public void displayProducts(Iterable<ProductTableDTO> products) {
        tableModel.setProducts((List<ProductTableDTO>) products);
    }
}