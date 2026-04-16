package org.aic.UI.Views.Employee;

import org.aic.DTOModels.Employee.EmployeeListDTO;
import org.aic.DTOModels.ProductTableDTO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {
    // 1. Define the column headers
    private String[] columnNames = {"Surname", "Name", "Role", "Date of Birth"};
    private List<EmployeeListDTO> employees;

    public EmployeeTableModel() {
        this.employees = new ArrayList<>();
    }

    public void setProducts(List<EmployeeListDTO> employees) {
        this.employees = employees;

        // Redraws the screen
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EmployeeListDTO product = employees.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> product.getId().toString().substring(0, 4);
            case 1 -> product.getName();
            case 2 -> product.getRole();
            case 3 -> product.getDateOfBirth();
            default -> null;
        };
    }
}
