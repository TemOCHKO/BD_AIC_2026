package org.aic.Repositories.CustomerCard;

import org.aic.DBModels.CustomerCardDBModel;
import org.aic.DBModels.EmployeeDBModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerCardRepository implements ICustomerCardRepository {
    private Connection connection;

    public CustomerCardRepository(Connection connection) {
        this.connection = connection;
    }

    public void addCustomerCard(CustomerCardDBModel c) throws SQLException {
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
    public List<CustomerCardDBModel> getAllCustomerCards() throws SQLException {
        List<CustomerCardDBModel> list = new ArrayList<>();

        String sql = "SELECT * FROM Customer_Card";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            CustomerCardDBModel c = new CustomerCardDBModel(
                    rs.getString("card_number"),
                    rs.getString("cust_name"),
                    rs.getString("cust_surname"),
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
    public List<CustomerCardDBModel> getCustomerBySurname(String surname) throws SQLException {
        List<CustomerCardDBModel> list = new ArrayList<>();
        // Просто шукаємо точний збіг прізвища
        String sql = "SELECT * FROM Customer_Card WHERE cust_surname = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, surname);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new CustomerCardDBModel(
                    rs.getString("card_number"),
                    rs.getString("cust_name"),
                    rs.getString("cust_surname"),
                    rs.getString("cust_patronymic"),
                    rs.getString("phone_number"),
                    rs.getString("city"),
                    rs.getString("street"),
                    rs.getString("zip_code"),
                    rs.getInt("percent")
            ));
        }
        return list;
    }

    @Override
    public CustomerCardDBModel getCustomerByCardNumber(String cardNumber) throws SQLException {
        String sql = "SELECT * FROM Customer_Card WHERE card_number = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, cardNumber);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            return mapCustomer(rs);
        }
        return null;
    }

    public List<CustomerCardDBModel> getCustomersByPercent(int percent) throws SQLException {
        List<CustomerCardDBModel> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer_Card WHERE percent = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, percent);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new CustomerCardDBModel(
                    rs.getString("card_number"),
                    rs.getString("cust_name"),
                    rs.getString("cust_surname"),
                    rs.getString("cust_patronymic"),
                    rs.getString("phone_number"),
                    rs.getString("city"),
                    rs.getString("street"),
                    rs.getString("zip_code"),
                    rs.getInt("percent")
            ));
        }
        return list;
    }
    public List<CustomerCardDBModel> getAllCustomerCardsSorted() throws SQLException {
        List<CustomerCardDBModel> list = new ArrayList<>();
        // Просто вибираємо всіх і кажемо базі посортувати
        String sql = "SELECT * FROM Customer_Card ORDER BY cust_surname ASC";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(new CustomerCardDBModel(
                    rs.getString("card_number"),
                    rs.getString("cust_name"),
                    rs.getString("cust_surname"),
                    rs.getString("cust_patronymic"),
                    rs.getString("phone_number"),
                    rs.getString("city"),
                    rs.getString("street"),
                    rs.getString("zip_code"),
                    rs.getInt("percent")
            ));
        }
        return list;
    }
    // Оновлення карти клієнта
    public boolean updateCustomerCard(CustomerCardDBModel c) throws SQLException {
        String sql = "UPDATE Customer_Card SET cust_surname = ?, cust_name = ?, cust_patronymic = ?, " +
                "phone_number = ?, city = ?, street = ?, zip_code = ?, percent = ? " +
                "WHERE card_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, c.getCust_surname());
            stmt.setString(2, c.getCust_name());
            stmt.setString(3, c.getCust_patronymic());
            stmt.setString(4, c.getPhone_number());
            stmt.setString(5, c.getCity());
            stmt.setString(6, c.getStreet());
            stmt.setString(7, c.getZip_code());
            stmt.setInt(8, c.getPercent());
            stmt.setString(9, c.getCard_number());
            return stmt.executeUpdate() > 0;
        }
    }

    private CustomerCardDBModel mapCustomer(ResultSet rs) throws SQLException {
        return new CustomerCardDBModel(
                rs.getString("card_number"),
                rs.getString("cust_name"),
                rs.getString("cust_surname"),
                rs.getString("cust_patronymic"),
                rs.getString("phone_number"),
                rs.getString("city"),
                rs.getString("street"),
                rs.getString("zip_code"),
                rs.getInt("percent")
        );
    }
}
