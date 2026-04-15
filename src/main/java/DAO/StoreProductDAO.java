package DAO;

import models.Store_Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreProductDAO {
    private Connection connection;

    public StoreProductDAO(Connection conn) {
        this.connection = conn;
    }

    // 1. Додавання товару з автоматичним розрахунком акційної ціни
    public void addStoreProduct(Store_Product sp) throws SQLException {
        // Якщо товар акційний, ціна стає на 20% меншою (множимо на 0.8)
        double price = Double.parseDouble(sp.getSelling_price());
        if (sp.getPromotional_product()) {
            price = price * 0.8;
        }

        String sql = "INSERT INTO Store_Product (upc, upc_prom, id_product, selling_price, products_number, promotional_product) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, sp.getUPC());
        stmt.setString(2, sp.getUPC_prom());
        stmt.setInt(3, sp.getId_product());
        stmt.setDouble(4, price); // Записуємо вже оброблену ціну
        stmt.setInt(5, sp.getProducts_number());
        stmt.setBoolean(6, sp.getPromotional_product());
        stmt.executeUpdate();
    }

    // 2. Отримати ВСІ товари, відсортовані за кількістю (Вимога менеджера №10)
    public List<Store_Product> getAllSortedByNumber() throws SQLException {
        List<Store_Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Store_Product ORDER BY products_number DESC";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(mapStoreProduct(rs));
        }
        return list;
    }

    // 3. Пошук АКЦІЙНИХ товарів (Вимога №15)
    public List<Store_Product> getPromotionalProducts() throws SQLException {
        List<Store_Product> list = new ArrayList<>();
        // Тільки ті, де promotional_product = true
        String sql = "SELECT * FROM Store_Product WHERE promotional_product = true ORDER BY products_number ASC";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(mapStoreProduct(rs));
        }
        return list;
    }

    // 4. ПЕРЕОЦІНКА: Коли приходить нова партія (Вимога 2.1.2.1)
    // Якщо ціна змінюється, вона змінюється для всіх одиниць цього товару
    public void updateProductPrice(int idProduct, double newPrice) throws SQLException {
        // Оновлюємо ціну для всіх звичайних товарів з цим ID
        String sqlNormal = "UPDATE Store_Product SET selling_price = ? WHERE id_product = ? AND promotional_product = false";
        // Оновлюємо ціну для акційних (нова ціна - 20%)
        String sqlPromo = "UPDATE Store_Product SET selling_price = ? * 0.8 WHERE id_product = ? AND promotional_product = true";

        PreparedStatement st1 = connection.prepareStatement(sqlNormal);
        st1.setDouble(1, newPrice);
        st1.setInt(2, idProduct);
        st1.executeUpdate();

        PreparedStatement st2 = connection.prepareStatement(sqlPromo);
        st2.setDouble(1, newPrice);
        st2.setInt(2, idProduct);
        st2.executeUpdate();
    }

    // Допоміжний метод, щоб не дублювати код створення об'єкта
    private Store_Product mapStoreProduct(ResultSet rs) throws SQLException {
        return new Store_Product(
                rs.getString("upc"),
                rs.getString("upc_prom"),
                rs.getInt("id_product"),
                String.valueOf(rs.getDouble("selling_price")),
                rs.getInt("products_number"),
                rs.getBoolean("promotional_product")
        );
    }
}