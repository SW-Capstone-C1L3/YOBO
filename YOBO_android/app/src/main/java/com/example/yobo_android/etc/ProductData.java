package com.example.yobo_android.etc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductData {
    public ProductData(String product_id, Integer product_qty, String company_id, Integer price, String product_name) {
        this.product_id = product_id;
        this.product_qty = product_qty;
        this.company_id = company_id;
        this.price = price;
        this.product_name = product_name;
    }

    @SerializedName("product_id")
    @Expose
    private String product_id;
    @SerializedName("product_qty")
    @Expose
    private Integer product_qty;
    @SerializedName("company_id")
    @Expose
    private String company_id;
    @SerializedName("price")
    @Expose
    private Integer price;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Integer getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(Integer product_qty) {
        this.product_qty = product_qty;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
