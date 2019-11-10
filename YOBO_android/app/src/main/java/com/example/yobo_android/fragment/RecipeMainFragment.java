package com.example.yobo_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yobo_android.R;

/*
* RecipeActivity에 띄워지는 Fragment
* 1번째 fragment로 레시피 메인 사진, 이름 등을 나타내는 UI
 */

public class RecipeMainFragment extends Fragment {
    // newInstance constructor for creating fragment with arguments
    public static RecipeMainFragment newInstance(int page, String title) {
        Log.i("ddddddddddddd3","1");
        RecipeMainFragment fragment = new RecipeMainFragment();
        return fragment;
    }
    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_main, container, false);
        return view;
    }
}
