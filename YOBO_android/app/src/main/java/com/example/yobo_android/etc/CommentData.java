package com.example.yobo_android.etc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class CommentData {

    public CommentData(String comment_id, String comments, String u_id, String u_name, String ts, String r_id) {
        this.comment_id = comment_id;
        this.comments = comments;
        this.u_id = u_id;
        this.u_name = u_name;
        this.ts = ts;
        this.r_id = r_id;
    }

    @SerializedName("_id")
    @Expose
    private String comment_id;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("user_id")
    @Expose
    private String u_id;
    @SerializedName("user_name")
    @Expose
    private String u_name;
    @SerializedName("timestamp")
    @Expose
    private String ts;
    @SerializedName("recipe_id")
    @Expose
    private String r_id;
    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }


    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }


    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }
}
