package com.example.yobo_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.yobo_android.R;
import com.example.yobo_android.fragment.RecipeRecomFragment;
import com.google.android.material.navigation.NavigationView;

public class ShopIngredientActivity extends AppCompatActivity {
    private Button mBtnIng1;
    private Button mBtnIng2;
    private Button mBtnIng3;
    private Button mBtnIng4;
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
        mBtnIng1 = findViewById(R.id.btnIng1);
        mBtnIng2 = findViewById(R.id.btnIng2);
        mBtnIng3 = findViewById(R.id.btnIng3);
        mBtnIng4 = findViewById(R.id.btnIng4);
        mtoolbarTitle = findViewById(R.id.toolbar_title);
        mSearchview = findViewById(R.id.action_search);
        /*Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (v.getId()) {
                    case R.id.btnIng1:
                        intent = new Intent(ShopIngredientActivity.this, ShowIngredientActivity.class);
                        break;
                    case R.id.btnIng2:
                        intent = new Intent(ShopIngredientActivity.this, ShowIngredientActivity.class);
                        break;
                    case R.id.btnIng3:
                        intent = new Intent(ShopIngredientActivity.this, ShowIngredientActivity.class);
                        break;
                    case R.id.btnIng4:
                        intent = new Intent(ShopIngredientActivity.this, ShowIngredientActivity.class);
                        break;
                }
            }

        }
        mBtnIng1.setOnClickListener(onClickListener);
        mBtnIng2.setOnClickListener(onClickListener);
        mBtnIng3.setOnClickListener(onClickListener);
        mBtnIng4.setOnClickListener(onClickListener);

         */
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

//        //메뉴 아이콘 클릭했을 시 확장, 취소했을 시 축소
//        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                TextView text=(TextView)findViewById(R.id.txtstatus);
//                text.setText("현재 상태 : 확장됨");
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                TextView text=(TextView)findViewById(R.id.txtstatus);
//                text.setText("현재 상태 : 축소됨");
//                return true;
//            }
//        });

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
