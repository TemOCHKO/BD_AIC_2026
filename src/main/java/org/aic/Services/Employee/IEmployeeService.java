package org.aic.Services.Employee;

import org.aic.DBModels.EmployeeDBModel;
import org.aic.DBModels.ProductDBModel;
import org.aic.DTOModels.Employee.EmployeeListDTO;
import org.aic.DTOModels.ProductTableDTO;

public interface IEmployeeService {
    Iterable<EmployeeDBModel> getAllEmployees();
    Iterable<EmployeeListDTO> getAllListEmployeesDTO();
    EmployeeDBModel getEmployeeBySurname(String name);
    EmployeeDBModel getEmployeeById(String id);
    void saveNewEmployee(EmployeeDBModel employeeDBModel);
}
