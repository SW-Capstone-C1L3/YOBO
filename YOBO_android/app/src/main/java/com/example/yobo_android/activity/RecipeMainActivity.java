package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.CommentAdapter;
import com.example.yobo_android.adapter.viewholder.IngredientsAdapter;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.Comment;
import com.example.yobo_android.etc.CommentData;
import com.example.yobo_android.etc.Cooking_ingredient;
import com.example.yobo_android.etc.Recipe;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeMainActivity extends AppCompatActivity {

    public static final int REQUEST_MODIFY1 = 100;
    public static final int REQUEST_MODIFY2 = 200;

    String recipeWriterId;
    String recipeId;
    int descriptionNum;
    private double rate;
    private RatingBar ratingBar;
    ImageButton back;
    Button mBtnModify;
    Recipe recipe;
    List<Cooking_ingredient> main_cooking_ingredients;
    List<Cooking_ingredient> sub_cooking_ingredients;
    List<CommentData> commentDataList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private IngredientsAdapter mMainIngredientAdapter;
    private IngredientsAdapter mSubIngredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);

        recipeId = getIntent().getStringExtra("recipeId");
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        CollapsingToolbarLayout mCollapseToolBar = findViewById(R.id.collapsingtoolbar);
        recyclerViewInit();
        mBtnModify = findViewById(R.id.recipeModifyButton);

        Call<Recipe> call = RetrofitClient.getInstance().getApiService().getReicpebyDid(recipeId);
        Call<List<CommentData>> call2 = RetrofitClient.getInstance().getApiService().getCommentsbyRId(recipeId,0,10);

        if(call != null) {
            call.enqueue(new Callback<Recipe>() {
                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    recipe = response.body();
                    main_cooking_ingredients = recipe.getMain_cooking_ingredients();
                    sub_cooking_ingredients = recipe.getSub_cooking_ingredients();
                    descriptionNum = recipe.getCooking_description().size();
                    recipeWriterId = recipe.getWriter_id();
                    if(!recipeWriterId.equals(MainActivity.u_id)) mBtnModify.setVisibility(View.GONE);

                    for (int i = 0; i < main_cooking_ingredients.size(); i++)
                        mMainIngredientAdapter.addItem(main_cooking_ingredients.get(i));
                    for (int i = 0; i < sub_cooking_ingredients.size(); i++)
                        mSubIngredientAdapter.addItem(sub_cooking_ingredients.get(i));

                    mMainIngredientAdapter.notifyDataSetChanged();
                    mSubIngredientAdapter.notifyDataSetChanged();

                    String temp = recipe.getCooking_description().get(0).getImage();
                    temp = temp.replace("/", "%2F");
                    String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                    try {
                        URL url = new URL(sum);
                        Picasso.get().load(url.toString()).into((ImageView)findViewById(R.id.recipeImage2));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    ((TextView)findViewById(R.id.recipeName)).setText(recipe.getRecipe_name());
                    ((TextView)findViewById(R.id.difficulty2)).setText(recipe.getDifficulty());
                    ((TextView)findViewById(R.id.serving2)).setText(recipe.getServing());
                    rate = recipe.getRating();
                    ratingBar = findViewById(R.id.ratingStar);
                    ratingBar.setRating((float)rate);
                    ((TextView)findViewById(R.id.subdescription2)).setText(recipe.getCooking_description().get(0).getDescription());
                    ((TextView)findViewById(R.id.writerid2)).setText("by " + recipe.getWriter_name());
                }

                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {
                    Toast.makeText(RecipeMainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (call2 != null) {
            call2.enqueue(new Callback<List<CommentData>>() {
                @Override
                public void onResponse(Call<List<CommentData>> call2, Response<List<CommentData>> response2) {
                    Log.i("TEST", call2.toString());
                    Log.i("TEST", response2.toString());
                    commentDataList = response2.body();
                    if(commentDataList.size()==0){
                    }
                    else {
                        for (int i = 0; i < commentDataList.size(); i++) {
                            commentAdapter.addItem(commentDataList.get(i), i);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<CommentData>> call2, Throwable t) {
                }
            });
        }

        ((ImageButton)findViewById(R.id.back2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeMainActivity.super.onBackPressed();
            }
        });

        ((Button)findViewById(R.id.cook)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeMainActivity.this, RecipeActivity.class);
                intent.putExtra("recipeId",recipeId);
                intent.putExtra("recipeDescriptionNum",descriptionNum);
                startActivity(intent);
            }
        });

        ((ImageButton)findViewById(R.id.btnaddcomment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.u_id == null){
                    showLoginAlertDialog("login");
                }
                else if(((EditText)findViewById(R.id.contents)).getText().toString().equals("")){
                    showLoginAlertDialog("noContents");
                }
                else{
                    Comment comment = new Comment(
                        ((EditText)findViewById(R.id.contents)).getText().toString(),
                        MainActivity.u_id,
                        MainActivity.u_name,
                        recipeId,
                        ((TextView)findViewById(R.id.recipeName)).getText().toString()
                );

                    Call<ResponseBody> call3 = RetrofitClient.getInstance().getApiService().postComment(comment);
                    call3.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call3, Response<ResponseBody> response3) {
                            finish();
                            startActivity(getIntent());
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call3, Throwable t) {
                        }
                    });
                }
            }
        });
        ((Button)findViewById(R.id.rating2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.u_id == null){
                    showLoginAlertDialog("login");
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipeMainActivity.this);

                    final View layout = getLayoutInflater().inflate(R.layout.item_rating_dialog, null);
                    builder.setView(layout);
                    final AlertDialog dialog = builder.create();

                    ((Button)layout.findViewById(R.id.rate)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Call<ResponseBody> call4 = RetrofitClient.getInstance()
                                    .getApiService()
                                    .rate(recipeId, (double)((RatingBar)layout.findViewById(R.id.score)).getRating(),  MainActivity.u_id);
                            call4.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call4, Response<ResponseBody> response4) {
                                    dialog.dismiss();
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call4, Throwable t) {
                                    Toast.makeText(getApplicationContext(),"다시 시도해주세요",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    ((Button)layout.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        ((Button)findViewById(R.id.addshortcut2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.u_id == null){
                    showLoginAlertDialog("login");
                }
                else{
                    Call<Integer> call5 = RetrofitClient.getInstance().getApiService().addShortCut(recipeId, MainActivity.u_id);
                    call5.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call5, Response<Integer> response5) {
                            if(response5.body() == 1)
                                Toast.makeText(getApplicationContext(),"관심있는 레시피 추가",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(),"관심있는 레시피 삭제",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Call<Integer> call5, Throwable t) {
                            Toast.makeText(getApplicationContext(),"다시 시도해주세요",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeMainActivity.this,RecipeFormActivity.class);
                intent.putExtra("type", RecipeFormActivity.MODIFY_MY_RECIPE);
                intent.putExtra("recipeId",recipeId);
                startActivityForResult(intent,REQUEST_MODIFY2);
            }
        });
    }

    private void recyclerViewInit() {
        RecyclerView mMainIngredientsView = findViewById(R.id.mainingredients2);
        RecyclerView mSubIngredientsView = findViewById(R.id.subingredients2);
        RecyclerView mCommentsView = findViewById(R.id.comments2);

        LinearLayoutManager mLinearLayoutManagerMain = new LinearLayoutManager(this);
        mMainIngredientsView.setLayoutManager(mLinearLayoutManagerMain);
        LinearLayoutManager mLinearLayoutManagerSub = new LinearLayoutManager(this);
        mSubIngredientsView.setLayoutManager(mLinearLayoutManagerSub);
        LinearLayoutManager mLinearLayoutManagerComment = new LinearLayoutManager(this);
        mCommentsView.setLayoutManager(mLinearLayoutManagerComment);

        mMainIngredientAdapter = new IngredientsAdapter();
        mSubIngredientAdapter = new IngredientsAdapter();
        commentAdapter = new CommentAdapter();

        mMainIngredientsView.setAdapter(mMainIngredientAdapter);
        mSubIngredientsView.setAdapter(mSubIngredientAdapter);
        mCommentsView.setAdapter(commentAdapter);
    }

    public void showLoginAlertDialog(String reason){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_error_outline_24dp);
        switch (reason){
            case "login":
                builder.setTitle("로그인 해주세요 :(");
                builder.setMessage("로그인을 해야 가능합니다.");
                break;
            case "noContents":
                builder.setTitle("내용을 작성 해주세요 :(");
                builder.setMessage("내용을 입력하셔야 가능합니다.");
                break;
        }
        builder.setNegativeButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MODIFY2) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                intent.putExtra("result", "some value");
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
    @Override
    public void onBackPressed(){
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }
}
