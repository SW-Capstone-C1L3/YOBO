package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.yobo_android.R;

/*
* 재료를 선택해서 레시피를 검색하는 Activity
* 재료선택완료시 BoardActivity로 이동
*/

public class ChoiceIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_ingredient);
    }
}
