package com.example.yobo_android.api;


import java.util.HashMap;
import retrofit2.http.FieldMap;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {


    public static final String API_URL = "http://45.119.146.82:8081";

    @FormUrlEncoded
    @POST("/yobo/basket/insertBasket")
    Call<ResponseBody> insertBasket(@FieldMap HashMap<String,Object> parameters);

    @FormUrlEncoded
    @POST("/yobo/basket/DeleteBasket")
    Call<ResponseBody> DeleteBasket(@FieldMap HashMap<String,Object> parameters);

//    @Multipart
//    @POST("/yobo/recipe/createRecipe")
//    Call<이부분> uploadRecipe(@Multipart... );
}

