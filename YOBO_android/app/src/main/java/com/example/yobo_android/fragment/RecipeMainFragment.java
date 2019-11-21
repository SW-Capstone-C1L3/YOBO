package com.example.yobo_android.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.Recipe;

import org.json.JSONException;
import org.json.JSONObject;

/*
* RecipeActivity에 띄워지는 Fragment
* 1번째 fragment로 레시피 메인 사진, 이름 등을 나타내는 UI
 */

public class RecipeMainFragment extends Fragment {

    private static final String ARG_RECIPE_ID ="";

    private static String recipeId;

    private TextView detailRecipeName;
    private TextView detailRecipeWriter;
    private TextView detailRecipeSubDescription;

    // newInstance constructor for creating fragment with arguments
    public static RecipeMainFragment newInstance(String recipeId) {
        RecipeMainFragment fragment = new RecipeMainFragment();
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

            detailRecipeName.setText(recipeInfo.getString("recipe_name"));
            detailRecipeWriter.setText(recipeInfo.getString("writer_id"));
            detailRecipeSubDescription.setText("");

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
        View view = inflater.inflate(R.layout.fragment_recipe_main, container, false);

        new RequestAsync().execute();

        detailRecipeName = view.findViewById(R.id.detailRecipeName);
        detailRecipeWriter = view.findViewById(R.id.detailRecipeWriter);
        detailRecipeSubDescription = view.findViewById(R.id.detailRecipeSubDescription);

        return view;
    }
}
