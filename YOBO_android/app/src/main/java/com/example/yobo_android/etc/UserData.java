package com.example.yobo_android.etc;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("user_age")
    @Expose
    private Integer user_age;
    @SerializedName("user_phone_num")
    @Expose
    private String user_phone_num;
    @SerializedName("interest_category")
    @Expose
    private ArrayList<String> interest_category = null;
    @SerializedName("user_grade")
    @Expose
    private Integer user_grade;
    @SerializedName("user_email")
    @Expose
    private String user_email;
    @SerializedName("user_address")
    @Expose
    private ArrayList<String> user_address = null;
    @SerializedName("recipe_shotcut")
    @Expose
    private ArrayList<String> recipe_shotcut = null;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getUser_age() {
        return user_age;
    }

    public void setUser_age(Integer user_age) {
        this.user_age = user_age;
    }

    public String getUser_phone_num() {
        return user_phone_num;
    }

    public void setUser_phone_num(String user_phone_num) {
        this.user_phone_num = user_phone_num;
    }

    public ArrayList<String> getInterest_category() {
        return interest_category;
    }

    public void setInterest_category(ArrayList<String> interest_category) {
        this.interest_category = interest_category;
    }

    public Integer getUser_grade() {
        return user_grade;
    }

    public void setUser_grade(Integer user_grade) {
        this.user_grade = user_grade;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public ArrayList<String> getUser_address() {
        return user_address;
    }

    public void setUser_address(ArrayList<String> user_address) {
        this.user_address = user_address;
    }

    public ArrayList<String> getRecipe_shotcut() {
        return recipe_shotcut;
    }

    public void setRecipe_shotcut(ArrayList<String> recipe_shotcut) {
        this.recipe_shotcut = recipe_shotcut;
    }

}