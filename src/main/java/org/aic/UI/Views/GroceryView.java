package org.aic.UI.Views;
import org.aic.DTOModels.ProductTableDTO;
import org.aic.DTOModels.ProductTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GroceryView extends JFrame {

    private final ProductTableModel tableModel;
    private final JButton loadDataButton;

    public GroceryView() {
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
        loadDataButton = new JButton("Load Products");
        bottomPanel.add(loadDataButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- MVC METHODS: How the Controller talks to the View ---

    // Expose the button so the Controller can attach an ActionListener
    public JButton getLoadDataButton() {
        return loadDataButton;
    }

    // Method to populate the top table
    public void displayProducts(Iterable<ProductTableDTO> products) {
        tableModel.setProducts((List<ProductTableDTO>) products);
    }
}