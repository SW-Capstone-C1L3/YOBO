package com.example.yobo_android.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BasketIngredientAdapter;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.IngredientsBasketData;
import com.example.yobo_android.fragment.BottomSheetFragBasket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasketActivity extends AppCompatActivity{

    private SearchView mSearchview;
    private TextView mtoolbarTitle;
    private BasketIngredientAdapter adapter;
    private Button mbtnBuyAll;
    private Integer sum_all_price=0;
    private String user_id;
    Integer deletePos=0;
    Integer len;

    HashMap<String,Object> hashMap = new HashMap<>();
    Retrofit retrofit;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        mtoolbarTitle = findViewById(R.id.toolbar_title);
        mSearchview = findViewById(R.id.action_search);
        mbtnBuyAll = findViewById(R.id.btnBuyAll);
        mbtnBuyAll.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFragBasket fragment = new BottomSheetFragBasket();
                fragment.show(getSupportFragmentManager(), "TAG");
            }
        });
        recyclerViewInit();
        new BasketActivity.RequestAsync().execute();
    }
    public void jsonParser(String json) {
        try {
            //바꿔야됨
            JSONObject temp = new JSONObject(json);
            JSONArray BasketIngredientList = temp.getJSONArray("basket");
            len = BasketIngredientList.length();
            Log.i("kkkkk size: ",String.valueOf(BasketIngredientList.length()));
            for(int i=0; i<BasketIngredientList.length(); i++){
                IngredientsBasketData basketitem = new IngredientsBasketData();
                JSONObject basket = BasketIngredientList.getJSONObject(i);
                basketitem.setIngredientDescription(basket.getString("product_description"));
                basketitem.setBasket_product_id(basket.getString("product_id"));
                basketitem.setIngredientImage(basket.getString("product_image"));
                basketitem.setIngredientName(basket.getString("product_name"));
                basketitem.setIngredientPrice(basket.getInt("product_price"));
                basketitem.setBasket_qty(basket.getInt("qty"));
                sum_all_price +=basket.getInt("qty")*basket.getInt("product_price");
                basketitem.setUser_id("5dc6e8de068a0d0928838088");      /*****여기 전달받은 id로 바꿔야됨****/
                adapter.addItem(basketitem);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String giveSum(){
        return String.valueOf(sum_all_price);
    }

    public void delete(String str,String name, int num){
        //삭제하는거 처리해야됨
        final String ing_name = name;
        deletePos = num;
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        user_id = "5dc6e8de068a0d0928838088";                   /****나중에 user_id 받아오는거로 수정해야함****/
        hashMap.put("Product_id", str);
        hashMap.put("User_id",user_id);
        Call<ResponseBody> call = apiService.DeleteBasket(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(BasketActivity.this,ing_name+"이 삭제되었습니다",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(BasketActivity.this,"실패",Toast.LENGTH_LONG).show();
            }
        });
        adapter.removeItem(deletePos);
        recyclerViewInit();
        new BasketActivity.RequestAsync().execute();
    }

    public class RequestAsync extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                //GET Request
                /*************현재는 doc_id를 임의의 값으로 배정**********/
                /*************나중에 바꿔야됨***************/
                return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/basket/getBasket?User_id=5dc6e8de068a0d0928838088");
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                jsonParser(s);
            }
        }
    }

    private void recyclerViewInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerShopIngredientView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BasketIngredientAdapter();
        recyclerView.setAdapter(adapter);
    }
}
