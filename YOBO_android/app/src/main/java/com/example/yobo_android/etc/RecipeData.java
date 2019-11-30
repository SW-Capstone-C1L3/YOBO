package com.example.yobo_android.etc;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Post 할 때 쓰는 클래스. Recipe 클래스와 다름.
public class RecipeData {
    public RecipeData(List<String> category,
                      List<Cooking_description> cooking_description,
                      List<Cooking_ingredient> main_cooking_ingredients,
                      List<Cooking_ingredient> sub_cooking_ingredients,
                      Double rating, String recipe_name, String serving,
                      String difficulty, String writer_id, String writer_name) {
        this.category = category;
        this.cooking_description = cooking_description;
        this.difficulty = difficulty;
        this.main_cooking_ingredients = main_cooking_ingredients;
        this.rating = rating;
        this.recipe_name = recipe_name;
        this.serving = serving;
        this.sub_cooking_ingredients = sub_cooking_ingredients;
        this.writer_id = writer_id;
        this.writer_name = writer_name;
    }

    @SerializedName("category")
    @Expose
    private List<String> category = null;
    @SerializedName("cooking_description")
    @Expose
    private List<Cooking_description> cooking_description = null;
    @SerializedName("difficulty")
    @Expose
    private String difficulty;
    @SerializedName("main_cooking_ingredients")
    @Expose
    private List<Cooking_ingredient> main_cooking_ingredients = null;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("recipe_name")
    @Expose
    private String recipe_name;
    @SerializedName("serving")
    @Expose
    private String serving;
    @SerializedName("sub_cooking_ingredients")
    @Expose
    private List<Cooking_ingredient> sub_cooking_ingredients = null;
    @SerializedName("writer_id")
    @Expose
    private String writer_id;
    @SerializedName("writer_name")
    @Expose
    private String writer_name;

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<Cooking_description> getCooking_description() {
        return cooking_description;
    }

    public void setCooking_description(List<Cooking_description> cooking_description) {
        this.cooking_description = cooking_description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Cooking_ingredient> getMain_cooking_ingredients() {
        return main_cooking_ingredients;
    }

    public void setMain_cooking_ingredients(List<Cooking_ingredient> main_cooking_ingredients) {
        this.main_cooking_ingredients = main_cooking_ingredients;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public List<Cooking_ingredient> getSub_cooking_ingredients() {
        return sub_cooking_ingredients;
    }

    public void setSub_cooking_ingredients(List<Cooking_ingredient> sub_cooking_ingredients) {
        this.sub_cooking_ingredients = sub_cooking_ingredients;
    }

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

    public String getWriter_name() { return writer_name; }

    public void setWriter_name(String writer_name) { this.writer_name = writer_name; }
}