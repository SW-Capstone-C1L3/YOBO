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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.IngredientsFormAdapter;
import com.example.yobo_android.adapter.viewholder.RecipeSequenceFormAdapter;
import com.example.yobo_android.etc.IngredientsFormData;
import com.example.yobo_android.etc.RecipeSequenceFormData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeFormActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1000;

    List<IngredientsFormData> mMainIngredientsDataList = new ArrayList<>();
    List<IngredientsFormData> mSubIngredientsDataList = new ArrayList<>();
    List<RecipeSequenceFormData> mRecipeSequenceFormDataList = new ArrayList<>();

    IngredientsFormAdapter mainIngredientsAdapter;
    IngredientsFormAdapter subIngredientsAdapter;
    RecipeSequenceFormAdapter recipeSequenceFormAdapter;

    ImageView mImageCookingDescription;
    Button mBtnAddMainIngredients;
    Button mBtnAddSubIngredients;
    Button mBtnAddRecipeSequenceForm;
    Spinner mSpinnerCountry;
    Spinner mSpinnerCookingType;

    String selectedCountry;
    String selectedCookingType;
    int tempPosForSequenceForm;

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

        mImageCookingDescription = findViewById(R.id.cooking_description_image);
        mImageCookingDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진 요청
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        });

        setRecyclerViews();
        setAddBtnForRecyclerViews();
        setSpinners();
    }

    private void setRecyclerViews() {
        RecyclerView mainIngredientsRecyclerView = findViewById(R.id.mainIngredientFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForMainIngredients = new LinearLayoutManager(this);
        mainIngredientsRecyclerView.setLayoutManager(layoutManagerForMainIngredients);
        for(int i=0;i<3;i++)
            mMainIngredientsDataList.add(new IngredientsFormData(null,null));
        mainIngredientsAdapter = new IngredientsFormAdapter(mMainIngredientsDataList);
        mainIngredientsRecyclerView.setAdapter(mainIngredientsAdapter);

        RecyclerView subIngredientsRecyclerView = findViewById(R.id.subIngredientFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForSubIngredients = new LinearLayoutManager(this);
        subIngredientsRecyclerView.setLayoutManager(layoutManagerForSubIngredients);
        for(int i=0;i<3;i++)
            mSubIngredientsDataList.add(new IngredientsFormData(null,null));
        subIngredientsAdapter = new IngredientsFormAdapter(mSubIngredientsDataList);
        subIngredientsRecyclerView.setAdapter(subIngredientsAdapter);

        RecyclerView recipeSequenceFormRecyclerView = findViewById(R.id.recipeSequenceFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForRecipeSequenceForm = new LinearLayoutManager(this);
        recipeSequenceFormRecyclerView.setLayoutManager(layoutManagerForRecipeSequenceForm);
        for(int i=0;i<3;i++)
            mRecipeSequenceFormDataList.add(new RecipeSequenceFormData(null,null));

        // 이 부분 getApplicationContext() 로 했을 때 오류 났음 - JH
        recipeSequenceFormAdapter = new RecipeSequenceFormAdapter(mRecipeSequenceFormDataList);
        recipeSequenceFormAdapter.setOnItemClickListener(new RecipeSequenceFormAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                tempPosForSequenceForm = pos;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra("sequencePosition", pos);
                intent.setType("image/*");
                startActivityForResult(intent,2000);
            }
        });
        recipeSequenceFormRecyclerView.setAdapter(recipeSequenceFormAdapter);

    }

    private void setAddBtnForRecyclerViews() {
        mBtnAddMainIngredients = findViewById(R.id.mainIngredientFormAddButton);
        mBtnAddMainIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainIngredientsDataList.add(new IngredientsFormData(null,null));
                mainIngredientsAdapter.notifyItemChanged(mMainIngredientsDataList.size()-1);
            }
        });
        mBtnAddSubIngredients = findViewById(R.id.subIngredientFormAddButton);
        mBtnAddSubIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubIngredientsDataList.add(new IngredientsFormData(null, null));
                subIngredientsAdapter.notifyItemChanged(mSubIngredientsDataList.size()-1);
            }
        });
        mBtnAddRecipeSequenceForm = findViewById(R.id.recipeSequenceFormAddButton);
        mBtnAddRecipeSequenceForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecipeSequenceFormDataList.add(new RecipeSequenceFormData(null, null));
                recipeSequenceFormAdapter.notifyItemChanged(mRecipeSequenceFormDataList.size()-1);
            }
        });
    }

    private void setSpinners() {
        mSpinnerCountry = findViewById(R.id.spinnerCountry);
        mSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerCookingType = findViewById(R.id.spinnerCookingType);
        mSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCookingType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(pictureSelected(requestCode, resultCode, data)){
            //사진 경로
            Uri imageUri = data.getData();
            Glide.with(this).load(imageUri.toString()).into(mImageCookingDescription);

        }else if(requestCode == 2000 && resultCode == RESULT_OK && data != null){

            Uri imageUri = data.getData();
            try {
                //사진을 bitmap으로 얻기
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                mRecipeSequenceFormDataList.get(tempPosForSequenceForm).setRecipeSequenceFormImageId(bitmap);
                recipeSequenceFormAdapter.notifyItemChanged(tempPosForSequenceForm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean pictureSelected(int requestCode, int resultCode, @Nullable Intent data) {
        return requestCode == 1000 && resultCode == RESULT_OK && data != null;
    }
}