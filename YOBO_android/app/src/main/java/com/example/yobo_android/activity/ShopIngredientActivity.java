package com.example.yobo_android.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.adapter.viewholder.ShopIngredientAdapter;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.etc.ShoppingIngredientData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//장보기 눌렀을때 뜨는 화면
public class ShopIngredientActivity extends AppCompatActivity {

    private ShopIngredientAdapter adapter;
    private SearchView mSearchview;
    private TextView mtoolbarTitle;
    MenuItem mSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_ingredient);
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        mtoolbarTitle = findViewById(R.id.toolbar_title);
        mSearchview = findViewById(R.id.action_search);
        recyclerViewInit();
        new ShopIngredientActivity.RequestAsync().execute();
    }
    public void jsonParser(String json) {
        try {
            JSONArray ShopIngredientList = new JSONArray(json);
            Log.i("hhhhhhhhhhhhhhhhh",String.valueOf(ShopIngredientList.length()));
            for(int i=0; i<ShopIngredientList.length(); i++){
                ShoppingIngredientData ingreditem = new ShoppingIngredientData();
                JSONObject ingredient = ShopIngredientList.getJSONObject(i);
                ingreditem.setSel_id(ingredient.getString("_id"));
                ingreditem.setProduct_name(ingredient.getString("product_name"));
                ingreditem.setProduct_price(ingredient.getInt("product_price"));
                ingreditem.setProduct_category(ingredient.getString("product_category"));
                ingreditem.setProduct_qty(ingredient.getInt("product_qty"));
                ingreditem.setProduct_unit(ingredient.getString("product_unit"));
                ingreditem.setProduct_description(ingredient.getString("product_description"));
                ingreditem.setProvided_company_id(ingredient.getString("product_company_id"));
                ingreditem.setCompany_name(ingredient.getString("company_name"));
                ingreditem.setProduct_image(ingredient.getString("product_image"));
                adapter.addItem(ingreditem);
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
                return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/product/getProducteList/?pageNum=0");
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

    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.recyclerShopIngredientView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ShopIngredientAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        //search_menu.xml 등록
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_action, menu);
        final MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (android.widget.SearchView) mSearch.getActionView();

        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("hhhhhhhhhhhhhhhh","title gone");
                mtoolbarTitle.setVisibility(View.GONE);
            }
        });
        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.i("hhhhhhhhhhhhhhhh","title visible");
                mtoolbarTitle.setVisibility(View.VISIBLE);
                return false;
            }
        });

        SearchView sv = (SearchView) mSearch.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //
//            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TextView text = (TextView) findViewById(R.id.txtresult);
                //text.setText(query + "를 검색합니다.");
                return true;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                //TextView text = (TextView) findViewById(R.id.txtsearch);
                //text.setText("검색식 : " + newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
