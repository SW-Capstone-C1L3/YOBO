package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.BasketActivity;
import com.example.yobo_android.etc.CommentData;
import com.example.yobo_android.etc.IngredientsBasketData;

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
        private String temp;
        private String temp2;
        private TextView comment_writer;
        private TextView comment_timestamp;
        private TextView comment_content;

        ItemViewHolder(View itemView) {
            super(itemView);
            comment_writer = itemView.findViewById(R.id.Comment_writer_name);
            comment_timestamp = itemView.findViewById(R.id.comment_timestamp);
            comment_content = itemView.findViewById(R.id.Comment_content);
        }

        void onBind(CommentData commentData, int position) {
            Log.i("kkkk onBind ",commentData+"");
            Log.i("kkkk onBind ",commentData.getU_id()+"");
            temp = commentData.getTs();
            Log.i("kkkkkk",temp);
            String[] splitText = temp.split("T");
            for(int i=0;i<splitText.length;i++) {
                Log.i("kkkkkk test split", splitText[i]);
                if(i==0)
                    temp=splitText[i];
                if(i==1)
                    temp2 = splitText[i];
            }
            temp2 = temp2.substring(0,8);
            temp = temp + " " + temp2;
            user_id = commentData.getU_id();
            comment_id = commentData.getComment_id();
            comment_writer.setText(commentData.getU_name());
            comment_timestamp.setText(temp);
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
