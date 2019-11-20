package com.example.yobo_android.etc;

/*
* 레시피 객체
 */

import org.json.JSONArray;
import org.json.JSONObject;

public class Recipe {

    private String recipeId; // doc_id
    private String name; // recipe_name
    private String writer;
    private int difficulty;
    private String rating;
    private String recipeImageId;
    private String reciepSubDescription;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    //    private String[] category;
    private int serving;
//    private JSONArray cooking_description;
    private int DescriptionNum;
//    private JSONArray cooking_ingredient;
//    private JSONArray sub_cooking_ingredient;


    public int getDescriptionNum() {
        return DescriptionNum;
    }

    public void setDescriptionNum(int descriptionNum) {
        DescriptionNum = descriptionNum;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRecipeImageId() {
        return recipeImageId;
    }

    public void setRecipeImageId(String recipeImageId) {
        this.recipeImageId = recipeImageId;
    }

    public String getReciepSubDescription() {
        return reciepSubDescription;
    }

    public void setReciepSubDescription(String reciepSubDescription) {
        this.reciepSubDescription = reciepSubDescription;
    }
}
