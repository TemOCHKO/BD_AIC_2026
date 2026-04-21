package org.aic.DBModels;

public class EmployeeDBModel {
    private String id_employee;
    private String empl_surname;
    private String empl_name;
    private String empl_patronymic;
    private String empl_role;
    private double salary;
    private String date_of_birth;
    private String date_of_start;
    private String phone_number;
    private String city;
    private String street;
    private String zip_code;
    private String empl_password;

    public EmployeeDBModel(String id_employee, String empl_surname, String empl_name,
                    String empl_patronymic, String empl_role, double salary,
                    String date_of_birth, String date_of_start, String phone_number,
                    String city, String street, String zipcode, String empl_password) {
        this.id_employee = id_employee;
        this.empl_surname = empl_surname;
        this.empl_name = empl_name;
        this.empl_patronymic = empl_patronymic;
        this.empl_role = empl_role;
        this.salary = salary;
        this.date_of_birth = date_of_birth;
        this.date_of_start = date_of_start;
        this.phone_number = phone_number;
        this.city = city;
        this.street = street;
        this.zip_code = zipcode;
        this.empl_password = empl_password;
    }

    public EmployeeDBModel(String empl_surname, String empl_name,
                           String empl_patronymic, String empl_role, double salary,
                           String date_of_birth, String date_of_start, String phone_number,
                           String city, String street, String zipcode, String empl_password) {
        this("defaultId", empl_surname, empl_name, empl_patronymic, empl_role, salary, date_of_birth, date_of_start, phone_number, city, street, zipcode, empl_password);
    }


    public String getId_employee() {
        return id_employee;
    }
    public String getEmpl_surname() {
        return empl_surname;
    }
    public String getEmpl_name() {
        return empl_name;
    }
    public String getEmpl_patronymic() {
        return empl_patronymic;
    }
    public String getEmpl_role() {
        return empl_role;
    }
    public double getSalary() {
        return salary;
    }
    public String getDate_of_birth() {
        return date_of_birth;
    }
    public String getDate_of_start() {
        return date_of_start;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public String getCity() {
        return city;
    }
    public String getStreet() {
        return street;
    }
    public String getZip_code() {
        return zip_code;
    }

    public void setId_employee(String id_employee) {
        this.id_employee = id_employee;
    }
    public void setEmpl_surname(String empl_surname) {
        this.empl_surname = empl_surname;
    }
    public void setEmpl_name(String empl_name) {
        this.empl_name = empl_name;
    }
    public void setEmpl_patronymic(String empl_patronymic) {
        this.empl_patronymic = empl_patronymic;
    }
    public void setEmpl_role(String empl_role) {
        this.empl_role = empl_role;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    public void setDate_of_start(String date_of_start) {
        this.date_of_start = date_of_start;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    @Override
    public String toString() {
        return "models.Employee: " + id_employee + " " + empl_surname + " " + empl_name+ "(" + empl_role + ")";
    }

    public String getEmpl_password() {
        return empl_password;
    }

    public void setEmpl_password(String empl_password) {
        this.empl_password = empl_password;
    }
}
