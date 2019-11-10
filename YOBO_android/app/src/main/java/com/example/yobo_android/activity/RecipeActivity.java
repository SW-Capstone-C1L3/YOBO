package com.example.yobo_android.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.fragment.RecipeDetailFragment;
import com.example.yobo_android.fragment.RecipeMainFragment;
import com.example.yobo_android.fragment.RecipeOrderFragment;

/*
* 레시피 Activity
* 1. RecipeMainFragment
* 2. RecipeDetailFragment
* 3. 이후 RecipeOrderFragment로 조리법 나열
*/

public class RecipeActivity extends FragmentActivity {

    private Fragment[] fragments = new Fragment[3];
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    private String recipeId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        recipeId = intent.getExtras().getString("recipeId");

        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm)
        {
            super(fm);

            fragments[0]= new RecipeMainFragment();
            fragments[1]= new RecipeDetailFragment();
            fragments[2]= new RecipeOrderFragment();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    public void selectIndex(int newIndex){
        mPager.setCurrentItem(newIndex);
    }
}
