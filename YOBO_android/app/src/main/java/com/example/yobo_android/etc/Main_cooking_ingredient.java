package com.example.yobo_android.etc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main_cooking_ingredient {
    public Main_cooking_ingredient(String ingredients_name, Integer qty, String unit) {
        this.ingredients_name = ingredients_name;
        this.qty = qty;
        this.unit = unit;
    }

    @SerializedName("ingredients_name")
    @Expose
    private String ingredients_name;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("unit")
    @Expose
    private String unit;

    public String getIngredients_name() {
        return ingredients_name;
    }

    public void setIngredients_name(String ingredients_name) {
        this.ingredients_name = ingredients_name;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}