package com.example.yobo_android.api;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface FileUploadAPI {
    @Multipart
    @POST("/api/")
    Call<ResponseBody> uploadImages(@Part Map< ArrayList<MultipartBody.Part>, Map<String, RequestBody>> description,
                                    @PartMap Map<String, RequestBody> mainIngredient);


}

