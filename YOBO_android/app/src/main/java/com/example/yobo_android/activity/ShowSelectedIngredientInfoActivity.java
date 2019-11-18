package com.example.yobo_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.yobo_android.R;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.fragment.BottomSheetFragment;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;


public class ShowSelectedIngredientInfoActivity extends AppCompatActivity{

    HashMap<String,Object> hashMap = new HashMap<>();
    Retrofit retrofit;
    ApiService apiService;

    private SearchView mSearchview;
    private TextView mtoolbarTitle;
    private TextView mIngredientName;
    private TextView mIngredientPrice;
    private TextView mCompanyName;
    private TextView mIngredientDescription;
    private Button mBuy;
    private String Ingredient_id;
    private String IngredientPrice;
    private final String Tag = "abcde";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ingredient);
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        String IngredientName = intent.getExtras().getString("Ingredient_name");
        Ingredient_id = intent.getExtras().getString("Ingredient_id");
        IngredientPrice = intent.getExtras().getString("Ingredient_price");
        String IngredientUnit = intent.getExtras().getString("Ingredient_unit");
        String IngredientDescription = intent.getExtras().getString("Ingredient_description");
        String CompanyName = intent.getExtras().getString("Company_name");
        mIngredientName = findViewById(R.id.textView_ingredient_name);
        mIngredientPrice = findViewById(R.id.textView_ingredient_price);
        mIngredientDescription = findViewById(R.id.textView_ingredient_description);
        mCompanyName = findViewById(R.id.textView_company_name);
        mtoolbarTitle = findViewById(R.id.toolbar_title);
        mSearchview = findViewById(R.id.action_search);
        mBuy = findViewById(R.id.btnBuy);
        mBuy.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFragment fragment = new BottomSheetFragment();
                fragment.show(getSupportFragmentManager(), "TAG");
            }
        });
        mIngredientName.setText(IngredientName);
        mIngredientPrice.setText(IngredientPrice+"/"+IngredientUnit);
        mIngredientDescription.setText(IngredientDescription);
        mCompanyName.setText(CompanyName);
    }

    public String giveVal(){
        String substr = IngredientPrice.substring(0,IngredientPrice.length()-1);
        return substr;
    }
    public void goToBasket(int amount){       //장바구니로 가기
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        String product_id = Ingredient_id;
        int qty = amount;
        String userId = "5dc6e8de068a0d0928838088";
        hashMap.put("Product_id", product_id);
        hashMap.put("qty",qty);
        hashMap.put("User_id",userId);
        Call<ResponseBody> call = apiService.insertBasket(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(ShowSelectedIngredientInfoActivity.this,"GOOD",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ShowSelectedIngredientInfoActivity.this,"WRONG",Toast.LENGTH_LONG).show();
            }
        });



        Intent intent = new Intent(ShowSelectedIngredientInfoActivity.this, BasketActivity.class);
        startActivity(intent);
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
                mtoolbarTitle.setVisibility(View.GONE);
            }
        });
        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
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
