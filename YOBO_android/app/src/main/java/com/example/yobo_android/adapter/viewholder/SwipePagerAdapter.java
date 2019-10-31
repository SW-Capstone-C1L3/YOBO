package com.example.yobo_android.adapter.viewholder;

import com.example.yobo_android.fragment.RecipeDetailFragment;
import com.example.yobo_android.fragment.RecipeMainFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SwipePagerAdapter extends FragmentStatePagerAdapter{

    public SwipePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new RecipeMainFragment();
            case 1:
                // Games fragment activity
                return new RecipeDetailFragment();
        }
        return null;
    }

    @Override
    public int getCount(){
        return 2;
    }
}
