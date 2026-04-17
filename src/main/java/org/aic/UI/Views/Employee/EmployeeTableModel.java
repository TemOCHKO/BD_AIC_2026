package org.aic.UI.Views.Employee;

import org.aic.DTOModels.Employee.EmployeeListDTO;
import org.aic.DTOModels.ProductTableDTO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {
    // 1. Define the column headers
    private String[] columnNames = {"ID", "Surname", "Name", "Role", "Date of Birth"};
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
    public String getColumnName(int column) {
        return columnNames[column];
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
        EmployeeListDTO employee = employees.get(rowIndex);

        return switch (columnIndex) {
            case -1 -> employee.getDbIdEmployee();
            case 0 -> employee.getId().toString().substring(0, 4);
            case 1 -> employee.getSurname();
            case 2 -> employee.getName();
            case 3 -> employee.getRole();
            case 4 -> employee.getDateOfBirth();
            default -> null;
        };
    }
}
