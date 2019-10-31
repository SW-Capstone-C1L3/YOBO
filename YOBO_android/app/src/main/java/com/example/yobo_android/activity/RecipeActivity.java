package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.RecipeOrderAdapter;
import com.example.yobo_android.adapter.viewholder.SwipePagerAdapter;
import com.example.yobo_android.etc.RecipeOrder;
import com.example.yobo_android.fragment.RecipeDetailFragment;
import com.example.yobo_android.fragment.RecipeMainFragment;
import com.example.yobo_android.fragment.RecipeOrderFragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/*
* 레시피 Activity
* 1. RecipeMainFragment
* 2. RecipeDetailFragment
* 3. 이후 RecipeOrderFragment로 조리법 나열
*/

public class RecipeActivity extends FragmentActivity {

    private int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new Fragment();
            switch (position){
                case 0:
                    fragment = new RecipeMainFragment();
                    break;
                case 1:
                    fragment = new RecipeDetailFragment();
                    break;
                case 2:
                    fragment = new RecipeOrderFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    // XXXXXXXX
    /*private void init(){
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
    }*/
}
