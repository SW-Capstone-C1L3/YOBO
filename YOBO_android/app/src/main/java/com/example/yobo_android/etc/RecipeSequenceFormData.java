package com.example.yobo_android.etc;

import android.graphics.Bitmap;

public class RecipeSequenceFormData {
    Bitmap recipeSequenceFormImageId;
    String recipeSequenceFormDescription;

    public RecipeSequenceFormData(Bitmap recipeSequenceFormImage, String recipeSequenceFormDescription) {
        this.recipeSequenceFormImageId = recipeSequenceFormImage;
        this.recipeSequenceFormDescription = recipeSequenceFormDescription;
    }

    public Bitmap getRecipeSequenceFormImageId() {
        return recipeSequenceFormImageId;
    }

    public void setRecipeSequenceFormImageId(Bitmap recipeSequenceFormImageId) {
        this.recipeSequenceFormImageId = recipeSequenceFormImageId;
    }

    public String getRecipeSequenceFormDescription() {
        return recipeSequenceFormDescription;
    }

    public void setRecipeSequenceFormDescription(String recipeSequenceFormDescription) {
        this.recipeSequenceFormDescription = recipeSequenceFormDescription;
    }
}
