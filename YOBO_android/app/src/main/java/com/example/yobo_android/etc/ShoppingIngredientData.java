package com.example.yobo_android.etc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShoppingIngredientData {
    public ShoppingIngredientData(String _id,
                                  String company_name,
                                  String product_category,
                                  String product_description,
                                  String product_image,
                                  String product_name,
                                  int product_price,
                                  int product_qty,
                                  String product_unit,
                                  String provided_company_id) {
        this._id = _id;
        this.company_name = company_name;
        this.product_category = product_category;
        this.product_description = product_description;
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_qty = product_qty;
        this.product_unit = product_unit;
        this.provided_company_id = provided_company_id;
    }

    @SerializedName("_id")
    @Expose
    private String _id;      //document id
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("product_category")
    @Expose
    private String product_category;
    @SerializedName("product_description")
    @Expose
    private String product_description;
    @SerializedName("product_image")
    @Expose
    private String product_image;
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("product_price")
    @Expose
    private int product_price;
    @SerializedName("product_qty")
    @Expose
    private int product_qty;
    @SerializedName("product_unit")
    @Expose
    private String product_unit;
    @SerializedName("provided_company_id")
    @Expose
    private String provided_company_id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
