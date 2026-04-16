package org.aic.Repositories.Employee;

import org.aic.DBModels.EmployeeDBModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IEmployeeRepository {
    void addEmployee(EmployeeDBModel emp) throws SQLException;
    void deleteEmployee(String id) throws SQLException;
    List<EmployeeDBModel> getAllEmployees() throws SQLException;
    List<EmployeeDBModel> getAllCashiers() throws SQLException;
    EmployeeDBModel getEmplBySurname(String surname) throws SQLException;
    EmployeeDBModel getEmplById(String id) throws SQLException;
    void updateEmployee(EmployeeDBModel emp) throws SQLException;
    List<EmployeeDBModel> getEmployeeBySurname(String surname) throws SQLException;
    EmployeeDBModel getEmployeeById(String id) throws SQLException;

    void saveNewEmployee(EmployeeDBModel employeeDBModel) throws SQLException;
}
