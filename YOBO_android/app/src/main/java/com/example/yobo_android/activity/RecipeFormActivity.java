package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.Cooking_description;
import com.example.yobo_android.etc.Cooking_ingredient;
import com.example.yobo_android.etc.Recipe;
import com.google.android.material.snackbar.Snackbar;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFormActivity extends AppCompatActivity  {

    public static final int PICK_FROM_ALBUM = 1000;
    public static final int PICK_FROM_ALBUM2 = 2000;
    public static final int NEW_RECIPE = 3000;
    public static final int MODIFY_MY_RECIPE = 4000;

    List<Uri> fileUris = new ArrayList<>();

    List<Cooking_description> mRecipeSequenceFormDataList = new ArrayList<>();
    List<Cooking_description> cooking_descriptions = new ArrayList<>();
    List<Cooking_ingredient> main_cooking_ingredients = new ArrayList<>();
    List<Cooking_ingredient> sub_cooking_ingredients = new ArrayList<>();
    List<String> category = new ArrayList<>();
    boolean[] checkChanged = new boolean[20];

    IngredientsFormAdapter mainIngredientsAdapter;
    IngredientsFormAdapter subIngredientsAdapter;
    RecipeSequenceFormAdapter recipeSequenceFormAdapter;

    ImageView mImageCookingDescription;
    Button mBtnAddMainIngredients;
    Button mBtnAddSubIngredients;
    Button mBtnAddRecipeSequenceForm;

    Spinner mSpinnerCountry;
    Spinner mSpinnerCookingType;
    Spinner mSpinnerServing;
    Spinner mSpinnerDifficulty;

    EditText mEtRecipeName;
    EditText mEtCookingDescription;

    String selectedCountry;
    String selectedCookingType;
    String selectedServing;
    String selectedDifficulty;

    String userId;
    String userName;
    String recipeId;
    Double rating;
    Intent intent;

    int typeOfWrite;
    int tempPosForSequenceForm;
    boolean flagCookingDescriptionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        Uri dummyForCheck = null;
        fileUris.add(dummyForCheck);

        intent = getIntent();
        userId = MainActivity.u_id;
        userName = MainActivity.u_name;
        recipeId = intent.getStringExtra("recipeId");
        typeOfWrite = intent.getIntExtra("type",NEW_RECIPE);

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
                if(checkInput()) {
                    category.add(mSpinnerCountry.getSelectedItem().toString());
                    category.add(mSpinnerCookingType.getSelectedItem().toString());
                    List<MultipartBody.Part> parts = new ArrayList<>();
                    for (int i = 0; i < fileUris.size(); i++)
                        parts.add(prepareFileParts("image", fileUris.get(i)));

                    if (typeOfWrite == NEW_RECIPE) {
                        cooking_descriptions.add(new Cooking_description(
                                mEtCookingDescription.getText().toString(), "TEMP"));
                        for (int i = 0; i < mRecipeSequenceFormDataList.size(); i++) {
                            cooking_descriptions.add(new Cooking_description(
                                    mRecipeSequenceFormDataList.get(i).getDescription(),
                                    "TEMP"));
                        }

                        final Recipe recipe = new Recipe(
                                category, cooking_descriptions, main_cooking_ingredients,
                                sub_cooking_ingredients, 0.0, selectedDifficulty,
                                mEtRecipeName.getText().toString(), selectedServing, userId, userName);

                        Call<ResponseBody> call = RetrofitClient.getInstance().getApiService().uploadRecipe(parts, recipe);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(RecipeFormActivity.this, "레시피 등록 완료 :)", Toast.LENGTH_SHORT).show();
                                intent.putExtra("result", "some value");
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("ERROR", t.toString());
                                Log.e("ERROR", call.toString());
                            }
                        });
                    }
                    else if(typeOfWrite == MODIFY_MY_RECIPE){
                        if(checkChanged[0])
                            cooking_descriptions.add(0,new Cooking_description(
                                    mEtCookingDescription.getText().toString(), "change"));
                        else
                            cooking_descriptions.add(0,new Cooking_description(
                                    mEtCookingDescription.getText().toString(), "exit"));

                        for (int i = 0; i < mRecipeSequenceFormDataList.size(); i++) {
                            if(checkChanged[i+1])
                                cooking_descriptions.add(new Cooking_description(
                                        mRecipeSequenceFormDataList.get(i).getDescription(),
                                        "change"));
                            else
                                cooking_descriptions.add(new Cooking_description(
                                        mRecipeSequenceFormDataList.get(i).getDescription(),
                                        "exit"));
                        }

                        final Recipe recipe = new Recipe(
                                category, cooking_descriptions, main_cooking_ingredients,
                                sub_cooking_ingredients, rating, selectedDifficulty,
                                mEtRecipeName.getText().toString(), selectedServing, userId, userName,recipeId);

                        Call<ResponseBody> call = RetrofitClient.getInstance().getApiService().updateRecipe(parts, recipe);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(RecipeFormActivity.this, "레시피 수정 완료 :)", Toast.LENGTH_SHORT).show();
                                intent.putExtra("result", "some value");
                                setResult(RESULT_OK, intent);
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

        if(typeOfWrite == MODIFY_MY_RECIPE){
            fileUris.remove(0);
            Call<Recipe> modifyCall = RetrofitClient.getInstance().getApiService().getReicpebyDid(recipeId);
            modifyCall.enqueue(new Callback<Recipe>() {
                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    Recipe recipe = response.body();
                    rating = recipe.getRating();
                    mEtRecipeName.setText(recipe.getRecipe_name());
                    mEtCookingDescription.setText(recipe.getCooking_description().get(0).getDescription());
                    mSpinnerCountry.setSelection(getSpinnerIndex(mSpinnerCountry,recipe.getCategory().get(0)));
                    mSpinnerCookingType.setSelection(getSpinnerIndex(mSpinnerCookingType,recipe.getCategory().get(1)));
                    mSpinnerServing.setSelection(getSpinnerIndex(mSpinnerServing,recipe.getServing()));
                    mSpinnerDifficulty.setSelection(getSpinnerIndex(mSpinnerDifficulty,recipe.getDifficulty()));

                    main_cooking_ingredients.remove(0);
                    for (int i = 0; i < recipe.getMain_cooking_ingredients().size(); i++) {
                        main_cooking_ingredients.add(recipe.getMain_cooking_ingredients().get(i));
                        mainIngredientsAdapter.notifyItemChanged(i);
                    }
                    sub_cooking_ingredients.remove(0);
                    for (int i = 0; i < recipe.getSub_cooking_ingredients().size(); i++) {
                        sub_cooking_ingredients.add(recipe.getSub_cooking_ingredients().get(i));
                        subIngredientsAdapter.notifyItemChanged(i);
                    }

                    mRecipeSequenceFormDataList.remove(0);
                    recipeSequenceFormAdapter.notifyDataSetChanged();
                    for (int i = 0; i < recipe.getCooking_description().size(); i++) {
                        String temp = recipe.getCooking_description().get(i).getImage();
                        temp = temp.replace("/", "%2F");
                        String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                        Uri uri = Uri.parse(sum);
                        if(i == 0 ){
                            Picasso.get().load(uri).into(mImageCookingDescription);
                            flagCookingDescriptionImage = true;
                        }else{
                            mRecipeSequenceFormDataList.add(new Cooking_description(
                                    recipe.getCooking_description().get(i).getDescription(),uri.toString())
                            );
                            recipeSequenceFormAdapter.notifyItemChanged(i);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {

                }
            });
        }
    }

    private void setRecyclerViews() {
        RecyclerView mainIngredientsRecyclerView = findViewById(R.id.mainIngredientFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForMainIngredients = new LinearLayoutManager(this);
        mainIngredientsRecyclerView.setLayoutManager(layoutManagerForMainIngredients);
        for(int i=0;i<1;i++)
            main_cooking_ingredients.add(new Cooking_ingredient(null,null,null));
        mainIngredientsAdapter = new IngredientsFormAdapter(main_cooking_ingredients);
        mainIngredientsRecyclerView.setAdapter(mainIngredientsAdapter);

        RecyclerView subIngredientsRecyclerView = findViewById(R.id.subIngredientFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForSubIngredients = new LinearLayoutManager(this);
        subIngredientsRecyclerView.setLayoutManager(layoutManagerForSubIngredients);
        for(int i=0;i<1;i++)
            sub_cooking_ingredients.add(new Cooking_ingredient(null,null,null));
        subIngredientsAdapter = new IngredientsFormAdapter(sub_cooking_ingredients);
        subIngredientsRecyclerView.setAdapter(subIngredientsAdapter);

        RecyclerView recipeSequenceFormRecyclerView = findViewById(R.id.recipeSequenceFormRecyclerView);
        RecyclerView.LayoutManager layoutManagerForRecipeSequenceForm = new LinearLayoutManager(this);
        recipeSequenceFormRecyclerView.setLayoutManager(layoutManagerForRecipeSequenceForm);

        for(int i=0;i<1;i++)
            mRecipeSequenceFormDataList.add(new Cooking_description(null,null));
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
                main_cooking_ingredients.add(new Cooking_ingredient(null,null,null));
                mainIngredientsAdapter.notifyItemChanged(main_cooking_ingredients.size()-1);
            }
        });
        mBtnAddSubIngredients = findViewById(R.id.subIngredientFormAddButton);
        mBtnAddSubIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub_cooking_ingredients.add(new Cooking_ingredient(null, null,null));
                subIngredientsAdapter.notifyItemChanged(sub_cooking_ingredients.size()-1);
            }
        });
        mBtnAddRecipeSequenceForm = findViewById(R.id.recipeSequenceFormAddButton);
        mBtnAddRecipeSequenceForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecipeSequenceFormDataList.get(mRecipeSequenceFormDataList.size()-1).
                        getImage() == null ) {
                    showSnackBar("레시피 순서의 사진을 등록해주세요 :(");
                }else{
                    mRecipeSequenceFormDataList.add(new Cooking_description(null, null));
                    recipeSequenceFormAdapter.notifyItemChanged(mRecipeSequenceFormDataList.size()-1);
                }
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
        mSpinnerServing = findViewById(R.id.spinnerServing);
        mSpinnerServing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedServing = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        mSpinnerDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDifficulty = parent.getItemAtPosition(position).toString();
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
            Uri imageUri = data.getData();
            ExifInterface oldExif = null;
            String exifOrientation;
            int degree = 0;
            try {
                oldExif = new ExifInterface(getRealPathFromUri(getApplicationContext(),imageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            exifOrientation = oldExif.getAttribute(ExifInterface.TAG_ORIENTATION);
            if (exifOrientation != null) degree = getOrientation(Integer.parseInt(exifOrientation));

            try {
                Bitmap resize = getBitmapFromUri(data.getData());
                resize = RotateBitmap(resize, degree);
                imageUri = getImageUri(getApplicationContext(), resize);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(requestCode == PICK_FROM_ALBUM){
                if(typeOfWrite == MODIFY_MY_RECIPE){
                    checkChanged[0] = true;
                    fileUris.add(0,imageUri);
                }else{
                    fileUris.set(0,imageUri);
                }
                Picasso.get().load(imageUri).into(mImageCookingDescription);
                flagCookingDescriptionImage = true;

            }else if(requestCode == PICK_FROM_ALBUM2){
                if(typeOfWrite == MODIFY_MY_RECIPE)
                    checkChanged[tempPosForSequenceForm+1] = true;
                fileUris.add(imageUri);
                mRecipeSequenceFormDataList.get(tempPosForSequenceForm).setImage(imageUri.toString());
                recipeSequenceFormAdapter.notifyItemChanged(tempPosForSequenceForm);

            }
        }
    }

    private boolean pictureSelected(int resultCode, @Nullable Intent data) {
        return resultCode == RESULT_OK && data != null;
    }

    public boolean checkInput(){
        //TODO : 키보드 열려있으면 내리는 이벤트 나중에 추가하면 좋을듯!

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
        }else if(subIngredientsCheck()){
            snackBarMessage = "부재료 정보를 모두 입력해주세요 :(";
        }else if(ingredientsQtyCheck()){
            snackBarMessage = "재료 양은 숫자로 입력해주세요 :(";
        }else if(recipeSequenceCheck()){
            snackBarMessage = "레시피 순서를 모두 입력해주세요 :(";
        }else if(mSpinnerCountry.getSelectedItem().toString().equals("[나라별]")){
            snackBarMessage = "나라별 카테고리 설정을 해주세요 :(";
        }else if(mSpinnerCookingType.getSelectedItem().toString().equals("[조리별]")){
            snackBarMessage = "조리별 카테고리 설정을 해주세요 :(";
        }else if(mSpinnerServing.getSelectedItem().toString().equals("[요리 양]")){
            snackBarMessage = "요리 양 카테고리 설정을 해주세요 :(";
        }else if(mSpinnerDifficulty.getSelectedItem().toString().equals("[난이도]")){
            snackBarMessage = "난이도 카테고리 설정을 해주세요 :(";
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
        for(int i=0; i<main_cooking_ingredients.size(); i++){
            if(main_cooking_ingredients.get(i).getIngredients_name().equals("")
            || main_cooking_ingredients.get(i).getUnit().equals("")){
                return true;
            }
       }
        return false;
    }

    public boolean subIngredientsCheck(){
        for(int i=0; i<sub_cooking_ingredients.size(); i++){
            if(sub_cooking_ingredients.get(i).getIngredients_name().equals("")
                    || sub_cooking_ingredients.get(i).getUnit().equals("")){
                return true;
            }
        }
        return false;
    }

    public boolean ingredientsQtyCheck(){
        for (int i = 0; i < main_cooking_ingredients.size(); i++) {
            if(main_cooking_ingredients.get(i).getQty()==null) return true;
        }
        for (int i = 0; i < sub_cooking_ingredients.size(); i++) {
            if(sub_cooking_ingredients.get(i).getQty()==null) return true;
        }
        return false;
    }

    public boolean recipeSequenceCheck(){
        for(int i=0; i<mRecipeSequenceFormDataList.size(); i++){
            if(mRecipeSequenceFormDataList.get(i).getDescription().equals("")
            || mRecipeSequenceFormDataList.get(i).getImage() == null){
                return true;
            }
        }
        return false;
    }

    private int getSpinnerIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
                return index;
            }
        }
        return index;
    }

    @NonNull
    private MultipartBody.Part prepareFileParts(String partName, Uri fileUri){
        File file = FileUtils.getFile(this,fileUri);
        RequestBody requestFile =
                RequestBody.create(file,MediaType.parse(getContentResolver().getType(fileUri)));
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor = getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        //세부 정보 말고 크기 정보만 갖고 온다
        opts.inJustDecodeBounds = true;

        // ex) 4096 * 3800
        int width = opts.outWidth;
        int height = opts.outHeight;

        float sampleRatio = getSampleRatio(width, height);

        opts.inJustDecodeBounds=false;
        opts.inSampleSize=(int)sampleRatio;

        Bitmap resizedBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opts);
        Log.d("Resizing", "Resized Width / Height" + resizedBitmap.getWidth() + "/" + resizedBitmap.getHeight());
        parcelFileDescriptor.close();
        return resizedBitmap;
    }

    private float getSampleRatio(int width, int height) {
        //상한
        final int targetWidth = 1280;
        final int targetHeight = 1280;

        float ratio;

        if(width > height){
            //Landscape
            if(width > targetWidth)
                ratio = (float)width / (float)targetWidth;
            else
                ratio = 1f;
        }
        else{
            //Portrait
            if(height > targetHeight)
                ratio=(float)height/(float)targetHeight;
            else
                ratio = 1f;
        }

        return Math.round(ratio);
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                inImage, "ResizeImage", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public int getOrientation(int orientation){
        int degree;
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                degree = 0;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            case ExifInterface.ORIENTATION_UNDEFINED:
                degree = 0;
                break;
            default:
                degree = 90;
        }
        return degree;
    }

}
