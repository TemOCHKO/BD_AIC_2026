package Mains;

import DAO.CategoryDao;
import models.Category;
import org.aic.DBConnection;

import java.sql.Connection;

public class CategoryMain {
    public static void main(String[] args) throws Exception {

        Connection connection = DBConnection.getConnection();

        CategoryDao dao = new CategoryDao(connection);

        // 1. показати всі категорії
        System.out.println("=== Всі категорії ===");

        dao.getAllCategories().forEach(c ->
                System.out.println(c.getCategory_number() + " | " + c.getCategory_name())
        );

        // 2. додати нову категорію
        /*Category newCategory = new Category(0, "Молочні продукти");
        dao.addCategory(newCategory);
        System.out.println("Категорію додано!");*/

        // 3. всі категорії відсортовані за назвою
        System.out.println("=== Категорії відсортовані за назвою ===");

        dao.getAllCategoriesSortedByName().forEach(c ->
                System.out.println(c.getCategory_number() + " | " + c.getCategory_name())
        );

        // 4. оновити категорію
        boolean updated = dao.updateCategory(3, "Хлібобулочні вироби");
        if (updated) {
            System.out.println("Категорію оновлено!");
        } else {
            System.out.println("Категорію не знайдено!");
        }

        // 5. перевірити після оновлення
        System.out.println("=== Після оновлення ===");

        dao.getAllCategories().forEach(c ->
                System.out.println(c.getCategory_number() + " | " + c.getCategory_name())
        );

        // 6. видалити категорію
       /* boolean deleted = dao.deleteCategory(3);
        if (deleted) {
            System.out.println("Категорію видалено!");
        } else {
            System.out.println("Категорію не знайдено!");
        }

        // 7. перевірити після видалення
        System.out.println("=== Після видалення ===");

        dao.getAllCategories().forEach(c ->
                System.out.println(c.getCategory_number() + " | " + c.getCategory_name())
        );

        connection.close();*/
    }
}