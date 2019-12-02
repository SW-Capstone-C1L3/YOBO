package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
    private TextView nav_header_user_email;
    private Integer num=0;
    private Button mBtnLoginInNavHeader;
    public static String u_id;
    public static String u_phone;
    public static String u_name;
    public static String u_email;
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

        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);
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
                if(mBtnLoginInNavHeader.getText().equals("로그인")) {
                    Intent intent = new Intent(MainActivity.this, // 현재 화면의 제어권자
                            NaverLoginActivity.class); // 다음 넘어갈 클래스 지정
                    startActivityForResult(intent, REQUEST_NAVER);
                }
                else if(mBtnLoginInNavHeader.getText().equals("로그아웃")){
                    Log.i("kkkk22222","로그아웃 누름");
                    SharedPreferences pref = getSharedPreferences("sFile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    NaverLoginActivity.mOAuthLoginInstance.logout((NaverLoginActivity)NaverLoginActivity.mContext);

                    mBtnLoginInNavHeader.setText("로그인");
                    nav_header_user_email.setText("이메일");
                    nav_header_user_name.setText("없음");
                    u_id=null;
                }
            }
        });

        mBtnChoiceIngredient = findViewById(R.id.btnChoiceIngredient);
        mBtnRecipeCategory = findViewById(R.id.btnRecipeCategory);
        mBtnShop = findViewById(R.id.btnShop);
        mBtnWriteRecipe = findViewById(R.id.btnWriteRecipe);
        nav_header_user_name = header.findViewById(R.id.nav_header_user_name);
        nav_header_user_email = header.findViewById(R.id.nav_header_user_email);

        LinearLayout.OnClickListener onClickListener = new LinearLayout.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                boolean dialogFlag = false;

                switch(v.getId()){
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
                        if(u_id == null){
                            showLoginAlertDialog();
                            dialogFlag = true;
                        }else{
                            intent = new Intent(MainActivity.this, RecipeFormActivity.class);
                            intent.putExtra("u_id", u_id);
                            intent.putExtra("u_name",u_name);
                        }
                        break;
                }
                if(!dialogFlag) startActivity(intent);
            }
        };
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
                        Thread.sleep(4000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
//        nav_header_user_name.setText(sf.getString("u_name",""));
        if(nav_header_user_name.getText().toString().equals("")){

        }
        else {
//            u_id =sf.getString("u_id","");
//            nav_header_user_email.setText(sf.getString("u_email","")+"@naver.com");
//            mBtnLoginInNavHeader.setText("로그아웃");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        //search_menu.xml 등록
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_action, menu);

        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        menu.findItem(R.id.action_search).setIcon(drawable);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearch.getActionView();

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

            //검색버튼을 눌렀을 경우
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
        boolean dialogFlag = false;
        Intent intent = new Intent();
        // TODO : 로그인 확인 부분 꽤 겹치는데 나중에 한번에 수정함 -LJH
        if (id == R.id.nav_enroll_recipe) {
            if(u_id == null){
                showLoginAlertDialog();
                dialogFlag = true;
            }else{
                intent = new Intent(MainActivity.this, MyRecipeListActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("u_name",u_name);
            }
        }else if(id == R.id.nav_scrap_recipe){
            intent = new Intent(MainActivity.this,BoardActivity.class);
        }
        else if(id==R.id.nav_setting){
            intent = new Intent(MainActivity.this,SettingActivity.class);
        }
        else if(id==R.id.nav_changeInfo){
            if(u_id==null){
                showLoginAlertDialog();
                dialogFlag = true;
            }
            else{
                //내 회원정보 수정으로 변경
            }
        }
        else if(id==R.id.nav_myShopLog){
            //내 쇼핑정보 보기
            intent = new Intent(MainActivity.this,ShowShopLogActivity.class);
        }
        if(!dialogFlag) startActivity(intent);
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
            if(resultCode==RESULT_OK) {
                u_id = data.getStringExtra("user_id");
                u_name = data.getStringExtra("user_name");
                u_email = data.getStringExtra("user_email");
                u_phone = data.getStringExtra("user_phone");
                nav_header_user_name.setText(u_name);
                nav_header_user_email.setText(data.getStringExtra("user_email") + "@naver.com");
                mBtnLoginInNavHeader.setText("로그아웃");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
                Log.i("kkkkkk main u_id", u_id);
                Log.i("kkkkkk main u_phone",u_phone);
                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("u_id",u_id); // key, value를 이용하여 저장하는 형태
                editor.putString("u_name",u_name);
                editor.putString("u_email",u_email);
                editor.commit();
            }
        }
    }
    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size;
    }

    public void showLoginAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_error_outline_24dp);
        builder.setTitle("로그인 해주세요 :(");
        builder.setMessage("로그인을 해야 레시피 등록이 가능합니다.");
        builder.setPositiveButton("로그인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, NaverLoginActivity.class);
                        startActivityForResult(intent,REQUEST_NAVER);
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Activity가 종료되기 전에 저장한다.
        //SharedPreferences를 sFile이름, 기본모드로 설정
        SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("u_id",u_id); // key, value를 이용하여 저장하는 형태
        editor.putString("u_name",u_name);
        editor.putString("u_email",u_email);
        editor.commit();
    }
}
