package com.example.yobo_android.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.IngredientsFormAdapter;
import com.example.yobo_android.etc.IngredientsFormData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeFormActivity extends AppCompatActivity {

    List<IngredientsFormData> mainIngredientsDataList = new ArrayList<>();
    List<IngredientsFormData> subIngredientsDataList = new ArrayList<>();
    IngredientsFormAdapter mainIngredientsAdapter;
    IngredientsFormAdapter subIngredientsAdapter;
    ImageView cookingDescriptionImage;
    Button mBtnAddMainIngredients;
    Button mBtnAddSubIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton mBtnBack = findViewById(R.id.arrow_back2_ImageButton);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton mBtnWriteRecipe = findViewById(R.id.done_ImageButton);
        mBtnWriteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                // 완료 버튼 눌렀을 때 DB와 서버에 레시피 등록 !!
            }
        });

        cookingDescriptionImage = findViewById(R.id.cooking_description_image);
        cookingDescriptionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진 요청
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent,1000);
            }
        });

        RecyclerView mainIngredientsRecyclerView = findViewById(R.id.mainIngredientFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForMainIngredients = new LinearLayoutManager(this);
        mainIngredientsRecyclerView.setLayoutManager(layoutManagerForMainIngredients);
        for(int i=0;i<3;i++)
            mainIngredientsDataList.add(new IngredientsFormData(null,null));
        mainIngredientsAdapter = new IngredientsFormAdapter(mainIngredientsDataList);
        mainIngredientsRecyclerView.setAdapter(mainIngredientsAdapter);

        RecyclerView subIngredientsRecyclerView = findViewById(R.id.subIngredientFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForSubIngredients = new LinearLayoutManager(this);
        subIngredientsRecyclerView.setLayoutManager(layoutManagerForSubIngredients);
        for(int i=0;i<3;i++)
            subIngredientsDataList.add(new IngredientsFormData(null,null));
        subIngredientsAdapter = new IngredientsFormAdapter(subIngredientsDataList);
        subIngredientsRecyclerView.setAdapter(subIngredientsAdapter);

        mBtnAddMainIngredients = findViewById(R.id.mainIngredientFormAddButton);
        mBtnAddMainIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIngredientsDataList.add(new IngredientsFormData(null,null));
                mainIngredientsAdapter.notifyItemChanged(mainIngredientsDataList.size()-1);
            }
        });
        mBtnAddSubIngredients = findViewById(R.id.subIngredientFormAddButton);
        mBtnAddSubIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subIngredientsDataList.add(new IngredientsFormData(null, null));
                subIngredientsAdapter.notifyItemChanged(subIngredientsDataList.size()-1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 사진이 정상적으로 선택되었을 때.
        if(requestCode == 1000 && resultCode == RESULT_OK && data != null){
            //사진 경로
            Uri imageUri = data.getData();
            try {
                //사진을 bitmap으로 얻기
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                // 이미지뷰에 세팅
                cookingDescriptionImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
