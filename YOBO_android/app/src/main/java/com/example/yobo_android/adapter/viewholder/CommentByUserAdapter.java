package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.MainActivity;
import com.example.yobo_android.activity.RecipeMainActivity;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.CommentData;
import com.example.yobo_android.etc.Recipe;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CommentByUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
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
        private String HHMMSS;
        private String recipe_id;
        private ImageView comment_recipe_image;
        private TextView comment_recipe_name;
        private TextView comment_timestamp;
        private TextView comment_content;


        ItemViewHolder(View itemView) {
            super(itemView);
            comment_recipe_image = itemView.findViewById(R.id.myCommentRecipeImage);
            comment_recipe_name = itemView.findViewById(R.id.RecipeName);
            comment_timestamp = itemView.findViewById(R.id.commentTimestamp);
            comment_content = itemView.findViewById(R.id.comment_info);
        }

        void onBind(CommentData commentData, int position) {
            YYMMDD = commentData.getTimestamp();
            String[] splitText = YYMMDD.split("T");
            for(int i=0;i<splitText.length;i++) {
                if(i==0)
                    YYMMDD=splitText[i];
            }

            user_id = commentData.getUser_id();
            comment_id = commentData.get_id();
            comment_recipe_name.setText(commentData.getRecipe_name());             /*****여기 수정해야됨****/
            comment_timestamp.setText(YYMMDD);
            comment_content.setText(commentData.getComments());
            recipe_id = commentData.getRecipe_id();
            Call<Recipe> call = RetrofitClient.getInstance().getApiService().getReicpebyDid(recipe_id);
            if(call != null) {
                call.enqueue(new Callback<Recipe>() {
                    @Override
                    public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                        String temp = response.body().getCooking_description().get(0).getImage();
                        temp = temp.replace("/", "%2F");
                        String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                        try {
                            URL url = new URL(sum);
                            Picasso.get().load(url.toString()).into(comment_recipe_image);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<Recipe> call, Throwable t) {
                        Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition() ;
            Intent intent = new Intent(context, RecipeMainActivity.class);
            //doc Id를 넘기고 recipeActivity에서 이걸로 레시피 정보를 서버에 요청
            intent.putExtra("recipeId",recipe_id);
//            intent.putExtra("recipeName",comment_recipe_name.getText().toString());
            intent.putExtra("fromComment","re");
            context.startActivity(intent);
        }
    }

    // item view를 위한 viewHolder 객체 생성 및 return
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_by_user, parent, false);
        holder = new CommentByUserAdapter.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        CommentByUserAdapter.ItemViewHolder itemViewHolder = (CommentByUserAdapter.ItemViewHolder) holder;
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
