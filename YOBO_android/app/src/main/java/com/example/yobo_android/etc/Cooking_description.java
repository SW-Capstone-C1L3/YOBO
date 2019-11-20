package com.example.yobo_android.etc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cooking_description {
    public Cooking_description(String description, String image) {
        this.description = description;
        this.image = image;
    }

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}