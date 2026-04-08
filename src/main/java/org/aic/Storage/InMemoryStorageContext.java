package org.aic.Storage;

import org.aic.DBModels.CategoryDBModel;
import org.aic.DBModels.ProductDBModel;
import org.aic.DBModels.StoreProductDBModel;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStorageContext implements IStorageContext {

    private record CategoryRecord (int dbId, String name) {}
    private record ProductRecord (int dbId, String title, String manufacturer, String description, int categoryNumber)  {}
    private record StoreProductRecord (String upc, String upcSale, int productId, double price, int numberOfProducts, boolean promotionalProduct) {}

    // Lists will become obsolete once db appears
    private static ArrayList<CategoryRecord> categories = new ArrayList<>();
    private static ArrayList<ProductRecord> products = new ArrayList<>();
    private static ArrayList<StoreProductRecord> storeProducts = new ArrayList<>();


    // Mock storage (will be replaced by db soon)
    public InMemoryStorageContext() {

        //region Mock Storage
        // 1. Generate 5 Grocery ProductRecords
        ProductRecord product1 = new ProductRecord(1, "Whole Milk", "Dairy Farms Inc.", "1 Gallon Whole Milk", 0);
        ProductRecord product2 = new ProductRecord(2, "Sourdough Bread", "Local Bakery", "Freshly baked sourdough loaf", 3);
        ProductRecord product3 = new ProductRecord(3, "Free Range Eggs", "Happy Hens", "Grade A Large Eggs - 1 dozen", 0);
        ProductRecord product4 = new ProductRecord(4, "Fuji Apples", "Nature's Best", "Crisp and sweet Fuji apples", 1);
        ProductRecord product5 = new ProductRecord(5, "Medium Roast Coffee", "Morning Brew", "100% Arabica ground coffee", 6);

        // 3. Generate 7 new Mock Products using the Enum
        ProductRecord product6 = new ProductRecord(
                6, "Atlantic Salmon Fillet", "Ocean Catch", "Fresh farm-raised Atlantic salmon", 4
        );

        ProductRecord product7 = new ProductRecord(
                7, "Ground Beef 80/20", "Local Farms", "1 lb of 80% lean ground beef", 2
        );

        ProductRecord product8 = new ProductRecord(
                8, "Ibuprofen 200mg", "HealthPlus", "Pain reliever and fever reducer - 100 count", 8
        );

        ProductRecord product9 = new ProductRecord(
                9, "Ultra Strong Paper Towels", "CleanCo", "2-ply absorbent paper towels - 6 rolls", 9
        );

        ProductRecord product10 = new ProductRecord(
                10, "Adult Dry Dog Food", "Happy Paws", "Chicken and rice formula - 15 lb bag", 10
        );

        ProductRecord product11 = new ProductRecord(
                11, "100% Orange Juice", "Citrus Grove", "Pulp-free orange juice - 64 oz", 5
        );

        ProductRecord product12 = new ProductRecord(
                12, "Organic Baby Spinach", "Green Leaf Farms", "Pre-washed fresh baby spinach - 5 oz", 1
        );

        ProductRecord product13 = new ProductRecord(
                13, "Spaghetti Pasta", "Pasta Bella", "1 lb enriched dried spaghetti", 6
        );

        ProductRecord product14 = new ProductRecord(
                14, "Canned Black Beans", "Goya", "15.5 oz organic low-sodium black beans", 6
        );

        ProductRecord product15 = new ProductRecord(
                15, "Almond Milk", "NutriFarms", "Unsweetened vanilla almond milk - 64 oz", 0
        );

        ProductRecord product16 = new ProductRecord(
                16, "Butter Croissants", "Local Bakery", "Freshly baked butter croissants - 4 pack", 3
        );

        ProductRecord product17 = new ProductRecord(
                17, "Sparkling Water", "Bubly", "Lime flavor sparkling water - 12 pack", 5
        );

        ProductRecord product18 = new ProductRecord(
                18, "Concentrated Dish Soap", "Dawn", "Grease-fighting liquid dish soap - 16 oz", 6
        );

        ProductRecord product19 = new ProductRecord(
                19, "Clumping Cat Litter", "Tidy Cats", "Multi-cat clumping clay litter - 20 lb", 10
        );

        // Add them to a list
        products = new ArrayList<>(List.of(
                product1, product2, product3, product4, product5,
                product6, product7, product8, product9, product10, product11, product12,
                product13, product14, product15, product16, product17, product18, product19
        ));

        // 2. Generate 10 StoreProductRecords (Linking 2 variants/UPCs to each productId)
        // Milk Variants
        StoreProductRecord sp1 = new StoreProductRecord("UPC-MLK-101", null, 1, 3.99, 50, false);
        StoreProductRecord sp2 = new StoreProductRecord("UPC-MLK-102", "SALE-MLK-102", 1, 2.99, 15, true); // Nearing expiration sale

        // Bread Variants
        StoreProductRecord sp3 = new StoreProductRecord("UPC-BRD-201", null, 2, 4.50, 20, false);
        StoreProductRecord sp4 = new StoreProductRecord("UPC-BRD-202", "SALE-BRD-202", 2, 2.00, 5, true); // Day-old bread promo

        // Egg Variants
        StoreProductRecord sp5 = new StoreProductRecord("UPC-EGG-301", null, 3, 5.99, 30, false);
        StoreProductRecord sp6 = new StoreProductRecord("UPC-EGG-302", null, 3, 8.49, 12, false); // 18-count carton variant

        // Apple Variants
        StoreProductRecord sp7 = new StoreProductRecord("UPC-APP-401", null, 4, 1.99, 150, false); // Standard stock
        StoreProductRecord sp8 = new StoreProductRecord("UPC-APP-402", "SALE-APP-402", 4, 0.99, 45, true); // Blemished/Discounted bag

        // Coffee Variants
        StoreProductRecord sp9 = new StoreProductRecord("UPC-COF-501", null, 5, 12.99, 25, false);
        StoreProductRecord sp10 = new StoreProductRecord("UPC-COF-502", "SALE-COF-502", 5, 9.99, 40, true); // Holiday promo packaging

        storeProducts = new ArrayList<>(List.of(
                sp1, sp2, sp3, sp4, sp5, sp6, sp7, sp8, sp9, sp10
        ));

        /*
        DIARY,
        FRESH,
        MEAT,
        BAKERY,
        SEAFOOD,
        BEVERAGE,
        PACKAGED,
        DRYGOODS,
        PHARMACY,
        HOUSEHOLD,
        PET
         */

        CategoryRecord c1 = new CategoryRecord(0, "DIARY");
        CategoryRecord c2 = new CategoryRecord(1, "FRESH");
        CategoryRecord c3 = new CategoryRecord(2, "MEAT");
        CategoryRecord c4 = new CategoryRecord(3, "BAKERY");
        CategoryRecord c5 = new CategoryRecord(4, "SEAFOOD");
        CategoryRecord c6 = new CategoryRecord(5, "BEVERAGE");
        CategoryRecord c7 = new CategoryRecord(6, "PACKAGED");
        CategoryRecord c8 = new CategoryRecord(7, "DRY GOODS");
        CategoryRecord c9 = new CategoryRecord(8, "PHARMACY");
        CategoryRecord c10 = new CategoryRecord(9, "HOUSEHOLD");
        CategoryRecord c11 = new CategoryRecord(10, "PET");

        categories = new ArrayList<>(List.of(
                c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11
        ));

        //endregion

        // 3. Print to verify
        System.out.println("--- Grocery Products (" + products.size() + ") ---");
        products.forEach(System.out::println);

        System.out.println("\n--- Store Inventory (" + storeProducts.size() + ") ---");
        storeProducts.forEach(System.out::println);

        System.out.println("--- Categories (" + categories.size() + ") ---");
        categories.forEach(System.out::println);

    }

    @Override
    public Iterable<ProductDBModel> getProducts() {
        var result = new ArrayList<ProductDBModel>();
        for (var product : products) {
            result.add(new ProductDBModel(product.dbId, product.title, product.manufacturer, product.description, product.categoryNumber));
        }
        return result;
    }

    @Override
    public Iterable<StoreProductDBModel> getStoreProducts() {
        var result = new ArrayList<StoreProductDBModel>();
        for (var storeProduct : storeProducts) {
            result.add(new StoreProductDBModel(storeProduct.upc, storeProduct.upcSale, storeProduct.productId, storeProduct.price, storeProduct.numberOfProducts, storeProduct.promotionalProduct));
        }
        return result;
    }

    @Override
    public ProductDBModel getProduct(int id) {
        for (var product : products) {
            if (product.dbId == id) return new ProductDBModel(product.dbId, product.title, product.manufacturer, product.description, product.categoryNumber);
        }
        return null;
    }

    @Override
    public StoreProductDBModel getStoreProduct(String upc) {
        for (var storeProduct : storeProducts) {
            if (storeProduct.upc.equals(upc)) return new StoreProductDBModel(storeProduct.upc, storeProduct.upcSale, storeProduct.productId, storeProduct.price, storeProduct.numberOfProducts, storeProduct.promotionalProduct);
        }
        return null;
    }

    @Override
    public ProductDBModel getProductByName(String name) {
        return null;
    }

    @Override
    public void saveNewProduct(ProductDBModel productDBModel) {
        products.add(new ProductRecord(productDBModel.dbId, productDBModel.title, productDBModel.manufacturer, productDBModel.description, productDBModel.categoryNumber));
    }

    @Override
    public Iterable<CategoryDBModel> getCategories() {
        var result = new ArrayList<CategoryDBModel>();
        for (var category : categories) {
            result.add(new CategoryDBModel(category.dbId, category.name));
        }
        return result;
    }

}
