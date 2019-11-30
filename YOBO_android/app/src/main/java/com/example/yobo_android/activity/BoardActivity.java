package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.Recipe;
import com.google.android.material.snackbar.Snackbar;

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
            call = apiService.getRecipeList(0,10);

        if (call != null) {
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    //Toast.makeText(BoardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Log.i("TEST", call.toString());
                    Log.i("TEST", response.toString());
                    recipeList = response.body();
                    if(recipeList.size()==0){
                        Intent intent = new Intent();
                        intent.putExtra("result", "some value");
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        for (int i = 0; i < recipeList.size(); i++) {
                            adapter.addItem(recipeList.get(i), i);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
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
    
}
