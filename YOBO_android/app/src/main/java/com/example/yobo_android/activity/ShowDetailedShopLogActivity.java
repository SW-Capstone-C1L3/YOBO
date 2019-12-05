package com.example.yobo_android.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yobo_android.R;

public class ShowDetailedShopLogActivity extends AppCompatActivity {
    private String doc_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop_log);
        if(getIntent().getStringExtra("log_id")!=null)
            doc_id = getIntent().getStringExtra("log_id");

    }


    //보여지는 것 중에서 하나를 클릭하면
    //ingred_id를 넣어서 ShowSelectedIngredient로 보내면됨// 단, 구매하기 버튼은 안보이도록
}
