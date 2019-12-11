package com.example.yobo_android.etc;

public class IngredientsFormData {
    private String ingredientsName;
    private String ingredientsQuantity;
    private String ingredientsUnit;

    public IngredientsFormData(String ingredientsText, String ingredientsQuantity, String ingredientsUnit) {
        this.ingredientsName = ingredientsText;
        this.ingredientsQuantity = ingredientsQuantity;
        this.ingredientsUnit = ingredientsUnit;
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
