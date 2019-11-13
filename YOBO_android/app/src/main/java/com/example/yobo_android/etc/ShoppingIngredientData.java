package com.example.yobo_android.etc;

public class ShoppingIngredientData {

    private String sel_id;      //document id
    private String company_name;
    private String product_category;
    private String product_description;
    private String product_image;
    private String product_name;
    private int product_price;
    private int product_qty;
    private String product_unit;
    private String provided_company_id;

    public String getSel_id() {
        return sel_id;
    }

    public void setSel_id(String sel_id) {
        this.sel_id = sel_id;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }

    public void setProduct_unit(String product_unit) {
        this.product_unit = product_unit;
    }

    public void setProvided_company_id(String provided_company_id) {
        this.provided_company_id = provided_company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getProduct_category() {
        return product_category;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getProduct_price() {
        return product_price;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public String getProduct_unit() {
        return product_unit;
    }

    public String getProvided_company_id() {
        return provided_company_id;
    }
}
