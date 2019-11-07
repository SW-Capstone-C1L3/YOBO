package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yobo_android.R;

/*
* 카테고리로 요리를 검색하는 Activity
* 카테고리 선택시 BoardActivity로 이동
 */

public class CategorySearchActivity extends AppCompatActivity {

    private Button mBtnKorean;
    private Button mBtnWestern;
    private Button mBtnChinese;
    private Button mBtnJapanese;
    // private Button mBtn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);

        mBtnKorean = findViewById(R.id.btnKorean);
        mBtnWestern = findViewById(R.id.btnWestern);
        mBtnChinese = findViewById(R.id.btnChinese);
        mBtnJapanese = findViewById(R.id.btnJapanese);

        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                switch (v.getId()){
                    case R.id.btnKorean:
                        // category keyword와 같이
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        break;
                    case R.id.btnWestern:
                        // category keyword와 같이
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        break;
                    case R.id.btnChinese:
                        // category keyword와 같이
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        break;
                    case R.id.btnJapanese:
                        // category keyword와 같이
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };
        mBtnKorean.setOnClickListener(onClickListener);
        mBtnWestern.setOnClickListener(onClickListener);
        mBtnChinese.setOnClickListener(onClickListener);
        mBtnJapanese.setOnClickListener(onClickListener);
    }
}
