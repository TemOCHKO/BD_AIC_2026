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
        String sql = "INSERT INTO CustomerCard(surname, name, patronymic, phone, city, street, zip_code, percent) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, c.getCust_surname());
        stmt.setString(2, c.getCust_name());
        stmt.setString(3, c.getCust_patronymic());
        stmt.setString(4, c.getPhone_number());
        stmt.setString(5, c.getCity());
        stmt.setString(6, c.getStreet());
        stmt.setString(7, c.getZip_code());
        stmt.setDouble(8, c.getPercent());

        stmt.executeUpdate();

    }
    public List<Customer_card> getAllCustomerCards() throws SQLException {
        List<Customer_card> list = new ArrayList<>();

        String sql = "SELECT * FROM CustomerCard";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Customer_card c = new Customer_card(
                    rs.getInt("id"),
                    rs.getString("surname"),
                    rs.getString("name"),
                    rs.getString("patronymic"),
                    rs.getString("phone"),
                    rs.getString("city"),
                    rs.getString("street"),
                    rs.getString("zip_code"),
                    rs.getDouble("percent")
            );

            list.add(c);
        }

        return list;
    }
}
