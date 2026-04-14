package models;

public class Sale {
    private String UPC;
    private String check_number;
    private int product_number;
    private double selling_price;

    public Sale(String UPC, String check_number,
                int product_number, double selling_price) {
        this.UPC = UPC;
        this.check_number = check_number;
        this.product_number = product_number;
        this.selling_price = selling_price;
    }


    public String getUPC() {
        return UPC;
    }
    public String getCheck_number() {
        return check_number;
    }
    public int getProduct_number() {
        return product_number;
    }
    public double getSelling_price() {
        return selling_price;
    }


    public void setUPC(String UPC) {
        this.UPC = UPC;
    }
    public void setCheck_number(String check_number) {
        this.check_number = check_number;
    }
    public void setProduct_number(int product_number) {
        this.product_number = product_number;
    }
    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }
}
