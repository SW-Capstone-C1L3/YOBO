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
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.CommentData;
import com.example.yobo_android.etc.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentActivity extends AppCompatActivity {
    private String r_id;
    private String u_id;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        r_id = getIntent().getStringExtra("recipe_id");
        //recyclerViewInit();
        //new CommentActivity.RequestAsync().execute();
    }
//
//    public void jsonParser(String json) {
//        try {
//            JSONArray CommentList = new JSONArray(json);
//            for(int i=CommentList.length()-1; i>=0; i--){
//                CommentData commentItem = new CommentData();
//                JSONObject comment = CommentList.getJSONObject(i);
//                commentItem.setComment_id(comment.getString("_id"));
//                commentItem.setComments(comment.getString("comments"));
//                commentItem.setU_id(comment.getString("user_id"));
//                commentItem.setU_name(comment.getString("user_name"));
//                commentItem.setTs(comment.getString("timestamp"));
//                commentItem.setR_id(comment.getString("recipe_id"));
//                adapter.addItem(commentItem,i);
//            }
//            if(CommentList.length()==0){
//                Intent intent = new Intent();
//                intent.putExtra("result","no comment");
//                setResult(RESULT_OK,intent);
//                finish();
//            }
//            adapter.notifyDataSetChanged();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public class RequestAsync extends AsyncTask<String,String,String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/comments/getCommentsbyRId?RId="+r_id +"&pageNum=0");
//            } catch (Exception e) {
//                return new String("Exception: " + e.getMessage());
//            }
//        }
//        @Override
//        protected void onPostExecute(String s) {
//            if (s != null) {
//                jsonParser(s);
//            }
//        }
//    }
//
//    private void recyclerViewInit() {
//        RecyclerView recyclerView = findViewById(R.id.recyclerCommentView);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapter = new CommentAdapter();
//        recyclerView.setAdapter(adapter);
//    }
}
