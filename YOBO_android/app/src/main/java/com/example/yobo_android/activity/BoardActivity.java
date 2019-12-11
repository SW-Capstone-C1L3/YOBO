package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
* 검색에 따른 레시피 목록을 보여주는 Activity
* 레시피 item 선택 시 RecipeActivity 이동
 */

public class BoardActivity extends AppCompatActivity {

    List<Recipe> recipeList;

    private LinearLayout mLayoutEmptyNotify;
    private ImageButton mBtnBack;
    private TextView mToolbarTitle;
    private SearchView mSearchView;
    private RecyclerView recyclerView;
    private BoardAdapter adapter;

    private String query = null;
    private String category;
    private List<String> ingredients;
    private boolean shotcut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
    }

    @Override
    protected void onResume() {
        super.onResume();

      Toolbar toolbar = findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbarTitle = findViewById(R.id.toolbar_title);
        mBtnBack = findViewById(R.id.arrow_back_ImageButton);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        mSearchView = findViewById(R.id.toolbar_search);
        mSearchView.setVisibility(View.GONE);
        searchViewSetting();
        
        recyclerViewInit();

        if (getIntent().getStringExtra("query") != null) {
            query = getIntent().getStringExtra("query");
        }
        if (getIntent().getStringExtra("category") != null) {
            category = getIntent().getStringExtra("category");
        }
        if (getIntent().getStringArrayListExtra("ingredients") != null) {
            ingredients = getIntent().getStringArrayListExtra("ingredients");
        }
        if (getIntent().getBooleanExtra("shotcut",shotcut)) {
            shotcut = getIntent().getBooleanExtra("shotcut",shotcut);
        }

        recipeList = new ArrayList<>();
        recyclerViewInit();
        
        Call<List<Recipe>> call = null;

        if (query != null) {
            mSearchView.setVisibility(View.VISIBLE);
            call = RetrofitClient.getInstance().getApiService().search(query, 0, 10);
        }else if(category != null){
            call = RetrofitClient.getInstance().getApiService().getListByCate(category, 0, 10);
        }else if(ingredients != null){
            call = RetrofitClient.getInstance().getApiService().getByingredients(ingredients, 0, 10);
        }else if(shotcut) {
            call = RetrofitClient.getInstance().getApiService().getByshortcut(MainActivity.u_id, 0, 10);
        }else{
            call = RetrofitClient.getInstance().getApiService().getRecipeList(0,10);
        }

        if (call != null)
            callRecipeList(call);

    }

    private void callRecipeList(Call<List<Recipe>> call) {
        deleteItemForRefresh();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.i("TEST", call.toString());
                Log.i("TEST", response.toString());
                recipeList = response.body();
                if(recipeList.size()==0){
                    recyclerView.setVisibility(View.GONE);
                    mLayoutEmptyNotify.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mLayoutEmptyNotify.setVisibility(View.GONE);
                    for (int i = 0; i < recipeList.size(); i++) {
                        adapter.addItem(recipeList.get(i), i);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("ERROR", call.toString());
                Log.e("ERROR", t.toString());
            }
        });
    }

    private void deleteItemForRefresh() {
        for (int i = 0; i < recipeList.size(); i++) {
            adapter.deleteItem(i);
        }
    }

    private void searchViewSetting() {
        mSearchView.setQueryHint("레시피 검색");
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolbarTitle.setVisibility(View.GONE);
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mToolbarTitle.setVisibility(View.VISIBLE);
                return false;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Call<List<Recipe>> call = RetrofitClient.getInstance().getApiService().search(query,0,10);
                callRecipeList(call);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) { return true; }
        });
    }

    private void recyclerViewInit() {
        mLayoutEmptyNotify = findViewById(R.id.emptyNotifyLayout);
        mLayoutEmptyNotify.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerBoardView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BoardAdapter();
        recyclerView.setAdapter(adapter);
    }

}
