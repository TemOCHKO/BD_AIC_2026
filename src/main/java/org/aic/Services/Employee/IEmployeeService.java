package org.aic.Services.Employee;

import org.aic.DBModels.EmployeeDBModel;
import org.aic.DBModels.ProductDBModel;
import org.aic.DTOModels.Employee.EmployeeListDTO;
import org.aic.DTOModels.ProductTableDTO;

import java.sql.SQLException;
import java.util.List;

public interface IEmployeeService {
    List<EmployeeDBModel> getAllEmployees();
    List<EmployeeDBModel> getOnlyCashiers();
    List<EmployeeDBModel> getAllEmployeesSortedBySurname();
    Iterable<EmployeeListDTO> getAllListEmployeesDTO();
    EmployeeDBModel getEmployeeBySurname(String name);
    EmployeeDBModel getEmployeeById(String id);
    void saveNewEmployee(EmployeeDBModel employeeDBModel);
    void updateEmployee(EmployeeDBModel employeeDBModel);
    void deleteEmployee(String id);
}
