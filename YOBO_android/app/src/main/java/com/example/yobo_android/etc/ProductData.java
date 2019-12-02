package com.example.yobo_android.etc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductData {
    public ProductData(String product_id, Integer product_qty) {
        this.product_id = product_id;
        this.product_qty = product_qty;
    }
    @SerializedName("product_id")
    @Expose
    private String product_id;
    @SerializedName("product_qty")
    @Expose
    private Integer product_qty;

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
}
