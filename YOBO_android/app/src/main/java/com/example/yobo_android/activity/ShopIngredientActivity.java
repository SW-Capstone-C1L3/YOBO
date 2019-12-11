package com.example.yobo_android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.ShopIngredientAdapter;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.ShoppingIngredientData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//장보기 눌렀을때 뜨는 화면
public class ShopIngredientActivity extends AppCompatActivity {
    private ArrayList<String> image_list = new ArrayList<>();
    private ShopIngredientAdapter adapter;
    private LinearLayout mLayoutEmptyNotify;
    private LinearLayout mLayoutNonEmptyNotify;
    private SearchView mSearchview;
    private TextView mtoolbarTitle;
    private ImageButton mBtnBack;
    private Button mbtnGoToBakset;
    private String query = null;
    private RecyclerView recyclerView;
    MenuItem mSearch;

    private int REQUEST_TEST =1000;
    private int REQUEST_SHOP =2000;
    List<ShoppingIngredientData> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_ingredient);
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        mBtnBack = findViewById(R.id.arrow_back_ImageButton);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mbtnGoToBakset = findViewById(R.id.btnGoToBasket);
        if (getIntent().getStringExtra("query") != null) {
            query = getIntent().getStringExtra("query");
        }
        recyclerViewInit();
        Call<List<ShoppingIngredientData>> call = null;
        if (query != null)
            call = RetrofitClient.getInstance().getApiService().searchProduct(query, 0, 20);
        else
            call = RetrofitClient.getInstance().getApiService().getProductList(0,20);

        if (call != null) {
            call.enqueue(new Callback<List<ShoppingIngredientData>>() {
                @Override
                public void onResponse(Call<List<ShoppingIngredientData>> call, Response<List<ShoppingIngredientData>> response) {
                    //Toast.makeText(BoardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    ingredientList = response.body();
                    if(ingredientList.isEmpty()){
                        mLayoutEmptyNotify.setVisibility(View.VISIBLE);
                        mLayoutNonEmptyNotify.setVisibility(View.GONE);
                    }
                    else {
                        for (int i = 0; i < ingredientList.size(); i++) {
                            adapter.addItem(ingredientList.get(i), i);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<ShoppingIngredientData>> call, Throwable t) {
                    Log.e("ERROR", call.toString());
                    Log.e("ERROR", t.toString());
                }
            });
        }

        mbtnGoToBakset.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.u_id == null){
                    showLoginAlertDialog();
                }
                else {
                    adapter.notifyItemChanged(0);
                    Intent intent = new Intent(ShopIngredientActivity.this, BasketActivity.class);
                    startActivityForResult(intent,REQUEST_SHOP);
                }
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        mtoolbarTitle = findViewById(R.id.toolbar_title);
        mSearchview = findViewById(R.id.action_search);

    }

    private void recyclerViewInit() {
        mLayoutEmptyNotify = findViewById(R.id.emptyNotifyLayout);
        mLayoutEmptyNotify.setVisibility(View.GONE);

        mLayoutNonEmptyNotify = findViewById(R.id.nonEmptyLayout);
        recyclerView = findViewById(R.id.recyclerShopIngredientView);
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
            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TextView text = (TextView) findViewById(R.id.txtresult);
                //text.setText(query + "를 검색합니다.");
                Intent intent = new Intent(getApplication(),ShopIngredientActivity.class);
                intent.putExtra("query",query);
                startActivity(intent);
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
    public void showLoginAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_error_outline_24dp);
        builder.setTitle("로그인 해주세요 :(");
        builder.setMessage("로그인을 해야 가능합니다.");
        builder.setNegativeButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
}
