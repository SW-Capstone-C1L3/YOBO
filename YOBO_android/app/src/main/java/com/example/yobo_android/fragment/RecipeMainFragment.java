package com.example.yobo_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yobo_android.R;

/*
* RecipeActivity에 띄워지는 Fragment
* 1번째 fragment로 레시피 메인 사진, 이름 등을 나타내는 UI
 */

public class RecipeMainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recipe_main, container, false);

        return rootView;
    }
}
