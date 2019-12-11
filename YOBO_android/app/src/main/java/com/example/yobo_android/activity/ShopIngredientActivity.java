package com.example.yobo_android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopIngredientActivity extends AppCompatActivity {
    private ArrayList<String> image_list = new ArrayList<>();
    private ShopIngredientAdapter adapter;
    private LinearLayout mLayoutEmptyNotify;
    private LinearLayout mLayoutNonEmptyNotify;
    private SearchView mSearchview;
    private TextView mtoolbarTitle;
    private Button mbtnGoToBakset;
    private String query = null;
    private RecyclerView recyclerView;

    private int REQUEST_TEST =1000;
    private int REQUEST_SHOP =2000;
    List<ShoppingIngredientData> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_ingredient);
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_action, menu);
        final MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (android.widget.SearchView) mSearch.getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtoolbarTitle.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mtoolbarTitle.setVisibility(View.VISIBLE);
                return false;
            }
        });

        SearchView sv = (SearchView) mSearch.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplication(),ShopIngredientActivity.class);
                intent.putExtra("query",query);
                startActivity(intent);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
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
