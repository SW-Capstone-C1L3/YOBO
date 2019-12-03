package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.yobo_android.R;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.etc.UserData;

import java.util.ArrayList;
import java.util.List;

public class ModifyMyInfoActivity extends AppCompatActivity {

    String u_id = "";
    ArrayList<String> userAddress;
    ArrayList<String> userInterestCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_my_info);

        u_id = getIntent().getStringExtra("u_id");
//        Log.i("Dddd",u_id);

//        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        okhttpClientBuilder.addInterceptor(logging);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(ApiService.API_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okhttpClientBuilder.build())
//                .build();
//        ApiService apiService = retrofit.create(ApiService.class);
//        Call<UserData> call = apiService.getbyDid(user_Did);
//        if (call != null) {
//            call.enqueue(new Callback<UserData>() {
//                @Override
//                public void onResponse(Call<UserData> call, Response<UserData> response) {
//                    UserData userdata = response.body();
//                    ((EditText)findViewById(R.id.edit_userName)).setText(userdata.getUser_name());
//                    ((EditText)findViewById(R.id.edit_userAddress1)).setText(userdata.getUser_address().get(0));
//                    ((EditText)findViewById(R.id.edit_userAddress2)).setText(userdata.getUser_address().get(1));
//                }
//                @Override
//                public void onFailure(Call<UserData> call, Throwable t) {
//                }
//            });
//        }

    }
}
