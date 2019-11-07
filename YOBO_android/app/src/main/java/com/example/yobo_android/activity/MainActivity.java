package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.yobo_android.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private Button mBtnRecipeRecommendation;
    private Button mBtnChoiceIngredient;
    private Button mBtnRecipeCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        ImageButton mBtnOpen = findViewById(R.id.menuImageButton);
        mBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mNavigationView);
            }
        });

        mBtnRecipeRecommendation = findViewById(R.id.btnRecipeRecommendation);
        mBtnChoiceIngredient = findViewById(R.id.btnChoiceIngredient);
        mBtnRecipeCategory = findViewById(R.id.btnRecipeCategory);

        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                switch(v.getId()){
                    case R.id.btnRecipeRecommendation:
                        intent = new Intent(MainActivity.this, RecipeActivity.class);
                        break;

                    case R.id.btnChoiceIngredient:
                        intent = new Intent(MainActivity.this, ChoiceIngredientActivity.class);
                        break;

                    case R.id.btnRecipeCategory:
                        intent = new Intent(MainActivity.this, BoardActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };
        mBtnRecipeRecommendation.setOnClickListener(onClickListener);
        mBtnChoiceIngredient.setOnClickListener(onClickListener);
        mBtnRecipeCategory.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = new Intent();
        if (id == R.id.nav_enroll_recipe) {
            intent = new Intent(MainActivity.this,EnrollRecipeActivity.class);
        }else if(id == R.id.nav_scrap_recipe){
            intent = new Intent(MainActivity.this,BoardActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

}
