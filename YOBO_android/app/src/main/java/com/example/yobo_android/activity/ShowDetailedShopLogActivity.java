package com.example.yobo_android.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.SelectedShopLogAdapter;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.ProductData;
import com.example.yobo_android.etc.SelectedBasketLogData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailedShopLogActivity extends AppCompatActivity {
    private String doc_id;
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
        Call<SelectedBasketLogData> call = null;
        call = RetrofitClient.getInstance().getApiService().getSelectedLogData(doc_id, 0, 10);

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
