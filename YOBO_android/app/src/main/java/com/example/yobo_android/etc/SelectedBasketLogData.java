package com.example.yobo_android.etc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.List;

public class SelectedBasketLogData {

    public SelectedBasketLogData(List<ProductData> products, Integer total_price,
                                 String transaction_status, String user_Did,
                                 String user_address, String user_id,
                                 String user_phone_num, String invoice_number,
                                 String invoice_company, String company_id,
                                 Timestamp timestamp, String _id) {
        this.products = products;
        this.total_price = total_price;
        this.transaction_status = transaction_status;
        this.user_Did = user_Did;
        this.user_address = user_address;
        this.user_id = user_id;
        this.user_phone_num = user_phone_num;
        this.invoice_number = invoice_number;
        this.invoice_company = invoice_company;
        this.company_id = company_id;
        this.timestamp = timestamp;
        this._id = _id;
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
    @SerializedName("invoice_number")
    @Expose
    private String invoice_number;
    @SerializedName("invoice_company")
    @Expose
    private String invoice_company;
    @SerializedName("company_id")
    @Expose
    private String company_id;
    @SerializedName("timestamp")
    @Expose
    private Timestamp timestamp;
    @SerializedName("_id")
    @Expose
    private String _id;

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

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getInvoice_company() {
        return invoice_company;
    }

    public void setInvoice_company(String invoice_company) {
        this.invoice_company = invoice_company;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
