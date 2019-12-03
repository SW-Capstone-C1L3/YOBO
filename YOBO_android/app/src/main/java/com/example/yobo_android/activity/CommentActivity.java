package com.example.yobo_android.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BoardAdapter;
import com.example.yobo_android.adapter.viewholder.CommentAdapter;
import com.example.yobo_android.adapter.viewholder.CommentByUserAdapter;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.CommentData;
import com.example.yobo_android.etc.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentActivity extends AppCompatActivity {
    private CommentByUserAdapter adapter_comment;
    private String comments;
    List<CommentData> commentList = new ArrayList<>();
    Retrofit retrofit;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        if(getIntent().getStringExtra("comments")!=null)
            comments = getIntent().getStringExtra("comments");
        recyclerViewInit();
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

        Call<List<CommentData>> call2 = null;
        if(comments!=null){
            call2 = apiService.getCommentsbyUId(comments,0,10);
        }
        if(call2!=null){
            call2.enqueue(new Callback<List<CommentData>>() {
                @Override
                public void onResponse(Call<List<CommentData>> call, Response<List<CommentData>> response) {
                    //Toast.makeText(BoardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Log.i("TEST", call.toString());
                    Log.i("TEST", response.toString());
                    commentList = response.body();
                    if(commentList.size()==0){
                        Intent intent = new Intent();
                        intent.putExtra("result", "some value");
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        for (int i = 0; i < commentList.size(); i++) {
                            adapter_comment.addItem(commentList.get(i), i);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<CommentData>> call, Throwable t) {
                    Log.e("ERROR", call.toString());
                    Log.e("ERROR", t.toString());
                }
            });
        }
    }

    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.recyclerCommentView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter_comment = new CommentByUserAdapter();
        recyclerView.setAdapter(adapter_comment);
    }
}
