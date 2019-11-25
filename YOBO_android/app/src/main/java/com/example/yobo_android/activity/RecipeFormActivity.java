package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.IngredientsFormAdapter;
import com.example.yobo_android.adapter.viewholder.RecipeSequenceFormAdapter;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.Cooking_description;
import com.example.yobo_android.etc.Cooking_ingredient;
import com.example.yobo_android.etc.IngredientsFormData;
import com.example.yobo_android.etc.RecipeData;
import com.example.yobo_android.etc.RecipeSequenceFormData;
import com.google.android.material.snackbar.Snackbar;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeFormActivity extends AppCompatActivity  {

    private static final int PICK_FROM_ALBUM = 1000;
    private static final int PICK_FROM_ALBUM2 = 2000;

    Retrofit retrofit;
    ApiService apiService;
    List<Uri> fileUris = new ArrayList<>();

    List<IngredientsFormData> mMainIngredientsDataList = new ArrayList<>();
    List<IngredientsFormData> mSubIngredientsDataList = new ArrayList<>();
    List<RecipeSequenceFormData> mRecipeSequenceFormDataList = new ArrayList<>();

    List<String> category = new ArrayList<>();
    List<Cooking_description> cooking_descriptions = new ArrayList<>();
    List<Cooking_ingredient> main_cooking_ingredients = new ArrayList<>();
    List<Cooking_ingredient> sub_cooking_ingredients = new ArrayList<>();

    IngredientsFormAdapter mainIngredientsAdapter;
    IngredientsFormAdapter subIngredientsAdapter;
    RecipeSequenceFormAdapter recipeSequenceFormAdapter;

    ImageView mImageCookingDescription;
    Button mBtnAddMainIngredients;
    Button mBtnAddSubIngredients;
    Button mBtnAddRecipeSequenceForm;
    Spinner mSpinnerCountry;
    Spinner mSpinnerCookingType;

    EditText mEtRecipeName;
    EditText mEtCookingDescription;

    String selectedCountry;
    String selectedCookingType;
    int tempPosForSequenceForm;
    boolean flagCookingDescriptionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        mEtRecipeName = findViewById(R.id.recipe_name);
        mEtCookingDescription = findViewById(R.id.cooking_description);

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
                if(checkInput()){

                    category.add(mSpinnerCountry.getSelectedItem().toString());
                    category.add(mSpinnerCookingType.getSelectedItem().toString());
                    cooking_descriptions.add(new Cooking_description(
                            mEtCookingDescription.getText().toString(), "TEMP"));
                    for (int i = 0; i < mRecipeSequenceFormDataList.size(); i++) {
                        cooking_descriptions.add(new Cooking_description(
                                        mRecipeSequenceFormDataList.get(i).getRecipeSequenceFormDescription(),
                                        "TEMP"));
                    }
                    for (int i = 0; i < mMainIngredientsDataList.size(); i++) {
                        main_cooking_ingredients.add(new Cooking_ingredient(
                                mMainIngredientsDataList.get(i).getIngredientsName(), 0, ""));

                    }
                    for (int i = 0; i < mSubIngredientsDataList.size(); i++) {
                        sub_cooking_ingredients.add(new Cooking_ingredient(
                                mSubIngredientsDataList.get(i).getIngredientsName(), 0, ""));
                    }
                    final RecipeData recipe = new RecipeData(
                            category,cooking_descriptions, 0, main_cooking_ingredients,
                            0.0, mEtRecipeName.getText().toString(),
                            0, sub_cooking_ingredients,"LJH");

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

                    List<MultipartBody.Part> parts = new ArrayList<>();
                    for(int i=0 ; i<fileUris.size(); i++){
                        parts.add(prepareFileParts("image",fileUris.get(i)));
                    }
                    Call<ResponseBody> call = apiService.uploadRecipe(parts,recipe);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(RecipeFormActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ERROR", t.toString());
                            Log.e("ERROR", call.toString());
                        }
                    });
                }
            }
        });

        mImageCookingDescription = findViewById(R.id.cooking_description_image);
        mImageCookingDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진 요청
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        for(int i=0;i<2;i++)
            mMainIngredientsDataList.add(new IngredientsFormData(null,null));
        mainIngredientsAdapter = new IngredientsFormAdapter(mMainIngredientsDataList);
        mainIngredientsRecyclerView.setAdapter(mainIngredientsAdapter);

        RecyclerView subIngredientsRecyclerView = findViewById(R.id.subIngredientFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForSubIngredients = new LinearLayoutManager(this);
        subIngredientsRecyclerView.setLayoutManager(layoutManagerForSubIngredients);
        for(int i=0;i<2;i++)
            mSubIngredientsDataList.add(new IngredientsFormData(null,null));
        subIngredientsAdapter = new IngredientsFormAdapter(mSubIngredientsDataList);
        subIngredientsRecyclerView.setAdapter(subIngredientsAdapter);

        RecyclerView recipeSequenceFormRecyclerView = findViewById(R.id.recipeSequenceFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForRecipeSequenceForm = new LinearLayoutManager(this);
        recipeSequenceFormRecyclerView.setLayoutManager(layoutManagerForRecipeSequenceForm);

        for(int i=0;i<2;i++)
            mRecipeSequenceFormDataList.add(new RecipeSequenceFormData(null,null));

        recipeSequenceFormAdapter = new RecipeSequenceFormAdapter(mRecipeSequenceFormDataList);
        recipeSequenceFormAdapter.setOnItemClickListener(new RecipeSequenceFormAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                tempPosForSequenceForm = pos;
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("sequencePosition", pos);
                startActivityForResult(intent,PICK_FROM_ALBUM2);
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
        mSpinnerCookingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        if(pictureSelected(resultCode,data)){
            //TODO : 서버에 올릴 사진 크기(용량) 줄이는 작업
            Uri imageUri = data.getData();
            fileUris.add(imageUri);

            if(requestCode == PICK_FROM_ALBUM){
                Picasso.get().load(imageUri).into(mImageCookingDescription);
                flagCookingDescriptionImage = true;

            }else if(requestCode == PICK_FROM_ALBUM2){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                    mRecipeSequenceFormDataList.get(tempPosForSequenceForm).setRecipeSequenceFormImageId(bitmap);
                    recipeSequenceFormAdapter.notifyItemChanged(tempPosForSequenceForm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean pictureSelected(int resultCode, @Nullable Intent data) {
        return resultCode == RESULT_OK && data != null;
    }

    public boolean checkInput(){
        boolean flag = true;
        String snackBarMessage = null;

        if(mEtRecipeName.length() == 0){
            snackBarMessage = "레시피 제목을 입력해주세요 :(";
        }else if(mEtCookingDescription.length() == 0){
            snackBarMessage = "레시피 간단 설명을 입력해주세요 :(";
        }else if(!flagCookingDescriptionImage){
            snackBarMessage = "레시피 대표 사진을 등록해주세요 :(";
        }else if(mainIngredientsCheck()){
            snackBarMessage = "주재료 정보를 모두 입력해주세요 :(";
        }else if(recipeSequenceCheck()){
            snackBarMessage = "레시피 순서를 모두 입력해주세요 :(";
        }else if(mSpinnerCountry.getSelectedItem().toString().equals("[나라별]")){
            snackBarMessage = "나라별 카테고리 설정을 해주세요 :(";
        }else if(mSpinnerCookingType.getSelectedItem().toString().equals("[조리별]")){
            snackBarMessage = "조리별 카테고리 설정을 해주세요 :(";
        }

        if(snackBarMessage != null) {
            showSnackBar(snackBarMessage);
            flag = false;
        }
        return flag;
    }

    private void showSnackBar(String snackBarMessage) {
        Snackbar make = Snackbar.make(getWindow().getDecorView().getRootView(),
                snackBarMessage, Snackbar.LENGTH_LONG);
        make.setAction("확인", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        make.setActionTextColor(Color.GREEN);
        make.show();
    }

    public boolean mainIngredientsCheck(){
        for(int i=0; i<mMainIngredientsDataList.size(); i++){
            if(mMainIngredientsDataList.get(i).getIngredientsName().equals("")
            || mMainIngredientsDataList.get(i).getIngredientsQuantity().equals("")){
                return true;
            }
       }
        return false;
    }

    public boolean recipeSequenceCheck(){
        for(int i=0; i<mRecipeSequenceFormDataList.size(); i++){
            if(mRecipeSequenceFormDataList.get(i).getRecipeSequenceFormDescription().equals("")
            || mRecipeSequenceFormDataList.get(i).getRecipeSequenceFormImageId() == null){
                return true;
            }
        }
        return false;
    }

    @NonNull
    private MultipartBody.Part prepareFileParts(String partName, Uri fileUri){
        File file = FileUtils.getFile(this,fileUri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

}
