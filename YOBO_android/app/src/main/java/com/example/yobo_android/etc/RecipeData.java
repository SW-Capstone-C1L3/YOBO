package com.example.yobo_android.etc;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeData {
    public RecipeData(List<String> category, List<Cooking_description> cooking_description,
                  Integer difficulty, List<Main_cooking_ingredient> main_cooking_ingredients,
                  Integer rating, String recipe_name, Integer serving,
                  List<Sub_cooking_ingredient> sub_cooking_ingredients, String writer_id) {
        this.category = category;
        this.cooking_description = cooking_description;
        this.difficulty = difficulty;
        this.main_cooking_ingredients = main_cooking_ingredients;
        this.rating = rating;
        this.recipe_name = recipe_name;
        this.serving = serving;
        this.sub_cooking_ingredients = sub_cooking_ingredients;
        this.writer_id = writer_id;
    }

    @SerializedName("category")
    @Expose
    private List<String> category = null;
    @SerializedName("cooking_description")
    @Expose
    private List<Cooking_description> cooking_description = null;
    @SerializedName("difficulty")
    @Expose
    private Integer difficulty;
    @SerializedName("main_cooking_ingredients")
    @Expose
    private List<Main_cooking_ingredient> main_cooking_ingredients = null;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("recipe_name")
    @Expose
    private String recipe_name;
    @SerializedName("serving")
    @Expose
    private Integer serving;
    @SerializedName("sub_cooking_ingredients")
    @Expose
    private List<Sub_cooking_ingredient> sub_cooking_ingredients = null;
    @SerializedName("writer_id")
    @Expose
    private String writer_id;

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

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public List<Main_cooking_ingredient> getMain_cooking_ingredients() {
        return main_cooking_ingredients;
    }

    public void setMain_cooking_ingredients(List<Main_cooking_ingredient> main_cooking_ingredients) {
        this.main_cooking_ingredients = main_cooking_ingredients;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public Integer getServing() {
        return serving;
    }

    public void setServing(Integer serving) {
        this.serving = serving;
    }

    public List<Sub_cooking_ingredient> getSub_cooking_ingredients() {
        return sub_cooking_ingredients;
    }

    public void setSub_cooking_ingredients(List<Sub_cooking_ingredient> sub_cooking_ingredients) {
        this.sub_cooking_ingredients = sub_cooking_ingredients;
    }

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

}