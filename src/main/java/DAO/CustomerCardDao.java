package DAO;

import models.Customer_card;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerCardDao {
    private Connection connection;

    public CustomerCardDao(Connection connection) {
        this.connection = connection;
    }

    public void addCustomerCard(Customer_card c) throws SQLException {
        String sql = "INSERT INTO Customer_Card(card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, c.getCard_number());
        stmt.setString(2, c.getCust_surname());
        stmt.setString(3, c.getCust_name());
        stmt.setString(4, c.getCust_patronymic());
        stmt.setString(5, c.getPhone_number());
        stmt.setString(6, c.getCity());
        stmt.setString(7, c.getStreet());
        stmt.setString(8, c.getZip_code());
        stmt.setInt(9, c.getPercent());

        stmt.executeUpdate();

    }
    public List<Customer_card> getAllCustomerCards() throws SQLException {
        List<Customer_card> list = new ArrayList<>();

        String sql = "SELECT * FROM Customer_Card";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Customer_card c = new Customer_card(
                    rs.getString("card_number"),
                    rs.getString("cust_surname"),
                    rs.getString("cust_name"),
                    rs.getString("cust_patronymic"),
                    rs.getString("phone_number"),
                    rs.getString("city"),
                    rs.getString("street"),
                    rs.getString("zip_code"),
                    rs.getInt("percent")
            );

            list.add(c);
        }

        return list;
    }
    public double calculateDiscountedSum(String cardNumber, double originalSum) throws SQLException {
        String sql = "SELECT percent FROM Customer_Card WHERE card_number = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, cardNumber);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int percent = rs.getInt("percent");
            double discount = originalSum * percent / 100.0;
            return originalSum - discount;
        }

        // якщо карта не знайдена - повертаємо оригінальну суму без знижки
        return originalSum;
    }
    public boolean deleteCustomerCard(String cardNumber) throws SQLException {
        String sql = "DELETE FROM Customer_Card WHERE card_number = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, cardNumber);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }
}
