package com.example.yobo_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.yobo_android.R;
import com.example.yobo_android.fragment.BottomSheetFragment;

import org.w3c.dom.Text;


public class ShowSelectedIngredientInfoActivity extends AppCompatActivity{

    private SearchView mSearchview;
    private TextView mtoolbarTitle;
    private TextView mIngredientName;
    private TextView mIngredientPrice;
    private TextView mIngredientUnit;
    private TextView mCompanyName;
    private TextView mIngredientDescription;
    private Button mBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ingredient);
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        String IngredientName = intent.getExtras().getString("Ingredient_name");
        String IngredientPrice = intent.getExtras().getString("Ingredient_price");
        String IngredientUnit = intent.getExtras().getString("Ingredient_unit");
        String IngredientDescription = intent.getExtras().getString("Ingredient_description");
        String CompanyName = intent.getExtras().getString("Company_name");
        mIngredientName.setText(IngredientName);
        mIngredientPrice.setText(IngredientPrice);
        mIngredientUnit.setText(IngredientUnit);
        mIngredientDescription.setText(IngredientDescription);
        mCompanyName.setText(CompanyName);

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
    }

    public void goToBasket(){       //장바구니로 가기
        Intent intent = new Intent(ShowSelectedIngredientInfoActivity.this,basketActivity.class);
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
