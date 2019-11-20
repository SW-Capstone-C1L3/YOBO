package com.example.yobo_android.etc;

/*
* 레시피 객체
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recipe {

    private String recipeId; // doc_id
    private String name; // recipe_name
    private String writer;
    private int difficulty;
    private String rating;
    private String recipeImageId;
    private String reciepSubDescription;
    private int serving;
    private int DescriptionNum;
    private ArrayList<String> recipeDescription;
    private ArrayList<String> recipeDescriptionImage;

    //    private String[] category;
    //    private JSONArray cooking_description;
//    private JSONArray cooking_ingredient;
//    private JSONArray sub_cooking_ingredient;


    public ArrayList<String> getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(ArrayList<String> recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public ArrayList<String> getRecipeDescriptionImage() {
        return recipeDescriptionImage;
    }

    public void setRecipeDescriptionImage(ArrayList<String> recipeDescriptionImage) {
        this.recipeDescriptionImage = recipeDescriptionImage;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }



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
