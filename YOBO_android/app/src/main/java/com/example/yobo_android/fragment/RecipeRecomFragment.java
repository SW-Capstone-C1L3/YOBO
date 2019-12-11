package com.example.yobo_android.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeMainActivity;
import com.squareup.picasso.Picasso;

/*
* MainActivity의 첫번째 layout에 띄워지는 Fragment
* 요리 추천 UI를 띄워준다. 선택 시 RecipeActivity로 이동
 */
public class RecipeRecomFragment extends Fragment {
    String recipeId;
    String imageSrc;
    String recipe_name;
    String description;
    Integer position;

    ImageView mRecommendedRecipeImage;
    TextView mRecommendedRecipeName;
    TextView mRecommendedReciepDescription;

    public static RecipeRecomFragment newInstance(String recipeId, String imageSrc, String recipe_name, String description, int position) {
        RecipeRecomFragment fragment = new RecipeRecomFragment();
        Bundle args = new Bundle();
        args.putString("recipeId", recipeId);
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
        recipeId = getArguments().getString("recipeId");
        imageSrc = getArguments().getString("image");
        recipe_name = getArguments().getString("name");
        description = getArguments().getString("description");
        position = getArguments().getInt("position");
        Uri uri = Uri.parse(imageSrc);

        mRecommendedRecipeImage = rootView.findViewById(R.id.recommendedRecipeImage);
        mRecommendedRecipeName = rootView.findViewById(R.id.recommendedRecipeName);
        mRecommendedReciepDescription = rootView.findViewById(R.id.recommendedRecipeDescription);

        Picasso.get().load(uri).into(mRecommendedRecipeImage);
        mRecommendedRecipeName.setText(recipe_name);
        mRecommendedReciepDescription.setText(description);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecipeMainActivity.class);
                intent.putExtra("recipeId",recipeId);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public String getRecipeId() {
        return recipeId;
    }
}
