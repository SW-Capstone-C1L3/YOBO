package com.example.yobo_android.api;

import com.example.yobo_android.etc.Comment;
import com.example.yobo_android.etc.BasketLogData;
import com.example.yobo_android.etc.CommentData;
import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.etc.SelectedBasketLogData;
import com.example.yobo_android.etc.ShopLogData;
import com.example.yobo_android.etc.ShoppingIngredientData;
import com.example.yobo_android.etc.UserData;

import java.util.ArrayList;
import java.util.List;
import okhttp3.MultipartBody;
import java.util.HashMap;

import retrofit2.http.DELETE;
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

    /*Login*/

    @GET("users/naverlog")
    Call<UserData> getUserData(@Query("at") String at);

    /*Recipe*/
    @Multipart
    @POST("/yobo/recipe/createRecipe")
    Call<ResponseBody> uploadRecipe(@Part List<MultipartBody.Part> files,
                                      @Part("recipe") Recipe recipe);

    @Multipart
    @POST("/yobo/recipe/updateRecipe")
    Call<ResponseBody> updateRecipe(@Part List<MultipartBody.Part> files,
                                    @Part("recipe") Recipe recipe);

    @GET("/yobo/recipe/getRecipeList/")
    Call<List<Recipe>> getRecipeList(@Query("pageNum") int pageNum,
                                     @Query("pageSize") int pageSize);

    @GET("yobo/recipe/search/")
    Call<List<Recipe>> search(@Query("recipeName") String recipeName,
                              @Query("pageNum") int pageNum,
                              @Query("pageSize") int pageSize);

    @GET("/yobo/recipe/getListbyUid/")
    Call<List<Recipe>> geListByUid(@Query("uid") String uid,
                                   @Query("pageNum") int pageNum,
                                   @Query("pageSize") int pageSize);

    @GET("/yobo/recipe/getByshortcut/")
    Call<List<Recipe>> getByshortcut(@Query("uid") String uid,
                                   @Query("pageNum") int pageNum,
                                   @Query("pageSize") int pageSize);

    @GET("yobo/recipe/getListbyCate/")
    Call<List<Recipe>> getListByCate(@Query("cate") String cate,
                                     @Query("pageNum") int pageNum,
                                     @Query("pageSize") int pageSize);

    @GET("yobo/recipe/getByingredients")
    Call<List<Recipe>> getByingredients(@Query("ingredients") List<String> ingredients,
                                        @Query("pageNum") int pageNum,
                                        @Query("pageSize") int pageSize);

    @GET("yobo/recipe/getRecipebyDid/")
    Call<Recipe> getReicpebyDid(@Query("Did") String recipeId);

    @DELETE("/yobo/recipe/DeleteDid/")
    Call<ResponseBody> deleteRecipeByDid(@Query("Did") String Did);

    @POST("/yobo/recipe/rate")
    Call<ResponseBody> rate(@Query("Rid") String Rid,
                            @Query("rate") double rate,
                            @Query("uid") String uid);

    @GET("/yobo/recipe/getByrecommend/")
    Call<List<Recipe>> getRecommendedRecipe(@Query("favorite") List<String> favorite_list);

    /*Market*/

    @GET("yobo/product/search/")
    Call<List<ShoppingIngredientData>> searchProduct(@Query("productName") String productName,
                                                     @Query("pageNum") int pageNum,
                                                     @Query("pageSize") int pageSize);

    @GET("/yobo/product/getProducteList/")
    Call<List<ShoppingIngredientData>> getProductList(@Query("pageNum") int pageNum,
                                                      @Query("pageSize") int pageSize);

    @GET("/yobo/product/searchbyDid/")
    Call<ShoppingIngredientData> getProduct(@Query("Did") String productId);

    @Multipart
    @POST("/yobo/transaction/createtransaction")
    Call<ResponseBody> createTransaction(@Part("transcationLog")BasketLogData basketLogData);

    @GET("/yobo/transaction/getByUid")
    Call<List<ShopLogData>> getLogData(@Query("Uid") String Uid,
                                       @Query("pageNum") int pageNum,
                                       @Query("pageSize") int pageSize);

    @GET("/yobo/transaction/getByDid")
    Call<SelectedBasketLogData> getSelectedLogData(@Query("Did") String Did,
                                                         @Query("pageNum") int pageNum,
                                                         @Query("pageSize") int pageSize);
    /*Basket*/

    @FormUrlEncoded
    @POST("/yobo/basket/insertBasket")
    Call<ResponseBody> insertBasket(@FieldMap HashMap<String,Object> parameters);

    @FormUrlEncoded
    @POST("/yobo/basket/DeleteBasket")
    Call<ResponseBody> DeleteBasket(@FieldMap HashMap<String,Object> parameters);

    /*Comment*/

    @GET("yobo/comments/getCommentsbyRId/")
    Call<List<CommentData>> getCommentsbyRId(@Query("RId") String recipeId,
                                             @Query("pageNum") int pageNum,
                                             @Query("pageSize") int pageSize);

    @GET("yobo/comments/getCommentsbyUId/")
    Call<List<CommentData>> getCommentsbyUId(@Query("UId") String UId,
                                        @Query("pageNum") int pageNum,
                                        @Query("pageSize") int pageSize);

    @Multipart
    @POST("/yobo/comments/createcomments")
    Call<ResponseBody> postComment(@Part("comments") Comment comments);

  /* User */
    @GET("yobo/user/getbyDid/")
    Call<UserData> getbyDid(@Query("Did") String Did);

    @Multipart
    @POST("/yobo/recipe/updateUser")
    Call<ResponseBody> updateUser(@Part MultipartBody.Part image,
                                   @Part("user") UserData userData);

    @POST("/yobo/recipe/addShortCut")
    Call<Integer> addShortCut(@Query("Rid") String recipeId,
                              @Query("Uid") String userId);
}

