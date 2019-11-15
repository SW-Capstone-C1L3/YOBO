package com.example.yobo_android.etc;

public class IngredientsBasketData {
    private String ingredientName;
    private Integer ingredientPrice;
    private Integer basket_qty;
    private String basket_product_id;

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Integer getIngredientPrice() {
        return ingredientPrice;
    }

    public void setIngredientPrice(Integer ingredientPrice) {
        this.ingredientPrice = ingredientPrice;
    }


    public Integer getBasket_qty() {
        return basket_qty;
    }

    public void setBasket_qty(Integer basket_qty) {
        this.basket_qty = basket_qty;
    }

    public String getBasket_product_id() {
        return basket_product_id;
    }

    public void setBasket_product_id(String basket_product_id) {
        this.basket_product_id = basket_product_id;
    }

}
