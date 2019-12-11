package com.example.yobo_android.etc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class CommentData {

    public CommentData(String _id, String comments, String user_id, String user_name, String timestamp, String recipe_id, String recipe_name) {
        this._id = _id;
        this.comments = comments;
        this.user_id = user_id;
        this.user_name = user_name;
        this.timestamp = timestamp;
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
    }

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("recipe_id")
    @Expose
    private String recipe_id;
    @SerializedName("recipe_name")
    @Expose
    private String recipe_name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }
}
