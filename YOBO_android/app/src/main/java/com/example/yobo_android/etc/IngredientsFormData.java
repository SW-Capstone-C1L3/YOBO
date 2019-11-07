package com.example.yobo_android.etc;

public class IngredientsFormData {
    private String ingredientsName;
    private String ingredientsQuantity;

    public IngredientsFormData(String ingredientsText, String ingredientsQuantity) {
        this.ingredientsName = ingredientsText;
        this.ingredientsQuantity = ingredientsQuantity;
    }

    public String getIngredientsName() {
        return ingredientsName;
    }

    public void setIngredientsName(String ingredientsName) {
        this.ingredientsName = ingredientsName;
    }

    public String getIngredientsQuantity() {
        return ingredientsQuantity;
    }

    public void setIngredientsQuantity(String ingredientsQuantity) {
        this.ingredientsQuantity = ingredientsQuantity;
    }
}
