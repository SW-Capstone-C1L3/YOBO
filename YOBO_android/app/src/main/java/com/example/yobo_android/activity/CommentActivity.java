package com.example.yobo_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.CommentByUserAdapter;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.CommentData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    private CommentByUserAdapter commentAdapter;
    private RecyclerView recyclerView;
    private LinearLayout mLayoutEmptyNotify;
    private String comments;
    List<CommentData> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getStringExtra("comments")!=null)
            comments = getIntent().getStringExtra("comments");
        recyclerViewInit();

        Call<List<CommentData>> call2 = null;
        if(comments!=null){
            call2 = RetrofitClient.getInstance().getApiService().getCommentsbyUId(comments,0,10);
        }
        if(call2!=null){
            call2.enqueue(new Callback<List<CommentData>>() {
                @Override
                public void onResponse(Call<List<CommentData>> call, Response<List<CommentData>> response) {
                    //Toast.makeText(BoardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Log.i("TEST", call.toString());
                    Log.i("TEST", response.toString());
                    commentList = response.body();
                    Collections.reverse(commentList);
                    if(commentList.size()==0){
                        //여기에 작업
                        recyclerView.setVisibility(View.GONE);
                        mLayoutEmptyNotify.setVisibility(View.VISIBLE);
                    }
                    else {
                        for (int i = 0; i < commentList.size(); i++) {
                            commentAdapter.addItem(commentList.get(i), i);
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
        mLayoutEmptyNotify = findViewById(R.id.emptyNotifyLayout);
        mLayoutEmptyNotify.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerCommentView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentByUserAdapter();
        recyclerView.setAdapter(commentAdapter);
    }
}
