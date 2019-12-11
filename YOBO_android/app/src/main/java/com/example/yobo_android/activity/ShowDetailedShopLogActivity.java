package com.example.yobo_android.activity;

import android.os.Bundle;
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

        Call<SelectedBasketLogData> call = null;
        call = RetrofitClient.getInstance().getApiService().getSelectedLogData(doc_id, 0, 10);
        if (call != null) {
            call.enqueue(new Callback<SelectedBasketLogData>() {
                @Override
                public void onResponse(Call<SelectedBasketLogData> call, Response<SelectedBasketLogData> response) {
                    LogData = response.body();
                    for (int i = 0; i < LogData.getProducts().size(); i++) {
                        productList.add(LogData.getProducts().get(i));
                        adapter.addItem(LogData.getProducts().get(i),i,LogData.getTransaction_status());
                    }
                }
                @Override
                public void onFailure(Call<SelectedBasketLogData> call, Throwable t) {
                }
            });
        }
    }
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.recyclerLogView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SelectedShopLogAdapter();
        recyclerView.setAdapter(adapter);
    }
}
