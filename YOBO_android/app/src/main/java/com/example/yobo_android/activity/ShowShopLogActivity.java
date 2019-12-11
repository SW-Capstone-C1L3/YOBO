package com.example.yobo_android.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.ShopLogAdapter;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.ShopLogData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowShopLogActivity extends AppCompatActivity {

    List<ShopLogData> LogList = new ArrayList<>();
    private LinearLayout mLayoutEmptyNotify;
    private RecyclerView recyclerView;
    private ShopLogAdapter adapter;
    private ImageButton mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop_log);

        mBtnBack = findViewById(R.id.arrow_back_ImageButton);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerViewInit();
        Call<List<ShopLogData>> call = null;
        call = RetrofitClient.getInstance().getApiService().getLogData(MainActivity.u_id, 0, 10);

        if (call != null) {
            call.enqueue(new Callback<List<ShopLogData>>() {
                @Override
                public void onResponse(Call<List<ShopLogData>> call, Response<List<ShopLogData>> response) {
                    Log.i("TEST", call.toString());
                    Log.i("TEST", response.toString());
                    LogList = response.body();
                    if(LogList.isEmpty()){
                        recyclerView.setVisibility(View.GONE);
                        mLayoutEmptyNotify.setVisibility(View.VISIBLE);
                    }
                    else {
                        Collections.reverse(LogList);
                        for (int i = 0; i < LogList.size(); i++)
                            adapter.addItem(LogList.get(i), i);
                        }
                    }
                @Override
                public void onFailure(Call<List<ShopLogData>> call, Throwable t) {
                }
            });
        }
    }
    private void recyclerViewInit() {
        mLayoutEmptyNotify = findViewById(R.id.emptyNotifyLayout);
        mLayoutEmptyNotify.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerLogView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ShopLogAdapter();
        recyclerView.setAdapter(adapter);
    }
}
