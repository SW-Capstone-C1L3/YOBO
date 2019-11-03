package com.example.yobo_android.etc;

public class IngredientsFormData {
    private String ingredientsText;
    private String ingredientsQuantity;

    public IngredientsFormData(String ingredientsText, String ingredientsQuantity) {
        this.ingredientsText = ingredientsText;
        this.ingredientsQuantity = ingredientsQuantity;
    }

    public String getIngredientsText() {
        return ingredientsText;
    }

    public void setIngredientsText(String ingredientsText) {
        this.ingredientsText = ingredientsText;
    }

    public String getIngredientsQuantity() {
        return ingredientsQuantity;
    }

    public void setIngredientsQuantity(String ingredientsQuantity) {
        this.ingredientsQuantity = ingredientsQuantity;
    }
}
