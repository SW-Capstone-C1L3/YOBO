package com.example.yobo_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yobo_android.R;

/*
* MainActivity의 첫번째 layout에 띄워지는 Fragment
* 요리 추천 UI를 띄워준다. 선택 시 RecipeActivity로 이동
 */
public class RecipeRecomFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recipe_recom, container, false);

        return rootView;
    }
}
