package com.example.yobo_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yobo_android.R;

/*
 * RecipeActivity에 띄워지는 Fragment
 * 3번째부터의 모든 fragment로 레시피의 조리 순서를 나타내는 UI
 */

public class RecipeOrderFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recipe_order, container, false);

        return rootView;
    }
}
