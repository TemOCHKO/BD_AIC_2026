package org.aic.UI.Views.TableModels;

import org.aic.DBModels.StoreProductDBModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;
// import org.aic.Models.StoreProduct; // Import your actual domain class here!

public class StoreProductFullTableModel extends AbstractTableModel {

    private final List<StoreProductDBModel> storeProducts;

    // Defined based on the 6 fields in your entity
    private final String[] columnNames = {
            "UPC",
            "UPC Акційного",
            "Назва", // <--- Changed from ID
            "Ціна",
            "Кількість",
            "Акційний"
    };
    public StoreProductFullTableModel(List<StoreProductDBModel> storeProducts) {
        this.storeProducts = storeProducts;
    }

    @Override
    public int getRowCount() {
        return storeProducts.size();
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
        StoreProductDBModel sp = storeProducts.get(rowIndex);

        // Map each column index to the correct StoreProduct field
        return switch (columnIndex) {
            case 0 -> sp.getUPC();
            case 1 -> sp.getUPC_prom() != null ? sp.getUPC_prom() : "-";
            case 2 -> sp.getProduct_name(); // <--- Now it shows "Молоко" instead of "12"
            case 3 -> sp.getSelling_price();
            case 4 -> sp.getProducts_number();
            case 5 -> sp.getPromotional_product() ? "Так" : "Ні";
            default -> null;
        };
    }

    /**
     * Grabs the full StoreProduct object instantly when a row is clicked.
     */
    public StoreProductDBModel getStoreProductAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < storeProducts.size()) {
            return storeProducts.get(rowIndex);
        }
        return null;
    }

    /**
     * Use this to refresh the table with new search/filter results
     * without creating a whole new TableModel instance.
     */
    public void setStoreProducts(List<StoreProductDBModel> newStoreProducts) {
        this.storeProducts.clear();
        if (newStoreProducts != null) {
            this.storeProducts.addAll(newStoreProducts);
        }
        fireTableDataChanged(); // Tells JTable to re-render
    }
}