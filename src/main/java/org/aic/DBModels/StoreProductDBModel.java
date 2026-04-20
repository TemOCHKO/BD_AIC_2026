package org.aic.DBModels;

public class StoreProductDBModel {
    private String UPC;
    private String UPC_prom;
    private int id_product;
    private String product_name;
    private double selling_price;
    private int products_number;
    private boolean promotional_product;

    public StoreProductDBModel(String UPC, String UPC_prom, int id_product, String product_name,
                         double selling_price, int products_number,
                         boolean promotional_product) {
        this.UPC = UPC;
        this.UPC_prom = UPC_prom;
        this.id_product = id_product;
        this.product_name = product_name;
        this.selling_price = selling_price;
        this.products_number = products_number;
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
    public double getSelling_price() {
        return selling_price;
    }
    public int getProducts_number() {
        return products_number;
    }
    public boolean getPromotional_product() {
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
    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }
    public void setProducts_number(int products_number) {
        this.products_number = products_number;
    }
    public void setPromotional_product(boolean promotional_product) {
        this.promotional_product = promotional_product;
    }

    public String getProduct_name() {
        return product_name;
    }
}