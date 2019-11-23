package com.example.yobo_android.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.api.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "";
    private static final String ARG_DESCRIPTION = "";

    String recipeId;
    String description;
    TextView tvvv;

    public static TestFragment newInstance(String recipeId, String description) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_ID, recipeId);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            recipeId = getArguments().getString(ARG_RECIPE_ID);
            description = getArguments().getString(ARG_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ((TextView)view.findViewById(R.id.tvvv)).setText(description);
        Log.i("asdasd","createView"+description);
        return view;
    }
}