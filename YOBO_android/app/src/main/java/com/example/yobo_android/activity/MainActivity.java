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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yobo_android.R;
import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.UserData;
import com.example.yobo_android.fragment.RecipeRecomFragment;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int REQUEST_TEST =1000;
    private int REQUEST_NAVER=2000;
    private int REQUEST_MYINFO=3000;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private LinearLayout mBtnChoiceIngredient;
    private LinearLayout mBtnRecipeCategory;
    private LinearLayout mBtnShop;
    private LinearLayout mBtnWriteRecipe;
    private TextView mtoolbarTitle;

    // for recipe recommendation
    private int NUM_PAGES = 3;
    private ViewPager   mPager;
    private PagerAdapter pagerAdapter;
    private ImageButton mBtnOpen;
    private ArrayList<String> favorite_list = new ArrayList<>();
    private List<Recipe> recipe = new ArrayList<>();
    private List<String> recipe_id = new ArrayList<>();
    private List<String> fav_imageList = new ArrayList<>();
    private List<String> recipe_name = new ArrayList<>();
    private List<String> description = new ArrayList<>();

    // user Info in nav header
    private TextView nav_header_user_name;
    private TextView nav_header_user_email;
    private Integer num=0;
    private Button mBtnLoginInNavHeader;

    public static String u_id;
    public static String u_phone;
    public static String u_name;
    public static String u_email;
    private ImageView mUserPicture;
    UserData userData;
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
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        permissionCheck();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mtoolbarTitle = findViewById(R.id.toolbar_title);
        mPager = findViewById(R.id.pagerMain);

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
                    NaverLoginActivity.mOAuthLoginInstance.logout((NaverLoginActivity)NaverLoginActivity.mContext);
                    mBtnLoginInNavHeader.setText("로그인");
                    mBtnLoginInNavHeader.setGravity(Gravity.CENTER_HORIZONTAL);
                    nav_header_user_email.setText("");
                    nav_header_user_name.setText("");
                    mUserPicture.setImageDrawable(getResources().getDrawable(R.drawable.user));
                    u_id = null;
                }
            }
        });

        mBtnChoiceIngredient = findViewById(R.id.btnChoiceIngredient);
        mBtnRecipeCategory = findViewById(R.id.btnRecipeCategory);
        mBtnShop = findViewById(R.id.btnShop);
        mBtnWriteRecipe = findViewById(R.id.btnWriteRecipe);
        nav_header_user_name = header.findViewById(R.id.nav_header_user_name);
        nav_header_user_email = header.findViewById(R.id.nav_header_user_email);
        mUserPicture = header.findViewById(R.id.userPicture);
        mUserPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(u_id!=null) {
                    Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                    startActivityForResult(intent,REQUEST_MYINFO);
                }
                else
                    showLoginAlertDialog(4);
            }
        });
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
                        if(u_id  == null){
                            showLoginAlertDialog(0);
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

        favorite_list.add("");
        getRecommendedRecipes();

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
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    // 사용자 정보 setting
    public void setMyInfo(){
        Call<UserData> call = RetrofitClient.getInstance().getApiService().getbyDid(u_id);
        if (call != null) {
            call.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    userData = response.body();
                    u_name = userData.getUser_name();
                    u_email = userData.getUser_email() + "@naver.com";
                    u_phone = userData.getUser_phone_num();
                    if(userData.getImage() != null){
                        String temp = userData.getImage();
                        temp = temp.replace("/", "%2F");
                        String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                        Uri uri = Uri.parse(sum);
                        Picasso.get().load(uri).fit().centerInside().error(R.drawable.user).into(mUserPicture);
                    }
                    favorite_list = userData.getInterest_category();
                    nav_header_user_name.setText(userData.getUser_name());
                    nav_header_user_email.setText(userData.getUser_id() +"@naver.com");
                    getRecommendedRecipes();
                }
                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                }
            });
        }
    }

    //사용자의 취향에 따라 레시피를 가져오는 역할(대문에 게시용)
    public void getRecommendedRecipes(){
        Call<List<Recipe>> call = RetrofitClient.getInstance().getApiService().getRecommendedRecipe(favorite_list);
        if (call != null) {
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    recipe = response.body();
                    recipe_name = new ArrayList<>();
                    description = new ArrayList<>();
                    recipe_id = new ArrayList<>();
                    fav_imageList = new ArrayList<>();

                  for(int i=0;i<recipe.size();i++){
                        recipe_id.add(recipe.get(i).get_id());
                        recipe_name.add(recipe.get(i).getRecipe_name());
                        description.add(recipe.get(i).getCooking_description().get(0).getDescription());
                        String temp =  recipe.get(i).getCooking_description().get(0).getImage();
                        temp = temp.replace("/", "%2F");
                        String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                        fav_imageList.add(sum);
                    }
                    pagerAdapter = new MainActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
                    mPager.setAdapter(pagerAdapter);
                    pagerAdapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_action, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("레시피 검색");
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplication(),BoardActivity.class);
                intent.putExtra("query",query);
                startActivityForResult(intent,REQUEST_TEST);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        boolean dialogFlag = false;
        Intent intent = new Intent();

        if (id == R.id.nav_enroll_recipe) {
            if(u_id == null){
                showLoginAlertDialog(0);
                dialogFlag = true;
            }else{
                intent = new Intent(MainActivity.this, MyRecipeListActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("u_name",u_name);
            }
        }else if(id == R.id.nav_scrap_recipe){
            if(u_id == null){
                showLoginAlertDialog(5);
                dialogFlag = true;
            }else{
                intent = new Intent(MainActivity.this,BoardActivity.class);
                intent.putExtra("shotcut", true);
            }
        }
        else if(id==R.id.nav_commented_recipe){
            if(u_id == null){
                showLoginAlertDialog(3);
                dialogFlag = true;
            }else{
                intent = new Intent(MainActivity.this,CommentActivity.class);
                intent.putExtra("comments",u_id);
            }
        }
        else if(id==R.id.nav_modifyMyInfo){
            if(u_id == null){
                showLoginAlertDialog(1);
                dialogFlag = true;
            }
            else{
                intent = new Intent(MainActivity.this, MyPageActivity.class);
                startActivityForResult(intent,REQUEST_MYINFO);
            }
        }
        else if(id==R.id.nav_myShopLog){
            if(u_id == null) {
                showLoginAlertDialog(2);
                dialogFlag = true;
            }
            else
                intent = new Intent(MainActivity.this,ShowShopLogActivity.class);
        }
        if(!dialogFlag && id != R.id.nav_modifyMyInfo) startActivity(intent);
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
            return RecipeRecomFragment.newInstance(recipe_id.get(position), fav_imageList.get(position),recipe_name.get(position),description.get(position),position);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TEST) {
        }
        else if(requestCode == REQUEST_NAVER){
            if(resultCode==RESULT_OK) {
                u_id = data.getStringExtra("user_id");
                setMyInfo();
                mBtnLoginInNavHeader.setText("로그아웃");
                mBtnLoginInNavHeader.setGravity(Gravity.RIGHT);
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }
        else if(requestCode==REQUEST_MYINFO){
            if(resultCode==RESULT_OK){
                setMyInfo();
            }
        }
    }

    public void showLoginAlertDialog(int flag){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_error_outline_24dp);
        builder.setTitle("로그인 해주세요 :(");
        if(flag==0)
            builder.setMessage("로그인을 해야 레시피 등록이 가능합니다.");
        else if(flag==1)
            builder.setMessage("로그인을 해야 개인정보 수정이 가능합니다.");
        else if(flag==2)
            builder.setMessage("로그인을 해야 내 쇼핑정보를 볼 수 있습니다.");
        else if(flag==3)
            builder.setMessage("로그인을 해야 댓글 단 레시피를 볼 수 있습니다");
        else if(flag==4)
            builder.setMessage("로그인을 해야 회원정보 수정화면으로 갈 수 있습니다");
        else if(flag==5)
            builder.setMessage("로그인을 해야 즐겨찾기 등록한 레시피를 볼 수 있습니다");
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
}
