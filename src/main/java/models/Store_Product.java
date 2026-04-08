package models;

public class Store_Product {
    private String UPC;
    private String UPC_prom;
    private int id_product;
    private String selling_price;
    private int product_number;
    private String promotional_product;

    public Store_Product(String UPC, String UPC_prom, int id_product,
                         String selling_price, int product_number,
                         String promotional_product) {
        this.UPC = UPC;
        this.UPC_prom = UPC_prom;
        this.id_product = id_product;
        this.selling_price = selling_price;
        this.product_number = product_number;
        this.promotional_product = promotional_product;

    }
    public String getUPC() {
        return UPC;
    }
    public String getUPC_prom() {
        return UPC_prom;
    }
    public int getId_product() {
        return id_product;
    }
    public String getSelling_price() {
        return selling_price;
    }
    public int getProduct_number() {
        return product_number;
    }
    public String getPromotional_product() {
        return promotional_product;
    }


    public void setUPC(String UPC) {
        this.UPC = UPC;
    }
    public void setUPC_prom(String UPC_prom) {
        this.UPC_prom = UPC_prom;
    }
    public void setId_product(int id_product) {
        this.id_product = id_product;
    }
    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }
    public void setProduct_number(int product_number) {
        this.product_number = product_number;
    }
    public void setPromotional_product(String promotional_product) {
        this.promotional_product = promotional_product;
    }
}
