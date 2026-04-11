package org.aic.DBModels;

public class CustomerCardDBModel {

    private int card_number;
    private String cust_surname;
    private String cust_name;
    private String cust_patronymic;
    private String phone_number;
    private String city;
    private String street;
    private String zip_code;
    private double percent;

    public CustomerCardDBModel(int card_number, String cust_name, String cust_surname,
                         String cust_patronymic, String phone_number, String city,
                         String street, String zip_code, double percent) {
        this.card_number = card_number;
        this.cust_name = cust_name;
        this.cust_surname = cust_surname;
        this.cust_patronymic = cust_patronymic;
        this.phone_number = phone_number;
        this.city = city;
        this.street = street;
        this.zip_code = zip_code;
        this.percent = percent;
    }
    public int getCard_number() {
        return card_number;
    }
    public String getCust_name() {
        return cust_name;
    }
    public String getCust_surname() {
        return cust_surname;
    }
    public String getCust_patronymic() {
        return cust_patronymic;
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
    public double getPercent() {
        return percent;
    }

    public void setCard_number(int card_number) {
        this.card_number = card_number;
    }
    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }
    public void setCust_surname(String cust_surname) {
        this.cust_surname = cust_surname;
    }
    public void setCust_patronymic(String cust_patronymic) {
        this.cust_patronymic = cust_patronymic;
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
    public void setPercent(double percent) {
        this.percent = percent;
    }
}
