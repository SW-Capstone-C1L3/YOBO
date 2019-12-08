package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.BasketActivity;
import com.example.yobo_android.activity.MainActivity;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.CommentData;
import com.example.yobo_android.etc.IngredientsBasketData;
import com.example.yobo_android.etc.UserData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 1;
    // adapter에 들어갈 list 입니다.
    private ArrayList<CommentData> commentlist = new ArrayList<>();
    private Context context;

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    // 즉 item view를 저장하는 뷰홀드 클래스
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String user_id;
        private String comment_id;
        private String YYMMDD;
        private ImageView mCommentUserImage;
        private TextView comment_writer;
        private TextView comment_timestamp;
        private TextView comment_content;

        ItemViewHolder(View itemView) {
            super(itemView);
            mCommentUserImage = itemView.findViewById(R.id.commentUserImage);
            comment_writer = itemView.findViewById(R.id.Comment_writer_name);
            comment_timestamp = itemView.findViewById(R.id.comment_timestamp);
            comment_content = itemView.findViewById(R.id.Comment_content);
        }

        void onBind(CommentData commentData, int position) {

            YYMMDD = commentData.getTimestamp();
            String[] splitText = YYMMDD.split("T");
            for(int i=0;i<splitText.length;i++) {
                if(i==0)
                    YYMMDD=splitText[i];
            }

            user_id = commentData.getUser_id();
            OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttpClientBuilder.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClientBuilder.build())
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            Call<UserData> call = apiService.getbyDid(user_id);
            if (call != null) {
                call.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        if(response.body().getImage() != null){
                            String temp =  response.body().getImage();
                            temp = temp.replace("/", "%2F");
                            String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                            Uri uri = Uri.parse(sum);
                            Picasso.get().load(uri).fit().centerInside().error(R.drawable.user).into(mCommentUserImage);
                        }
                    }
                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Toast.makeText(context,"asd",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            comment_id = commentData.get_id();
            comment_writer.setText(commentData.getUser_name());
            comment_timestamp.setText(YYMMDD);
            comment_content.setText(commentData.getComments());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { int pos = getAdapterPosition() ;}
    }

    // item view를 위한 viewHolder 객체 생성 및 return
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        holder = new CommentAdapter.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        CommentAdapter.ItemViewHolder itemViewHolder = (CommentAdapter.ItemViewHolder) holder;
        Log.i("kkkk onBindView ",commentlist.get(position)+"");
        itemViewHolder.onBind(commentlist.get(position), position);
    }

    @Override
    public int getItemViewType(int position){ return TYPE_ITEM; }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return commentlist.size();
    }

    public void addItem(CommentData commentData, int position) {
        // 외부에서 item을 추가시킬 함수입니다.
        commentlist.add(commentData);
        Log.i("kkkk additem ",commentData+"");
        notifyItemChanged(position);
    }
}
