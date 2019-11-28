package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeActivity;
import com.example.yobo_android.activity.RecipeMainActivity;
import com.example.yobo_android.etc.Recipe;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
* BoardActivity에서 목록을 구성하는 item viewholder를 만들어주는 Adapter
*/


// recylerView는 개발자가 어댑터를 직접 구현해야함. 반드시 RecyclerView.Adapter 상속해서 구현
public class  BoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static final String ImageURL = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=";
    private static final int TYPE_ITEM = 1;

    // adapter에 들어갈 list 입니다.
    private ArrayList<Recipe> listRecipe = new ArrayList<>();
    private Context context;

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    // 즉 item view를 저장하는 뷰홀드 클래스
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String recipeId;
        private int recipeDescriptionNum;
        private TextView recipeName;
        private TextView recipeSubContents;
        private TextView recipeWriter;
        private TextView recipeScore;
        private ImageView recipeImage;

        ItemViewHolder(View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipeName);
            recipeSubContents = itemView.findViewById(R.id.recipeSubContents);
            recipeWriter = itemView.findViewById(R.id.recipeWriter);
            recipeScore = itemView.findViewById(R.id.recipeScore);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }

        void onBind(Recipe recipe, int position) {
            String temp = recipe.getCooking_description().get(0).getImage();
            temp = temp.replace("/", "%2F");
            String sum = ImageURL + temp;
            try {
                URL url = new URL(sum);
            Picasso.get().load(url.toString()).into(recipeImage);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            recipeId = recipe.get_id();
            recipeDescriptionNum = recipe.getCooking_description().size();
            recipeName.setText(recipe.getRecipe_name());
            recipeSubContents.setText(recipe.getCooking_description().get(0).getDescription());
            recipeWriter.setText(recipe.getWriter_id());
            recipeScore.setText(""+recipe.getRating());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition() ;
            if (pos != RecyclerView.NO_POSITION) { // 갱신하는 과정에서 뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 NO_POSITION 리턴

                //getAdapterPosition();
//                Intent intent = new Intent(context, RecipeActivity.class);

                Intent intent = new Intent(context, RecipeMainActivity.class);
                //doc Id를 넘기고 recipeActivity에서 이걸로 레시피 정보를 서버에 요청
                intent.putExtra("recipeId",recipeId);
                context.startActivity(intent);
            }
        }
    }

    // item view를 위한 viewHolder 객체 생성 및 return
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);
        holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.onBind(listRecipe.get(position), position);

    }

    @Override
    public int getItemViewType(int position){
        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listRecipe.size();
    }

    public void addItem(Recipe recipe, int position) {
        // 외부에서 item을 추가시킬 함수입니다.
        listRecipe.add(recipe);
        notifyItemChanged(position);
    }
}
