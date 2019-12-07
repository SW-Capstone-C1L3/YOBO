package com.example.yobo_android.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.SelectedShopLogAdapter;
import com.example.yobo_android.adapter.viewholder.ShopLogAdapter;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.ProductData;
import com.example.yobo_android.etc.SelectedBasketLogData;
import com.example.yobo_android.etc.ShopLogData;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowDetailedShopLogActivity extends AppCompatActivity {
    private String doc_id;
    Retrofit retrofit;
    ApiService apiService;
    SelectedBasketLogData LogData;
    List<ProductData> productList = new ArrayList<>();
    private SelectedShopLogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop_log);
        if(getIntent().getStringExtra("log_id")!=null)
            doc_id = getIntent().getStringExtra("log_id");
        recyclerViewInit();

        /*********수정 필요********/
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build())
                .build();
        apiService = retrofit.create(ApiService.class);
        Call<SelectedBasketLogData> call = null;
        call = apiService.getSelectedLogData(doc_id, 0, 10);

        if (call != null) {
            call.enqueue(new Callback<SelectedBasketLogData>() {
                @Override
                public void onResponse(Call<SelectedBasketLogData> call, Response<SelectedBasketLogData> response) {
                    //Toast.makeText(BoardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Log.i("TEST", call.toString());
                    Log.i("TEST", response.toString());
                    LogData = response.body();
                    for (int i = 0; i < LogData.getProducts().size(); i++) {
                        productList.add(LogData.getProducts().get(i));
                        adapter.addItem(LogData.getProducts().get(i),i,LogData.getTransaction_status());
//                        Log.i("TEST1112",LogList.get(i).get_id());
                    }
                }
                @Override
                public void onFailure(Call<SelectedBasketLogData> call, Throwable t) {
                    //Toast.makeText(BoardActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", call.toString());
                    Log.e("ERROR", t.toString());
                }
            });
        }
        /***********수정필요********/
    }


    //보여지는 것 중에서 하나를 클릭하면
    //ingred_id를 넣어서 ShowSelectedIngredient로 보내면됨// 단, 구매하기 버튼은 안보이도록
    /***********수정필요********/
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.recyclerLogView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SelectedShopLogAdapter();
        recyclerView.setAdapter(adapter);
    }
    /***********수정필요********/
}
