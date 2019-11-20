package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.Recipe;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
* 검색에 따른 레시피 목록을 보여주는 Activity
* 레시피 item 선택 시 RecipeActivity 이동
 */

public class BoardActivity extends AppCompatActivity {

    private BoardAdapter adapter;
    private String query = null;
    private String category;
    private String ingredients;
    Integer num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        if(getIntent().getStringExtra("query") != null){
            query = getIntent().getStringExtra("query");
        }
        if(getIntent().getStringExtra("category") != null){
            category = getIntent().getStringExtra("category");
        }
        if(getIntent().getStringExtra("ingredients") != null){
            ingredients = getIntent().getStringExtra("ingredients");
        }

        recyclerViewInit();
        new RequestAsync().execute();
     }


    public void jsonParser(String json) {
        try {
            JSONArray recipeList = new JSONArray(json);
            //화면에 아무것도 출력되지 않는것 방지
            num = recipeList.length();
            if(num==0 && query!=null) {
                Log.i("jjjjjjjjjjjjjj","아무것도 일치하는게 없음");
                Intent intent = new Intent();
                intent.putExtra("result","no value");
                setResult(RESULT_OK,intent);
                finish();
            }

            for(int i=0; i<recipeList.length(); i++){
                Recipe recipeItem = new Recipe();

                JSONObject recipe = recipeList.getJSONObject(i);
                JSONArray descriptionInfo = recipe.getJSONArray("cooking_description");

                recipeItem.setRecipeId(recipe.getString("_id"));
                recipeItem.setName(recipe.getString("recipe_name"));
                recipeItem.setWriter(recipe.getString("writer_id"));
                recipeItem.setDifficulty(recipe.getInt("difficulty"));
                recipeItem.setRating(recipe.getLong("rating"));
                recipeItem.setServing(recipe.getInt("serving"));
                recipeItem.setDescriptionNum(descriptionInfo.length() + 2);

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
                if(query != null)
                    return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/recipe/search/?recipeName="+query);
                else if(category != null){
                    if(category.equals("한식"))
                        return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/recipe/getListbyCate/?cate=%ED%95%9C%EC%8B%9D&pageNum=0");
                    else if(category.equals("일식"))
                        return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/recipe/getListbyCate/?cate=%EC%9D%BC%EC%8B%9D&pageNum=0");
                }
                else if(ingredients != null){
                    return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/recipe/getByingredients?"+ingredients);
                }
                else
                    return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/recipe/getRecipeList/?pageNum=2");
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
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                Log.i("jjjjjjjjjjjjjjjjjj","something in " + s);
                jsonParser(s);
            }
            else if(s==null){
                Log.i("jjjjjjjjjjjjjjj","nothing in");
            }
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
