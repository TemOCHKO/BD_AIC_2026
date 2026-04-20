package org.aic.UI.Views.TableModels;

import org.aic.DBModels.CustomerCardDBModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CustomerCardFullTableModel extends AbstractTableModel {
    private final List<CustomerCardDBModel> clients; // Assuming your class is named Client or Customer

    // Matches the exact columns from TAB_CLIENTS in ManagerFrame
    private final String[] columnNames = {
            "Номер карти",
            "Прізвище",
            "Ім'я",
            "По батькові",
            "Телефон",
            "Адреса",
            "Знижка %"
    };

    public CustomerCardFullTableModel(List<CustomerCardDBModel> clients) {
        this.clients = clients;
    }

    @Override
    public int getRowCount() {
        return clients.size();
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
        CustomerCardDBModel client = clients.get(rowIndex);

        // Map each column index to the correct Client field
        return switch (columnIndex) {
            case 0 -> client.getCard_number();
            case 1 -> client.getCust_surname();
            case 2 -> client.getCust_name();

            // Handle patronymic (it can sometimes be null or empty in real life)
            case 3 -> (client.getCust_patronymic() != null) ? client.getCust_patronymic() : "";

            case 4 -> client.getPhone_number();

            // 🔥 THE MAGIC: Combine city, street, and zip into one clean Address string
            case 5 -> formatAddress(client.getCity(), client.getStreet(), client.getZip_code());

            // Add a nice % sign to the integer for the UI
            case 6 -> client.getPercent() + " %";

            default -> null;
        };
    }

    /**
     * Helper method to format the address nicely, handling potential nulls.
     */
    private String formatAddress(String city, String street, String zip) {
        String address = "";
        if (city != null && !city.isEmpty()) address += "м. " + city;
        if (street != null && !street.isEmpty()) address += (address.isEmpty() ? "" : ", ") + street;
        if (zip != null && !zip.isEmpty()) address += (address.isEmpty() ? "" : ", ") + zip;

        return address.isEmpty() ? "Не вказано" : address;
    }

    /**
     * Grabs the full Client object instantly when a row is clicked.
     */
    public CustomerCardDBModel getClientAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < clients.size()) {
            return clients.get(rowIndex);
        }
        return null;
    }

    /**
     * Use this to refresh the table with new search results (e.g., when
     * searching by discount percentage or surname).
     */
    public void setClients(List<CustomerCardDBModel> newClients) {
        this.clients.clear();
        if (newClients != null) {
            this.clients.addAll(newClients);
        }
        fireTableDataChanged(); // Tells JTable to re-render
    }
}
