package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.yobo_android.R;
import com.example.yobo_android.api.FileUploadAPI;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.api.RequestRetroConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;



/*
* 레시피를 작성하는 Activity
 */

public class EnrollRecipeActivity extends AppCompatActivity {

    int arraySize = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_recipe);

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
                Intent intent = new Intent(EnrollRecipeActivity.this, RecipeFormActivity.class);
                startActivity(intent);
            }
        });

//        new RequestAsync().execute();
    }
    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                // POST Request
                JSONObject postDataParams = new JSONObject();
                JSONArray objArr;
                JSONObject obj;


                objArr = new JSONArray();
                for(int i=0; i<arraySize; i++){
                    objArr.put(i+"식");
                }
                postDataParams.put("category",objArr);

                objArr = new JSONArray();
                for(int i=0;i<arraySize;i++){
                    obj = new JSONObject();
                    obj.put("description","description");
                    obj.put("image","image");
                    objArr.put(obj);
                }
                postDataParams.put("cooking_description",objArr.toString());

                postDataParams.put("name", "Manjeet");

                objArr = new JSONArray();
                for(int i=0;i<arraySize;i++){
                    obj = new JSONObject();
                    obj.put("ingredient_name",i+" ingre");
                    obj.put("qty", 44);
                    obj.put("unit","44인분");
                    objArr.put(obj);
                }
                postDataParams.put("main_cooking_ingredient",objArr.toString());

                postDataParams.put("rating", 44);
                postDataParams.put("name", "윤석");
                postDataParams.put("serving", 44);
                postDataParams.put("difficulty", 44);

                objArr = new JSONArray();
                for(int i=0;i<arraySize;i++){
                    obj = new JSONObject();
                    obj.put("ingredient_name",i+" ingre");
                    obj.put("qty", 44);
                    obj.put("unit","44인분");
                    objArr.put(obj);
                }
                postDataParams.put("sub_cooking_ingredient",objArr.toString());

                postDataParams.put("writer_id", "dmzld");

                return RequestHttpURLConnection.sendPost("http://45.119.146.82:8081/yobo/recipe/createRecipe/", postDataParams);
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
//                jsonParser(s);
            }
        }
    }
}
