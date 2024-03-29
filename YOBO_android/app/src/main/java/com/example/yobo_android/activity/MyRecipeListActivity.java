package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRecipeListActivity extends AppCompatActivity {

    List<Recipe> myRecipeList = new ArrayList<>();

    BoardAdapter myRecipeListAdapter;
    String userId;
    String userName;
    Intent intent;
    private int REQUEST_TEST =1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_list);

        intent = getIntent();
        userId = intent.getStringExtra("u_id");
        userName = intent.getStringExtra("u_name");

        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton mBtnBack = findViewById(R.id.arrow_back_ImageButton);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton mBtnWriteRecipe = findViewById(R.id.write_ImageButton);
        mBtnWriteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyRecipeListActivity.this, RecipeFormActivity.class);
                intent.putExtra("u_id", userId);
                intent.putExtra("u_name",userName);
                startActivityForResult(intent,REQUEST_TEST);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.myRecipeListRecyclerView);
        RecyclerView.LayoutManager layoutManagerForMyRecipe = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerForMyRecipe);
        myRecipeListAdapter = new BoardAdapter();
        recyclerView.setAdapter(myRecipeListAdapter);

        Call<List<Recipe>> call = RetrofitClient.getInstance().getApiService().geListByUid(MainActivity.u_id, 0, 20);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.i("TEST", call.toString());
                Log.i("TEST", response.toString());
                myRecipeList = response.body();
                for (int i = 0; i < myRecipeList.size(); i++) {
                    myRecipeListAdapter.addItem(myRecipeList.get(i),i);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("ERROR", call.toString());
                Log.e("ERROR", t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TEST || requestCode == RecipeMainActivity.REQUEST_MODIFY1) {
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }
}
