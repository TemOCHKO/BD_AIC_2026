package org.aic.DTOModels.Employee;

import java.util.UUID;

public class EmployeeListDTO {
    private String dbIdEmployee;
    private UUID id;
    private String surname;
    private String name;
    private String role;
    private String dateOfBirth;

    public EmployeeListDTO(String dbIdEmployee, String surname, String name, String role, String dateOfBirth) {
        this.dbIdEmployee = dbIdEmployee;
        this.id = UUID.randomUUID();
        this.surname = surname;
        this.name = name;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters
    public UUID getId() { return  id; }
    public String getSurname() { return surname; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getDateOfBirth() { return dateOfBirth; }

    public String getDbIdEmployee() {
        return dbIdEmployee;
    }
}
