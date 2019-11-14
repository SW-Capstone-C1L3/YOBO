package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.yobo_android.R;



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
    }


}
