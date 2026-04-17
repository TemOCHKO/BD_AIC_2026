package org.aic.Services.Employee;

import org.aic.DBModels.EmployeeDBModel;
import org.aic.DTOModels.Employee.EmployeeListDTO;
import org.aic.DTOModels.ProductTableDTO;
import org.aic.Repositories.Employee.IEmployeeRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;

    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Iterable<EmployeeDBModel> getAllEmployees() {
        try {
            return employeeRepository.getAllEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<EmployeeListDTO> getAllListEmployeesDTO() {
        var employees = getAllEmployees();
        var dtos = new ArrayList<EmployeeListDTO>();

        for (var empl : employees) {
            dtos.add(new EmployeeListDTO(empl.getId_employee(), empl.getEmpl_surname(), empl.getEmpl_name(), empl.getEmpl_role(), empl.getDate_of_birth()));
        }

        return dtos;
    }

    @Override
    public EmployeeDBModel getEmployeeBySurname(String surname) {
        try {
            return employeeRepository.getEmplBySurname(surname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EmployeeDBModel getEmployeeById(String id) {
        try {
            return employeeRepository.getEmployeeById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveNewEmployee(EmployeeDBModel employeeDBModel) {
        try {
            employeeRepository.saveNewEmployee(employeeDBModel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmployee(EmployeeDBModel employeeDBModel) {
        try {
            employeeRepository.updateEmployee(employeeDBModel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEmployee(String id) {
        try {
            employeeRepository.deleteEmployee(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
