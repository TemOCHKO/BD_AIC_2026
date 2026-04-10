package DAO;

import models.Check;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckDao {
    private Connection connection;

    public CheckDao(Connection connection) {
        this.connection = connection;
    }
    public void addCheck(Check check) throws SQLException {
        String sql = "INSERT INTO Check VALUES(?,?,?,?,?)";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, check.getId_employee());
        stmt.setString(2, check.getCard_number());
        stmt.setString(3, check.getPrint_date());
        stmt.setDouble(4, check.getSum_total());
        stmt.setDouble(5, check.getVat());

        stmt.executeUpdate();
    }

    public List<Check> getAllChecks() throws SQLException {
        List<Check> list = new ArrayList<>();

        String sql = "SELECT * FROM Check";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Check c = new Check(
                    rs.getInt("id"),
                    rs.getString("Id_employee"),
                    rs.getString("card number"),
                    rs.getString("date"),
                    rs.getDouble("sum total"),
                    rs.getDouble("vat")
            );
            list.add(c);
        }
        return list;
    }
}
