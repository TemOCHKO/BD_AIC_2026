package org.aic.DBModels;

public class SaleDBModel {
    private String UPC;
    private int check_number;
    private int product_number;
    private String selling_price;

    public SaleDBModel(String UPC, int check_number,
                int product_number, String selling_price) {
        this.UPC = UPC;
        this.check_number = check_number;
        this.product_number = product_number;
        this.selling_price = selling_price;
    }


    public String getUPC() {
        return UPC;
    }
    public int getCheck_number() {
        return check_number;
    }
    public int getProduct_number() {
        return product_number;
    }
    public String getSelling_price() {
        return selling_price;
    }


    public void setUPC(String UPC) {
        this.UPC = UPC;
    }
    public void setCheck_number(int check_number) {
        this.check_number = check_number;
    }
    public void setProduct_number(int product_number) {
        this.product_number = product_number;
    }
    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }
}
