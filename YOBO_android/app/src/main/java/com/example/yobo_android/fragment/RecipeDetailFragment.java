package com.example.yobo_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeActivity;


public class RecipeDetailFragment extends Fragment {

    // newInstance constructor for creating fragment with arguments
    public static RecipeDetailFragment newInstance(int page, String title) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        return view;
    }
}
