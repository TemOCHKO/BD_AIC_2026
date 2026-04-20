package org.aic.UI.Views.TableModels;

import org.aic.DBModels.EmployeeDBModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;
// import org.aic.Models.Employee; // Don't forget to import your Employee class!

public class EmployeeFullTableModel extends AbstractTableModel {

    private final List<EmployeeDBModel> employees;

    // The exact columns from your ManagerFrame
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
            case 0 -> emp.getId_employee();                // ID
            case 1 -> emp.getEmpl_surname();           // Прізвище
            case 2 -> emp.getEmpl_name();      // Ім'я
            case 3 -> emp.getEmpl_patronymic();        // По батькові
            case 4 -> emp.getEmpl_role();   // Посада (e.g., "Касир", "Менеджер")
            case 5 -> emp.getSalary();            // Зарплата
            case 6 -> emp.getDate_of_birth();       // Дата нар.
            case 7 -> emp.getDate_of_start();      // Дата початку
            case 8 -> emp.getPhone_number();      // Телефон
            case 9 -> emp.getCity();              // Місто
            case 10 -> emp.getStreet();           // Вулиця
            case 11 -> emp.getZip_code();     // Індекс
            default -> null;
        };
    }

    /**
     * The magic method! Call this from your Controller to instantly get
     * the full Employee object when a user clicks a row.
     */
    public EmployeeDBModel getEmployeeAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < employees.size()) {
            return employees.get(rowIndex);
        }
        return null;
    }

    /**
     * Optional: If you need to update the table data without creating a new model,
     * you can add a method to swap the list and tell the UI to refresh.
     */
    public void setEmployees(List<EmployeeDBModel> newEmployees) {
        this.employees.clear();
        if (newEmployees != null) {
            this.employees.addAll(newEmployees);
        }
        fireTableDataChanged(); // Tells the JTable to redraw itself
    }
}