package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.fragment.RecipeRecomFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

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
    private int REQUEST_TEST =1000;
    private int REQUEST_NAVER=2000;
//    private Button mBtnRecipeRecommendation;
    private LinearLayout mBtnChoiceIngredient;
    private LinearLayout mBtnRecipeCategory;
    private LinearLayout mBtnShop;
    private LinearLayout mBtnWriteRecipe;
    private TextView mtoolbarTitle;
    // for recipe recommendation
    private int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private ImageButton mBtnOpen;
    // user Info in nav header
    private TextView nav_header_user_name;
    private TextView nav_header_user_id;
    private Integer num=0;
    private Button mBtnLoginInNavHeader;
    private String u_id;
    Thread thread = null;
    Handler handler = null;
    int p=0;	//페이지번호
    int v=1;

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
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBtnShop.getLayoutParams();
//        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) mBtnWriteRecipe.getLayoutParams();
//        LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) mBtnChoiceIngredient.getLayoutParams();
//        LinearLayout.LayoutParams lp4 = (LinearLayout.LayoutParams) mBtnRecipeCategory.getLayoutParams();
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        permissionCheck();

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
        View header = mNavigationView.getHeaderView(0);
        mBtnLoginInNavHeader = (Button) header.findViewById(R.id.loginButtonInNavHeader);
        mBtnLoginInNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, // 현재 화면의 제어권자
                        NaverLoginActivity.class); // 다음 넘어갈 클래스 지정
                startActivityForResult(intent,REQUEST_NAVER);
            }
        });

//        mBtnRecipeRecommendation = findViewById(R.id.btnRecipeRecommendation);
        mBtnChoiceIngredient = findViewById(R.id.btnChoiceIngredient);
        mBtnRecipeCategory = findViewById(R.id.btnRecipeCategory);
        mBtnShop = findViewById(R.id.btnShop);
        mBtnWriteRecipe = findViewById(R.id.btnWriteRecipe);
//        Log.i("kkkkkkk",getScreenSize(this).x+"");
//        lp.height = getScreenSize(this).x;
//        lp2.height = getScreenSize(this).x;
//        lp3.height = getScreenSize(this).x;
//        lp4.height = getScreenSize(this).x;
//        mBtnShop.setLayoutParams(lp);
//        mBtnWriteRecipe.setLayoutParams(lp2);
//        mBtnChoiceIngredient.setLayoutParams(lp3);
//        mBtnRecipeCategory.setLayoutParams(lp4);
        LinearLayout.OnClickListener onClickListener = new LinearLayout.OnClickListener(){
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

                    case R.id.btnWriteRecipe:
                        intent = new Intent(MainActivity.this, RecipeFormActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };
//        mBtnRecipeRecommendation.setOnClickListener(onClickListener);
        mBtnChoiceIngredient.setOnClickListener(onClickListener);
        mBtnRecipeCategory.setOnClickListener(onClickListener);
        mBtnShop.setOnClickListener(onClickListener);
        mBtnWriteRecipe.setOnClickListener(onClickListener);

        handler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                if(p==0)
                    mPager.setCurrentItem(1);
                else if(p==1)
                    mPager.setCurrentItem(2);
                else if(p==2)
                    mPager.setCurrentItem(0);
                p=(p+1)%3;
            }
        };
        thread = new Thread(){
            //run은 jvm이 쓰레드를 채택하면, 해당 쓰레드의 run메서드를 수행한다.
            public void run() {
                super.run();
                while(true){
                    try {
                        Thread.sleep(5000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
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
        sv.setQueryHint("레시피 검색");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

//          //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TextView text = (TextView) findViewById(R.id.txtresult);
                //text.setText(query + "를 검색합니다.");
                Intent intent = new Intent(getApplication(),BoardActivity.class);
                intent.putExtra("query",query);
                startActivityForResult(intent,REQUEST_TEST);
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
            switch (position){
                case 0:
                    return RecipeRecomFragment.newInstance("0");
                case 1:
                    return RecipeRecomFragment.newInstance("1");
                case 2:
                    return RecipeRecomFragment.newInstance("2");
                default:
                    return RecipeRecomFragment.newInstance("0");
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
    public void changeImage(int idx){
        int nexIdx = (idx+1)%3;
        mPager.setCurrentItem(nexIdx,true);
    }
    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> arrayPermission = new ArrayList<>();

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (arrayPermission.size() > 0) {
                String[] strArray = new String[arrayPermission.size()];
                strArray = arrayPermission.toArray(strArray);
                ActivityCompat.requestPermissions(this, strArray, 1000);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults.length < 1) {
                    Toast.makeText(this, "Failed get permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission is denied : " + permissions[i], Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //검색 일치 항목이 존재하지 않는 경우 상단에 snackbar가 뜨도록 설정하는 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TEST) {
            if (resultCode == RESULT_OK) {
                String snackBarMessage;
                snackBarMessage = "일치하는 항목이 존재하지 않습니다.";
                Snackbar make = Snackbar.make(getWindow().getDecorView().getRootView(),
                        snackBarMessage, Snackbar.LENGTH_LONG);
                View view = make.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
                params.gravity = Gravity.TOP;
                params.width = getScreenSize(this).x;
                make.setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                make.setActionTextColor(Color.RED);
                make.show();
            }
        }
        else if(requestCode == REQUEST_NAVER){
            u_id = data.getStringExtra("result");
            Log.i("kkkkk main, u_id",u_id);
        }
    }
    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size;
    }
}
