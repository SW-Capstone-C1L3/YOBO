package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.yobo_android.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class RecipeActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe2);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        CollapsingToolbarLayout mCollapseToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        mCollapseToolBar.setTitle("gdgd");
    }
}
