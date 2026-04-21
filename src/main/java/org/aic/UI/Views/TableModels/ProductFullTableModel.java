package org.aic.UI.Views.TableModels;

import org.aic.DBModels.ProductDBModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductFullTableModel extends AbstractTableModel {

    private final List<ProductDBModel> products;

    private final String[] columnNames = {
            "ID товару", "Назва", "Виробник", "Характеристики", "Категорія"
    };

    public ProductFullTableModel(List<ProductDBModel> products) {
        this.products = products;
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductDBModel p = products.get(rowIndex);

        // Map each column index to the correct Product field
        // Note: Make sure these getter names match exactly what is in your Product class!
        return switch (columnIndex) {
            case 0 -> p.getDbId();
            case 1 -> p.getTitle();
            case 2 -> p.getManufacturer();
            case 3 -> p.getDescription();
            case 4 -> p.getCategoryNumber();
            default -> null;
        };
    }

    /**
     * Instantly grabs the full Product object when a row is clicked.
     */
    public ProductDBModel getProductAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < products.size()) {
            return products.get(rowIndex);
        }
        return null;
    }

    /**
     * Use this if you want to refresh the table with new search results
     * without creating a whole new TableModel instance.
     */
    public void setProducts(List<ProductDBModel> newProducts) {
        this.products.clear();
        if (newProducts != null) {
            this.products.addAll(newProducts);
        }
        fireTableDataChanged(); // Tells JTable to re-render
    }
}