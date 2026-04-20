package org.aic.DTOModels;



import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Title", "Manufacturer", "Category"};

    private List<ProductTableDTO> products;

    public ProductTableModel() {
        this.products = new ArrayList<>();
    }

    public void setProducts(List<ProductTableDTO> products) {
        this.products = products;

        // Redraws the screen
        fireTableDataChanged();
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

    // how table pulls data from DTOs
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductTableDTO product = products.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> product.getId().toString().substring(0, 4);
            case 1 -> product.getTitle();
            case 2 -> product.getManufacturer();
            case 3 -> product.getCategory();
            default -> null;
        };
    }

    public ProductTableDTO getProductAt(int rowIndex) {
        return products.get(rowIndex);
    }
}
