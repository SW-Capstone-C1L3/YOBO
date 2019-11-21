package com.example.yobo_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yobo_android.R;

/*
* MainActivity의 첫번째 layout에 띄워지는 Fragment
* 요리 추천 UI를 띄워준다. 선택 시 RecipeActivity로 이동
 */
public class RecipeRecomFragment extends Fragment {
    private static String ARG_IMAGE_SRC;

    String imageSrc;

    public static RecipeRecomFragment newInstance(String imageSrc) {
        RecipeRecomFragment fragment = new RecipeRecomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_SRC, imageSrc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recipe_recom, container, false);
        imageSrc = getArguments().getString(ARG_IMAGE_SRC);
        switch (imageSrc){
            case "0":
                ((ImageView)rootView.findViewById(R.id.imageView4)).setBackgroundResource(R.drawable.recom1);
                break;
            case "1":
                ((ImageView)rootView.findViewById(R.id.imageView4)).setBackgroundResource(R.drawable.recom2);
                break;
            case "2":
                ((ImageView)rootView.findViewById(R.id.imageView4)).setBackgroundResource(R.drawable.recom3);
                break;
        }
        return rootView;
    }
}
