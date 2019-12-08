package com.example.yobo_android.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.MainActivity;
import com.squareup.picasso.Picasso;

/*
* MainActivity의 첫번째 layout에 띄워지는 Fragment
* 요리 추천 UI를 띄워준다. 선택 시 RecipeActivity로 이동
 */
public class RecipeRecomFragment extends Fragment {
    String imageSrc;
    String recipe_name;
    String description;
    Integer position;
    ImageView imv;
    public static RecipeRecomFragment newInstance(String imageSrc, String recipe_name, String description, int position) {
        RecipeRecomFragment fragment = new RecipeRecomFragment();
        Bundle args = new Bundle();
        args.putString("image", imageSrc);
        args.putString("name",recipe_name);
        args.putString("description",description);
        args.putInt("position",position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recipe_recom, container, false);
        imageSrc = getArguments().getString("image");
        recipe_name = getArguments().getString("name");
        description = getArguments().getString("description");
        position = getArguments().getInt("position");
        Uri uri = Uri.parse(imageSrc);
        imv = rootView.findViewById(R.id.imageView4);


        Picasso.get().load(uri).into(imv);
        return rootView;
    }
}
