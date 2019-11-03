package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.yobo_android.R;
import com.example.yobo_android.fragment.RecipeDetailFragment;
import com.example.yobo_android.fragment.RecipeMainFragment;
import com.example.yobo_android.fragment.RecipeOrderFragment;
import com.example.yobo_android.fragment.RecipeRecomFragment;
import com.google.android.material.navigation.NavigationView;

/*
* 레시피 목록을 보여주는 BoardActivity
* 1. RecipeDetailFragment로 요리추천 -> RecipeActivity로 이동
* 2. 요리재료검색 선택 -> ChoiceIngredientActivity로 이동
* 3. 요리카테고리검색 선택 -> CategorySearchActivity로 이동
*/

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

//    private Button mBtnRecipeRecommendation;
    private Button mBtnChoiceIngredient;
    private Button mBtnRecipeCategory;

    // for recipe recommendation
    private int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mPager = (ViewPager) findViewById(R.id.pagerMain);
        pagerAdapter = new MainActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        ImageButton mBtnOpen = findViewById(R.id.menuImageButton);
        mBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mNavigationView);
            }
        });

        //login test용 버튼
        Button b =findViewById(R.id.loginbnt);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, // 현재 화면의 제어권자
                        NaverLoginActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent);            }
        });



//        mBtnRecipeRecommendation = findViewById(R.id.btnRecipeRecommendation);
        mBtnChoiceIngredient = findViewById(R.id.btnChoiceIngredient);
        mBtnRecipeCategory = findViewById(R.id.btnRecipeCategory);

        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                switch(v.getId()){
//                    case R.id.btnRecipeRecommendation:
//                        intent = new Intent(MainActivity.this, RecipeActivity.class);
//                        break;

                    case R.id.btnChoiceIngredient:
                        intent = new Intent(MainActivity.this, ChoiceIngredientActivity.class);
                        break;

                    case R.id.btnRecipeCategory:
                        intent = new Intent(MainActivity.this, CategorySearchActivity.class);
                        break;

                }
                startActivity(intent);
            }
        };
//        mBtnRecipeRecommendation.setOnClickListener(onClickListener);
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new RecipeRecomFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
