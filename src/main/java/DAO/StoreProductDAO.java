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

    // 2. Отримати ВСІ товари, відсортовані за кількістю
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

    // 3. Пошук АКЦІЙНИХ товарів
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

    // 5. Не акційні товари відсортовані за кількістю
    public List<Store_Product> getNonPromotionalProducts() throws SQLException {
        List<Store_Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Store_Product WHERE promotional_product = false ORDER BY products_number ASC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) list.add(mapStoreProduct(rs));
        return list;
    }
    // Сортування за назвою
    public List<Store_Product> getAllSortedByName() throws SQLException {
        List<Store_Product> list = new ArrayList<>();
        String sql = "SELECT sp.*, p.product_name FROM Store_Product sp " +
                "JOIN Product p ON sp.id_product = p.id_product " +
                "ORDER BY p.product_name ASC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) list.add(mapStoreProduct(rs));
        return list;
    }
    // 6. Акційні відсортовані за назвою
    public List<Store_Product> getPromotionalSortedByName() throws SQLException {
        List<Store_Product> list = new ArrayList<>();
        String sql = "SELECT sp.*, p.product_name FROM Store_Product sp " +
                "JOIN Product p ON sp.id_product = p.id_product " +
                "WHERE sp.promotional_product = true ORDER BY p.product_name ASC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) list.add(mapStoreProduct(rs));
        return list;
    }

    // 7. Не акційні відсортовані за назвою
    public List<Store_Product> getNonPromotionalSortedByName() throws SQLException {
        List<Store_Product> list = new ArrayList<>();
        String sql = "SELECT sp.*, p.product_name FROM Store_Product sp " +
                "JOIN Product p ON sp.id_product = p.id_product " +
                "WHERE sp.promotional_product = false ORDER BY p.product_name ASC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) list.add(mapStoreProduct(rs));
        return list;
    }
    // 8. За UPC знайти ціну, к-сть, назву, характеристики
    public Store_Product getByUpc(String upc) throws SQLException {
        String sql = "SELECT sp.*, p.product_name, p.characteristics FROM Store_Product sp " +
                "JOIN Product p ON sp.id_product = p.id_product " +
                "WHERE sp.upc = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, upc);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapStoreProduct(rs);
        }
        return null;
    }

    // 9. Оновлення товару в магазині
    public boolean updateStoreProduct(Store_Product sp) throws SQLException {
        double price = Double.parseDouble(sp.getSelling_price());
        if (sp.getPromotional_product()) price = price * 0.8;

        String sql = "UPDATE Store_Product SET upc_prom = ?, id_product = ?, " +
                "selling_price = ?, products_number = ?, promotional_product = ? " +
                "WHERE upc = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sp.getUPC_prom());
            stmt.setInt(2, sp.getId_product());
            stmt.setDouble(3, price);
            stmt.setInt(4, sp.getProducts_number());
            stmt.setBoolean(5, sp.getPromotional_product());
            stmt.setString(6, sp.getUPC());
            return stmt.executeUpdate() > 0;
        }
    }

    // 10. Видалення товару з магазину
    public boolean deleteStoreProduct(String upc) throws SQLException {
        String sql = "DELETE FROM Store_Product WHERE upc = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, upc);
            return stmt.executeUpdate() > 0;
        }
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