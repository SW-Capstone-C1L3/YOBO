package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.Recipe;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
* 검색에 따른 레시피 목록을 보여주는 Activity
* 레시피 item 선택 시 RecipeActivity 이동
 */

public class BoardActivity extends AppCompatActivity {

    List<Recipe> recipeList = new ArrayList<>();
    Retrofit retrofit;
    ApiService apiService;

    private BoardAdapter adapter;
    private String query = null;
    private String category;
    private List<String> ingredients;
    Integer num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        if (getIntent().getStringExtra("query") != null) {
            query = getIntent().getStringExtra("query");
        }
        if (getIntent().getStringExtra("category") != null) {
            category = getIntent().getStringExtra("category");
        }
        if (getIntent().getStringArrayListExtra("ingredients") != null) {
            ingredients = getIntent().getStringArrayListExtra("ingredients");
        }

        recyclerViewInit();
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build())
                .build();
        apiService = retrofit.create(ApiService.class);

        Call<List<Recipe>> call = null;
        if (query != null)
            call = apiService.search(query, 0, 10);
        else if(category != null){
                call = apiService.getListByCate(category, 0, 10);
        } else if(ingredients != null){
            call = apiService.getByingredients(ingredients, 0, 10);
        } else
            call = apiService.getRecipeList(2,10);

        if (call != null) {
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    Toast.makeText(BoardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Log.i("TEST", call.toString());
                    Log.i("TEST", response.toString());
                    recipeList = response.body();
                    for (int i = 0; i < recipeList.size(); i++) {
                        adapter.addItem(recipeList.get(i), i);
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Toast.makeText(BoardActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", call.toString());
                    Log.e("ERROR", t.toString());
                }
            });
        }
    }

    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.recyclerBoardView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BoardAdapter();
        recyclerView.setAdapter(adapter);
    }


    public void checkInput() {
        String snackBarMessage = null;
        if (snackBarMessage == null) {
            snackBarMessage = "일치하는 항목이 존재하지 않습니다.";
            Snackbar make = Snackbar.make(getWindow().getDecorView().getRootView(),
                    snackBarMessage, Snackbar.LENGTH_LONG);
            make.setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            make.setActionTextColor(Color.RED);
            make.show();
        }
    }

}
