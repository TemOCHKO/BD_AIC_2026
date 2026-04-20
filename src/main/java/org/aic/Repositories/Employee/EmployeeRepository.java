package org.aic.Repositories.Employee;

import org.aic.DBModels.EmployeeDBModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeRepository implements IEmployeeRepository {
    private Connection connection;

    public EmployeeRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * додати працівника
     * @param emp
     * @throws SQLException
     */
    public void addEmployee(EmployeeDBModel emp) throws SQLException {
        String sql = "INSERT INTO Employee " +
                "(id_employee, empl_surname, empl_name, empl_patronymic, " +
                "empl_role, salary, date_of_birth, date_of_start, " +
                "phone_number, city, street, zip_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, emp.getId_employee());
        stmt.setString(2, emp.getEmpl_surname());
        stmt.setString(3, emp.getEmpl_name());
        stmt.setString(4, emp.getEmpl_patronymic());
        stmt.setString(5, emp.getEmpl_role());
        stmt.setDouble(6, emp.getSalary());
        stmt.setString(7, emp.getDate_of_birth());
        stmt.setString(8, emp.getDate_of_start());
        stmt.setString(9, emp.getPhone_number());
        stmt.setString(10, emp.getCity());
        stmt.setString(11, emp.getStreet());
        stmt.setString(12, emp.getZip_code());

        stmt.executeUpdate();

    }

    /**
     * видалити працівника
     */
    public void deleteEmployee(String id) throws SQLException {
        String sql = "DELETE FROM Employee WHERE id_employee = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, id);
        stmt.executeUpdate();
    }

    /**
     * отримати всіх працівників
     * (відсортованих за прізвищем)
     */
    public List<EmployeeDBModel> getAllEmployees() throws SQLException {
        List<EmployeeDBModel> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee ORDER BY empl_surname";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            employees.add(mapEmployee(rs));
        }
        return employees;
    }

    /**
     * отримати ТІЛЬКИ касирів
     * (відсортованих за прізвищем)
     */
    public List<EmployeeDBModel> getAllCashiers() throws SQLException {
        List<EmployeeDBModel> cashiers = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE empl_role = 'Cashier' ORDER BY empl_surname";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            cashiers.add(mapEmployee(rs));
        }
        return cashiers;
    }
    /**
     * отримати працівника за прізвищем
     */
    public EmployeeDBModel getEmplBySurname(String surname) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE empl_surname = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, surname);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            return mapEmployee(rs);
        }
        return null;
    }

    @Override
    public EmployeeDBModel getEmplById(String id) throws SQLException {
        return null;
    }

    /**
     * отримати працівника за id
     */
    public EmployeeDBModel getEmplByI(String id) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE id_employee = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            return mapEmployee(rs);
        }
        return null;
    }

    /**
     * оновити працівника
     */
    public void updateEmployee(EmployeeDBModel emp) throws SQLException {
        String sql = "UPDATE Employee SET " +
                "empl_surname = ?, empl_name = ?, empl_patronymic = ?, " +
                "empl_role = ?, salary = ?, date_of_birth = ?, date_of_start = ?, " +
                "phone_number = ?, city = ?, street = ?, zip_code = ? " +
                "WHERE id_employee = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, emp.getEmpl_surname());
        stmt.setString(2, emp.getEmpl_name());
        stmt.setString(3, emp.getEmpl_patronymic());
        stmt.setString(4, emp.getEmpl_role());
        stmt.setDouble(5, emp.getSalary());
        stmt.setString(6, emp.getDate_of_birth());
        stmt.setString(7, emp.getDate_of_start());
        stmt.setString(8, emp.getPhone_number());
        stmt.setString(9, emp.getCity());
        stmt.setString(10, emp.getStreet());
        stmt.setString(11, emp.getZip_code());
        stmt.setString(12, emp.getId_employee());
        stmt.executeUpdate();
    }

    private EmployeeDBModel mapEmployee(ResultSet rs) throws SQLException {
        return new EmployeeDBModel(
                rs.getString("id_employee"),
                rs.getString("empl_surname"),
                rs.getString("empl_name"),
                rs.getString("empl_patronymic"),
                rs.getString("empl_role"),
                rs.getDouble("salary"),
                rs.getString("date_of_birth"),
                rs.getString("date_of_start"),
                rs.getString("phone_number"),
                rs.getString("city"),
                rs.getString("street"),
                rs.getString("zip_code")
        );
    }
    //пошук за прізвищем
    public List<EmployeeDBModel> getEmployeeBySurname(String surname) throws SQLException {
        List<EmployeeDBModel> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE empl_surname = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, surname);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new EmployeeDBModel(
                    rs.getString("id_employee"),
                    rs.getString("empl_surname"),
                    rs.getString("empl_name"),
                    rs.getString("empl_patronymic"),
                    rs.getString("empl_role"),
                    rs.getDouble("salary"),
                    rs.getString("date_of_birth"),
                    rs.getString("date_of_start"),
                    rs.getString("phone_number"),
                    rs.getString("city"),
                    rs.getString("street"),
                    rs.getString("zip_code")
            ));
        }
        return list;
    }
    /**
     * коли касир заходить у систему - має бачити свої дані
     */
    public EmployeeDBModel getEmployeeById(String id) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE id_employee = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new EmployeeDBModel(
                    rs.getString("id_employee"),
                    rs.getString("empl_surname"),
                    rs.getString("empl_name"),
                    rs.getString("empl_patronymic"),
                    rs.getString("empl_role"),
                    rs.getDouble("salary"),
                    rs.getString("date_of_birth"),
                    rs.getString("date_of_start"),
                    rs.getString("phone_number"),
                    rs.getString("city"),
                    rs.getString("street"),
                    rs.getString("zip_code")
            );
        }
        return null;
    }

    @Override
    public void saveNewEmployee(EmployeeDBModel employeeDBModel) throws SQLException {
        String newId = "EMP-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        String sql = "INSERT INTO employee(id_employee, empl_surname, empl_name, empl_patronymic, empl_role, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, newId);
        stmt.setString(2, employeeDBModel.getEmpl_surname());
        stmt.setString(3, employeeDBModel.getEmpl_name());
        stmt.setString(4, employeeDBModel.getEmpl_patronymic());
        stmt.setString(5, employeeDBModel.getEmpl_role());
        stmt.setDouble(6, employeeDBModel.getSalary());
        stmt.setString(7, employeeDBModel.getDate_of_birth());
        stmt.setString(8, employeeDBModel.getDate_of_start());
        stmt.setString(9, employeeDBModel.getPhone_number());
        stmt.setString(10, employeeDBModel.getCity());
        stmt.setString(11, employeeDBModel.getStreet());
        stmt.setString(12, employeeDBModel.getZip_code());

        stmt.execute();
    }
}
