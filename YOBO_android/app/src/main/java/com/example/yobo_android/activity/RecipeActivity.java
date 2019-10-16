package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.RecipeOrderAdapter;
import com.example.yobo_android.etc.RecipeOrder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private RecipeOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        init();
        getData();

    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.recyclerRecipeOrderView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecipeOrderAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData(){
        List<Integer> listResId = Arrays.asList(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground
        );
        for(int i=0;i<10;i++){
            RecipeOrder recipeOrder = new RecipeOrder();
            recipeOrder.setRecipeOrderImageId(listResId.get(i));
            recipeOrder.setRecipeOrderNumber(""+i);
            recipeOrder.setRecipeOrderDescription("설명ㅁㅁㄻㄻㄻㄻㄻㄻㄹㄴㄻㄹ");

            adapter.addItem(recipeOrder);
        }
        adapter.notifyDataSetChanged();
    }

}
