package org.aic.DTOModels.Employee;

public class EmployeeListDTO {
    private String surname;
    private String name;
    private String role;
    private String dateOfBirth;

    public EmployeeListDTO(String surname, String name, String role, String dateOfBirth) {
        this.surname = surname;
        this.name = name;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters
    public String getSurname() { return surname; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getDateOfBirth() { return dateOfBirth; }
}
