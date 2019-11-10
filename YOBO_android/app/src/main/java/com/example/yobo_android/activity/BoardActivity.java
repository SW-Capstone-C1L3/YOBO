package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/*
* 검색에 따른 레시피 목록을 보여주는 Activity
* 레시피 item 선택 시 RecipeActivity 이동
 */

public class BoardActivity extends AppCompatActivity {

    private BoardAdapter adapter;
    private TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        init();
        new RequestAsync().execute();

     }

    public void jsonParser(String json) {
        try {
            JSONArray recipeList = new JSONArray(json);

            for(int i=0; i<recipeList.length(); i++){
                Recipe recipeItem = new Recipe();

                JSONObject recipe = recipeList.getJSONObject(i);

                recipeItem.setRecipeId(recipe.getString("_id"));
                recipeItem.setName(recipe.getString("recipe_name"));
                recipeItem.setWriter(recipe.getString("writer_id"));
                recipeItem.setDifficulty(recipe.getInt("difficulty"));
                recipeItem.setRating(recipe.getLong("rating"));
//                recipe_category = recipe.get
                recipeItem.setServing(recipe.getInt("serving"));

                adapter.addItem(recipeItem);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                //GET Request
                return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/recipe/getRecipeList/?pageNum=1");

                // POST Request
//                JSONObject postDataParams = new JSONObject();
//                postDataParams.put("name", "Manjeet");
//                postDataParams.put("email", "manjeet@gmail.com");
//                postDataParams.put("phone", "+1111111111");
//
//                return RequestHttpURLConnection.sendPost("https://prodevsblog.com/android_post.php", postDataParams);
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                jsonParser(s);
            }
        }
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerBoardView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BoardAdapter();
        recyclerView.setAdapter(adapter);
    }

}
