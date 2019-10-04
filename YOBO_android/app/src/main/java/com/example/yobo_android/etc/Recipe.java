package com.example.yobo_android.etc;

public class Recipe {

    private String recipeId;
    private String recipeName;
    private String recipeSubContents;
    private String recipeWriter;
    private int recipeScore;
    private int recipeImageId;
    //private ? recipeImage


    public int getRecipeImageId() {
        return recipeImageId;
    }

    public void setRecipeImageId(int recipeImageId) {
        this.recipeImageId = recipeImageId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeSubContents() {
        return recipeSubContents;
    }

    public void setRecipeSubContents(String recipeSubContents) {
        this.recipeSubContents = recipeSubContents;
    }

    public String getRecipeWriter() {
        return recipeWriter;
    }

    public void setRecipeWriter(String recipeWriter) {
        this.recipeWriter = recipeWriter;
    }

    public int getRecipeScore() {
        return recipeScore;
    }

    public void setRecipeScore(int recipeScore) {
        this.recipeScore = recipeScore;
    }
}
