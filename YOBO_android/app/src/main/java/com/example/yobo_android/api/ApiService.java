package com.example.yobo_android.api;

import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.etc.RecipeData;
import com.example.yobo_android.etc.ShoppingIngredientData;
import com.example.yobo_android.etc.UserData;

import java.util.List;
import okhttp3.MultipartBody;
import java.util.HashMap;
import retrofit2.http.FieldMap;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    public static final String API_URL = "http://45.119.146.82:8081";

    @Multipart
    @POST("/yobo/recipe/createRecipe")
    Call<ResponseBody> uploadRecipe(@Part List<MultipartBody.Part> files,
                                      @Part("recipe") RecipeData recipe);
    @FormUrlEncoded
    @POST("/yobo/basket/insertBasket")
    Call<ResponseBody> insertBasket(@FieldMap HashMap<String,Object> parameters);

    @FormUrlEncoded
    @POST("/yobo/basket/DeleteBasket")
    Call<ResponseBody> DeleteBasket(@FieldMap HashMap<String,Object> parameters);

    @GET("/yobo/recipe/getRecipeList/")
    Call<List<Recipe>> getRecipeList(@Query("pageNum") int pageNum,
                                     @Query("pageSize") int pageSize);

    @GET("/yobo/product/getProducteList/")
    Call<List<ShoppingIngredientData>> getProductList(@Query("pageNum") int pageNum,
                                     @Query("pageSize") int pageSize);

    @GET("yobo/recipe/search/")
    Call<List<Recipe>> search(@Query("recipeName") String recipeName,
                                             @Query("pageNum") int pageNum,
                                             @Query("pageSize") int pageSize);

    @GET("yobo/product/search/")
    Call<List<ShoppingIngredientData>> searchProduct(@Query("productName") String productName,
                                              @Query("pageNum") int pageNum,
                                              @Query("pageSize") int pageSize);

    @GET("yobo/recipe/getRecipebyDid/")
    Call<Recipe> getReicpebyDid(@Query("Did") String recipeId);

    @GET("yobo/recipe/getListbyCate/")
    Call<List<Recipe>> getListByCate(@Query("cate") String cate,
                                     @Query("pageNum") int pageNum,
                                     @Query("pageSize") int pageSize);

    @GET("yobo/recipe/getByingredients")
    Call<List<Recipe>> getByingredients(@Query("ingredients") List<String> ingredients,
                                     @Query("pageNum") int pageNum,
                                     @Query("pageSize") int pageSize);
    @GET("users/naverlog")
    Call<UserData> getUserData(@Query("at") String at);

}

