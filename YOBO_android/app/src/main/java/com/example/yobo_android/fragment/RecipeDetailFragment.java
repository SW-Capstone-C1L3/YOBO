package com.example.yobo_android.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeActivity;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.adapter.viewholder.RecipeOrderAdapter;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.RecipeOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
* RecipeActivity에 띄워지는 Fragment
* 2번째 fragment로 레시피의 상세정보제공 UI
*/

public class RecipeDetailFragment extends Fragment {

    private static final String ARG_RECIPE_ID ="";

    private static String recipeId;

    private RecipeOrderAdapter adapter;

    private TextView detailRecipeServing;
    private TextView detailRecipeDifficulty;
    private TextView detailRecipeRating;
    private TextView detailRecipeCategory;
    private TextView detailRecipeMainIngredient;
    private TextView detailRecipeSubIngredient;
    private RecyclerView recyclerView;

    Button btn;

    // newInstance constructor for creating fragment with arguments
    public static RecipeDetailFragment newInstance(String recipeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }
    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            recipeId = getArguments().getString(ARG_RECIPE_ID);
        }
    }

    public void jsonParser(String json) {
        try {
            JSONObject recipeInfo = new JSONObject(json);

            JSONArray categoryInfo = recipeInfo.optJSONArray("category");
            JSONArray mainIngredientInfo = recipeInfo.getJSONArray("main_cooking_ingredients");
            JSONArray subIngredientInfo = recipeInfo.getJSONArray("sub_cooking_ingredients");
            JSONArray descriptionInfo = recipeInfo.getJSONArray("cooking_description");

            String tmp = "";
            //category[]
            for(int i=0; i< categoryInfo.length(); i++){
                tmp = tmp + categoryInfo.getString(i) + " ";
            }
            detailRecipeCategory.setText(tmp);

            //ingredient[]
            tmp = "";
            for(int i=0; i< mainIngredientInfo.length(); i++){
                tmp = tmp + mainIngredientInfo.getJSONObject(i).getString("ingredients_name") + "(";
                tmp = tmp + mainIngredientInfo.getJSONObject(i).getString("qty");
                tmp = tmp + mainIngredientInfo.getJSONObject(i).getString("unit") + ") ";
            }
            detailRecipeMainIngredient.setText(tmp);
            tmp = "";
            for(int i=0; i< mainIngredientInfo.length(); i++){
                tmp = tmp + subIngredientInfo.getJSONObject(i).getString("ingredients_name") + "(";
                tmp = tmp + subIngredientInfo.getJSONObject(i).getString("qty");
                tmp = tmp + subIngredientInfo.getJSONObject(i).getString("unit") + ") ";
            }
            detailRecipeSubIngredient.setText(tmp);

            detailRecipeServing.setText(recipeInfo.getString("serving")+"인분");
            detailRecipeDifficulty.setText(recipeInfo.getString("difficulty")+"");
            detailRecipeRating.setText(recipeInfo.getString("rating")+"점");

            //order[]
            for(int i=0; i<descriptionInfo.length(); i++){
                RecipeOrder descriptionItem = new RecipeOrder();
                descriptionItem.setRecipeOrderDescription((i+1) + ". " + descriptionInfo.getJSONObject(i).getString("description"));
                adapter.addItem(descriptionItem);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                //GET Request
                return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/recipe/getRecipebyDid/?Did="+recipeId);
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                jsonParser(s);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        new RequestAsync().execute();

        detailRecipeCategory = view.findViewById(R.id.detailRecipeCategory);
        detailRecipeMainIngredient = view.findViewById(R.id.detailRecipeMainIngredient);
        detailRecipeSubIngredient = view.findViewById(R.id.detailRecipeSubIngredient);
        detailRecipeDifficulty = view.findViewById(R.id.detailRecipeDifficulty);
        detailRecipeServing = view.findViewById(R.id.detailRecipeServing);
        detailRecipeRating = view.findViewById(R.id.detailRecipeRating);

        recyclerView = view.findViewById(R.id.recyclerRecipeDetailView);

        recyclerViewInit();
        btn =(Button)view.findViewById(R.id.btn_startRecipe);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecipeActivity)getActivity()).selectIndex(2);
            }
        });

        return view;
    }

    private void recyclerViewInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecipeOrderAdapter();
        recyclerView.setAdapter(adapter);
    }
}
