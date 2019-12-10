package com.example.yobo_android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.yobo_android.R;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.ShoppingIngredientData;
import com.example.yobo_android.fragment.BottomSheetFragment;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowSelectedIngredientInfoActivity extends AppCompatActivity{

    HashMap<String,Object> hashMap = new HashMap<>();
    ShoppingIngredientData product;

    private String userId;
    private SearchView mSearchview;
    private ImageView mIngredientImage;
    private TextView mtoolbarTitle;
    private TextView mIngredientName;
    private TextView mIngredientPrice;
    private TextView mCompanyName;
    private TextView mIngredientDescription;
    private Button mBuy;
    private String Ingredient_id;
    private String Company_id;
    private int IngredientPrice;
    private String flag="";
    private final String Tag = "abcde";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ingredient);
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        Ingredient_id = intent.getStringExtra("Ingredient_id");
        if(intent.getStringExtra("fromLog")!=null)
            flag = intent.getStringExtra("fromLog");
        mIngredientImage = findViewById(R.id.ingredient_Image);
        mIngredientName = findViewById(R.id.textView_ingredient_name);
        mIngredientPrice = findViewById(R.id.textView_ingredient_price);
        mIngredientDescription = findViewById(R.id.textView_ingredient_description);
        mCompanyName = findViewById(R.id.textView_company_name);

        mtoolbarTitle = findViewById(R.id.toolbar_title);
        mSearchview = findViewById(R.id.action_search);

        Call<ShoppingIngredientData> call = RetrofitClient.getInstance().getApiService().getProduct(Ingredient_id);
        if(call != null) {
            call.enqueue(new Callback<ShoppingIngredientData>() {
                @Override
                public void onResponse(Call<ShoppingIngredientData> call, Response<ShoppingIngredientData> response) {
                    product = response.body();
                    String temp = product.getProduct_image();
                    temp = temp.replace("/", "%2F");
                    String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                    try {
                        URL url = new URL(sum);
                        Picasso.get().load(url.toString()).into((ImageView)findViewById(R.id.ingredient_Image));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    mIngredientName.setText(product.getProduct_name());
                    mIngredientPrice.setText(product.getProduct_price() + "원/" + product.getProduct_qty() + "" + product.getProduct_unit());
                    mCompanyName.setText(product.getCompany_name());
                    mIngredientDescription.setText(product.getProduct_description());
                    Company_id = product.getProvided_company_id();
                    IngredientPrice = product.getProduct_price();
                    }

                @Override
                public void onFailure(Call<ShoppingIngredientData> call, Throwable t) {
                    Toast.makeText(ShowSelectedIngredientInfoActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }

        mBuy = findViewById(R.id.btnBuy);
        mBuy.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.u_id == null){
                    showLoginAlertDialog();
                }
                else {
                    BottomSheetFragment fragment = new BottomSheetFragment();
                    fragment.show(getSupportFragmentManager(), "TAG");
                }
            }
        });

        if(flag.equals("true")){
            mBuy.setVisibility(View.GONE);
        }
    }

    public int getIngredientPrice() {
        return IngredientPrice;
    }

    public void goToBasket(int amount){       //장바구니에 담기
        userId = MainActivity.u_id;
        Log.i("kkkkkkkkk gotobasket",Ingredient_id);
        hashMap.put("Product_id", Ingredient_id);
        hashMap.put("qty",amount);
        hashMap.put("User_id",userId);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApiService().insertBasket(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(ShowSelectedIngredientInfoActivity.this,"장바구니에 담았습니다",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ShowSelectedIngredientInfoActivity.this,"장바구니에 담기 실패",Toast.LENGTH_LONG).show();
            }
        });
        finish();
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
