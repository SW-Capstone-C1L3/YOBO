package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.yobo_android.R;

public class RecipeFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton mBtnBack = findViewById(R.id.arrow_back2_ImageButton);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton mBtnWriteRecipe = findViewById(R.id.done_ImageButton);
        mBtnWriteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageView cookingDescriptionImage = findViewById(R.id.cooking_description_image);
    }
}
