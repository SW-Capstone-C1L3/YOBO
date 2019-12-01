package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yobo_android.R;

/*
* 카테고리로 요리를 검색하는 Activity
* 카테고리 선택시 BoardActivity로 이동
 */

public class CategorySearchActivity extends AppCompatActivity {

//    private TextView mBtnKorean;
//    private TextView mBtnWestern;
//    private TextView mBtnChinese;
//    private TextView mBtnJapanese;

    private LinearLayout mBtnKorean;
    private LinearLayout mBtnWestern;
    private LinearLayout mBtnChinese;
    private LinearLayout mBtnJapanese;
    private LinearLayout mBtnRice;
    private LinearLayout mBtnFry;
    private LinearLayout mBtnSoup;
    private LinearLayout mBtnNoodle;
    private LinearLayout mBtnMeat;
    private LinearLayout mBtnDesert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);

        mBtnKorean = findViewById(R.id.btnKorean);
        mBtnWestern = findViewById(R.id.btnWestern);
        mBtnChinese = findViewById(R.id.btnChinese);
        mBtnJapanese = findViewById(R.id.btnJapanese);
        mBtnRice = findViewById(R.id.rice);
        mBtnFry = findViewById(R.id.fry);
        mBtnSoup = findViewById(R.id.soup);
        mBtnNoodle = findViewById(R.id.noodle);
        mBtnMeat = findViewById(R.id.meat);
        mBtnDesert = findViewById(R.id.desert);

        LinearLayout.OnClickListener onClickListener = new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                String category;
                switch (v.getId()){
                    case R.id.btnKorean:
                        // category keyword와 같이
                        category = "한식";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                    case R.id.btnWestern:
                        // category keyword와 같이
                        category="양식";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                    case R.id.btnChinese:
                        // category keyword와 같이
                        category="중식";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                    case R.id.btnJapanese:
                        // category keyword와 같이
                        category="일식";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                    case R.id.rice:
                        category="밥";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                    case R.id.fry:
                        category="튀김";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                    case R.id.soup:
                        category="국물";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                    case R.id.noodle:
                        category="면";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                    case R.id.meat:
                        category="고기";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                    case R.id.desert:
                        category="디저트";
                        intent = new Intent(CategorySearchActivity.this, BoardActivity.class);
                        intent.putExtra("category",category);
                        break;
                }
                startActivity(intent);
            }
        };
        mBtnKorean.setOnClickListener(onClickListener);
        mBtnWestern.setOnClickListener(onClickListener);
        mBtnChinese.setOnClickListener(onClickListener);
        mBtnJapanese.setOnClickListener(onClickListener);
        mBtnRice.setOnClickListener(onClickListener);
        mBtnFry.setOnClickListener(onClickListener);
        mBtnSoup.setOnClickListener(onClickListener);
        mBtnNoodle.setOnClickListener(onClickListener);
        mBtnMeat.setOnClickListener(onClickListener);
        mBtnDesert.setOnClickListener(onClickListener);
    }
}
