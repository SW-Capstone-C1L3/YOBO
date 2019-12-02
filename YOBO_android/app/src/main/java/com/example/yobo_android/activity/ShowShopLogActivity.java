package com.example.yobo_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.adapter.viewholder.ShopLogAdapter;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.etc.ShopLogData;
import com.example.yobo_android.etc.ShoppingIngredientData;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowShopLogActivity extends AppCompatActivity {

    List<ShopLogData> LogList = new ArrayList<>();
    Retrofit retrofit;
    ApiService apiService;
    private ShopLogAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop_log);

        recyclerViewInit();
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.addInterceptor(logging);
        /*****************수정필요*****************/
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build())
                .build();
        apiService = retrofit.create(ApiService.class);
        /*******************여까지********************/
        Call<List<ShopLogData>> call = null;
        call = apiService.getLogData(MainActivity.u_id, 0, 10);

        if (call != null) {
            call.enqueue(new Callback<List<ShopLogData>>() {
                @Override
                public void onResponse(Call<List<ShopLogData>> call, Response<List<ShopLogData>> response) {
                    //Toast.makeText(BoardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Log.i("TEST", call.toString());
                    Log.i("TEST", response.toString());
                    LogList = response.body();
                    for (int i = 0; i < LogList.size(); i++) {
                        Log.i("TEST111",i+"");
                        adapter.addItem(LogList.get(i), i);
                    }
                }
                @Override
                public void onFailure(Call<List<ShopLogData>> call, Throwable t) {
                    //Toast.makeText(BoardActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", call.toString());
                    Log.e("ERROR", t.toString());
                }
            });
        }

    }

    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.recyclerLogView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ShopLogAdapter();
        recyclerView.setAdapter(adapter);
    }
}
