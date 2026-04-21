package org.aic.UI.Views.TableModels;

import org.aic.DBModels.EmployeeDBModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class EmployeeFullTableModel extends AbstractTableModel {

    private final List<EmployeeDBModel> employees;

    private final String[] columnNames = {
            "ID", "Прізвище", "Ім'я", "По батькові", "Посада", "Зарплата",
            "Дата нар.", "Дата початку", "Телефон", "Місто", "Вулиця", "Індекс"
    };

    public EmployeeFullTableModel(List<EmployeeDBModel> employees) {
        this.employees = employees;
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
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EmployeeDBModel emp = employees.get(rowIndex);

        // Map each column index to the correct Employee property
        return switch (columnIndex) {
            case 0 -> emp.getId_employee();
            case 1 -> emp.getEmpl_surname();
            case 2 -> emp.getEmpl_name();
            case 3 -> emp.getEmpl_patronymic();
            case 4 -> emp.getEmpl_role();
            case 5 -> emp.getSalary();
            case 6 -> emp.getDate_of_birth();
            case 7 -> emp.getDate_of_start();
            case 8 -> emp.getPhone_number();
            case 9 -> emp.getCity();
            case 10 -> emp.getStreet();
            case 11 -> emp.getZip_code();
            default -> null;
        };
    }

    public EmployeeDBModel getEmployeeAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < employees.size()) {
            return employees.get(rowIndex);
        }
        return null;
    }


    public void setEmployees(List<EmployeeDBModel> newEmployees) {
        this.employees.clear();
        if (newEmployees != null) {
            this.employees.addAll(newEmployees);
        }
        fireTableDataChanged();
    }
}