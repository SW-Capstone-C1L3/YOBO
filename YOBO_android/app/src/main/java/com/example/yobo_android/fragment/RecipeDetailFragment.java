package com.example.yobo_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeActivity;

/*
* RecipeActivity에 띄워지는 Fragment
* 2번째 fragment로 레시피의 상세정보제공 UI
*/

public class RecipeDetailFragment extends Fragment {
    Button btn;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        btn =(Button)view.findViewById(R.id.btn_startRecipe);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecipeActivity)getActivity()).selectIndex(2);
            }
        });
        return view;
    }
}
