package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.fragment.RecipeDetailFragment;
import com.example.yobo_android.fragment.RecipeMainFragment;
import com.example.yobo_android.fragment.RecipeOrderFragment;
import com.example.yobo_android.fragment.RecipeRecomFragment;
import com.google.android.material.navigation.NavigationView;

/*
* 레시피 목록을 보여주는 BoardActivity
* 1. RecipeDetailFragment로 요리추천 -> RecipeActivity로 이동
* 2. 요리재료검색 선택 -> ChoiceIngredientActivity로 이동
* 3. 요리카테고리검색 선택 -> CategorySearchActivity로 이동
*/

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MenuItem mSearch;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

//    private Button mBtnRecipeRecommendation;
    private Button mBtnChoiceIngredient;
    private Button mBtnRecipeCategory;
    private Button mBtnShop;
    private TextView mtoolbarTitle;
    // for recipe recommendation
    private int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private ImageButton mBtnOpen;
    private Button mBtnLogin;
    // user Info in nav header
    private TextView nav_header_user_name;
    private TextView nav_header_user_id;

    public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mtoolbarTitle = findViewById(R.id.toolbar_title);
        mPager = (ViewPager) findViewById(R.id.pagerMain);
        pagerAdapter = new MainActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        mBtnOpen = findViewById(R.id.menuImageButton);
        mBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mNavigationView);
            }
        });

        //login test용 버튼
        mBtnLogin =findViewById(R.id.loginbnt);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, // 현재 화면의 제어권자
                        NaverLoginActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent);            }
        });



//        mBtnRecipeRecommendation = findViewById(R.id.btnRecipeRecommendation);
        mBtnChoiceIngredient = findViewById(R.id.btnChoiceIngredient);
        mBtnRecipeCategory = findViewById(R.id.btnRecipeCategory);
        mBtnShop = findViewById(R.id.btnShop);
        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                switch(v.getId()){
//                    case R.id.btnRecipeRecommendation:
//                        intent = new Intent(MainActivity.this, RecipeActivity.class);
//                        break;

                    case R.id.btnChoiceIngredient:
                        intent = new Intent(MainActivity.this, ChoiceIngredientActivity.class);
                        break;

                    case R.id.btnRecipeCategory:
                        intent = new Intent(MainActivity.this, CategorySearchActivity.class);
                        break;

                    case R.id.btnShop:
                        intent = new Intent(MainActivity.this, ShopIngredientActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };
//        mBtnRecipeRecommendation.setOnClickListener(onClickListener);
        mBtnChoiceIngredient.setOnClickListener(onClickListener);
        mBtnRecipeCategory.setOnClickListener(onClickListener);
        mBtnShop.setOnClickListener(onClickListener);
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
                mBtnLogin.setVisibility(View.GONE);
            }
        });
        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.i("hhhhhhhhhhhhhhhh","title visible");
                mtoolbarTitle.setVisibility(View.VISIBLE);
                mBtnLogin.setVisibility(View.VISIBLE);
                return false;
            }
        });

        SearchView sv = (SearchView) mSearch.getActionView();
        sv.setQueryHint("레시피 검색");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

//          //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TextView text = (TextView) findViewById(R.id.txtresult);
                //text.setText(query + "를 검색합니다.");
                Intent intent = new Intent(getApplication(),BoardActivity.class);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = new Intent();
        if (id == R.id.nav_enroll_recipe) {
            intent = new Intent(MainActivity.this,EnrollRecipeActivity.class);
        }else if(id == R.id.nav_scrap_recipe){
            intent = new Intent(MainActivity.this,BoardActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new RecipeRecomFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
