package com.example.yobo_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeActivity;

/*
* RecipeActivity에 띄워지는 Fragment
* 2번째 fragment로 레시피의 상세정보제공 UI
*/

public class RecipeDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        return rootView;
    }

}
