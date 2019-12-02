package com.example.yobo_android.etc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BasketLogData {
    public BasketLogData(List<ProductData> products, Integer total_price,
                         String transaction_status, String user_Did, String user_address,
                         String user_id, String user_phone_num) {
        this.products = products;
        this.total_price = total_price;
        this.transaction_status = transaction_status;
        this.user_Did = user_Did;
        this.user_address = user_address;
        this.user_id = user_id;
        this.user_phone_num = user_phone_num;
    }

    @SerializedName("products")
    @Expose
    private List<ProductData> products = null;
    @SerializedName("total_price")
    @Expose
    private Integer total_price;
    @SerializedName("transaction_status")
    @Expose
    private String transaction_status;
    @SerializedName("user_Did")
    @Expose
    private String user_Did;
    @SerializedName("user_address")
    @Expose
    private String user_address;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("user_phone_num")
    @Expose
    private String user_phone_num;

    public List<ProductData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductData> products) {
        this.products = products;
    }

    public Integer getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Integer total_price) {
        this.total_price = total_price;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public String getUser_Did() {
        return user_Did;
    }

    public void setUser_Did(String user_Did) {
        this.user_Did = user_Did;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_phone_num() {
        return user_phone_num;
    }

    public void setUser_phone_num(String user_phone_num) {
        this.user_phone_num = user_phone_num;
    }
}
