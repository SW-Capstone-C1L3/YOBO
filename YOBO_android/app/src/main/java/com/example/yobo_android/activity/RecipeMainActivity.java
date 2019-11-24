package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.adapter.viewholder.IngredientsAdapter;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.Main_cooking_ingredient;
import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.etc.Sub_cooking_ingredient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RecipeMainActivity extends AppCompatActivity {

    ApiService apiService;
    Retrofit retrofit;

    String recipeId;
    int descriptionNum;

    Recipe recipe;
    List<Main_cooking_ingredient> main_cooking_ingredients;
    List<Sub_cooking_ingredient> sub_cooking_ingredients;

    private IngredientsAdapter mMainIngredientAdapter;
    private IngredientsAdapter mSubIngredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);

        recipeId = getIntent().getStringExtra("recipeId");
        descriptionNum = getIntent().getIntExtra("recipeDescriptionNum",descriptionNum);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CollapsingToolbarLayout mCollapseToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);

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
        Call<Recipe> call = null;
        call = apiService.getReicpebyDid(recipeId);
        if(call != null) {
            call.enqueue(new Callback<Recipe>() {
                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    recipe = response.body();
                    main_cooking_ingredients = recipe.getMain_cooking_ingredients();
                    sub_cooking_ingredients = recipe.getSub_cooking_ingredients();
                    for (int i = 0; i < main_cooking_ingredients.size(); i++) {
                        mMainIngredientAdapter.addItem(main_cooking_ingredients.get(i));
                    }
//                    for (int i = 0; i < sub_cooking_ingredients.size(); i++) {
//                        mSubIngredientAdapter.addItem(sub_cooking_ingredients.get(i),i);
//                    }
                    mMainIngredientAdapter.notifyDataSetChanged();
//                    mSubIngredientAdapter.notifyDataSetChanged();


                    String temp = recipe.getCooking_description().get(0).getImage();
                    temp = temp.replace("/", "%2F");
                    String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                    try {
                        URL url = new URL(sum);
                        Picasso.get().load(url.toString()).into((ImageView)findViewById(R.id.recipeImage2));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    ((TextView)findViewById(R.id.recipeName)).setText(recipe.getRecipe_name());

                    ((TextView)findViewById(R.id.difficulty2)).setText(recipe.getDifficulty()+"");
                    ((TextView)findViewById(R.id.serving2)).setText(recipe.getServing()+"");
                    ((TextView)findViewById(R.id.rating2)).setText(recipe.getRating()+"");

                    ((TextView)findViewById(R.id.subdescription2)).setText(recipe.getCooking_description().get(0).getDescription());

                    ((TextView)findViewById(R.id.writerid2)).setText("by " + recipe.getWriter_id());
                }

                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {
                    Toast.makeText(RecipeMainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }

        ((Button)findViewById(R.id.cook)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeMainActivity.this, RecipeActivity.class);
                intent.putExtra("recipeId",recipeId);
                intent.putExtra("recipeDescriptionNum",descriptionNum);
                startActivity(intent);
            }
        });
    }

    private void recyclerViewInit() {
        RecyclerView mMainIngredientsView = findViewById(R.id.mainingredients2);
        RecyclerView mSubIngredientsView = findViewById(R.id.subingredients2);

        LinearLayoutManager mLinearLayoutManagerMain = new LinearLayoutManager(this);
        mMainIngredientsView.setLayoutManager(mLinearLayoutManagerMain);
        LinearLayoutManager mLinearLayoutManagerSub = new LinearLayoutManager(this);
        mSubIngredientsView.setLayoutManager(mLinearLayoutManagerSub);

        mMainIngredientAdapter = new IngredientsAdapter();
        mSubIngredientAdapter = new IngredientsAdapter();

        mMainIngredientsView.setAdapter(mMainIngredientAdapter);
        mSubIngredientsView.setAdapter(mSubIngredientAdapter);
    }
}
