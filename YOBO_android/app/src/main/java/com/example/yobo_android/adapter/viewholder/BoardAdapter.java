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
import com.example.yobo_android.etc.Recipe;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// recylerView는 개발자가 어댑터를 직접 구현해야함. 반드시 RecyclerView.Adapter 상속해서 구현
public class BoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    // adapter에 들어갈 list 입니다.
    private ArrayList<Recipe> listRecipe = new ArrayList<>();
    private Context context;

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View headerView){
            super(headerView);
        }
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    // 즉 item view를 저장하는 뷰홀드 클래스
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            recipeName.setText(recipe.getRecipeName());
            recipeSubContents.setText(recipe.getRecipeSubContents());
            recipeWriter.setText(recipe.getRecipeWriter());
            recipeScore.setText(""+recipe.getRecipeScore());
            recipeImage.setImageResource(recipe.getRecipeImageId());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition() ;
            if (pos != RecyclerView.NO_POSITION) { // 갱신하는 과정에서 뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 NO_POSITION 리턴

                //getAdapterPosition();
                Intent intent = new Intent(context, RecipeActivity.class);
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

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        if(viewType == TYPE_HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_header, parent, false);
            holder = new HeaderViewHolder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);
            holder = new ItemViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        }
        else{
            // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.onBind(listRecipe.get(position), position); // if add header, listRecipe.get(position - 1)
        }
    }

    @Override
    public int getItemViewType(int position){
//        if(position == 0)
//            return TYPE_HEADER;
//        else
            return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listRecipe.size();
    }

    public void addItem(Recipe recipe) {
        // 외부에서 item을 추가시킬 함수입니다.
        listRecipe.add(recipe);
    }
}
