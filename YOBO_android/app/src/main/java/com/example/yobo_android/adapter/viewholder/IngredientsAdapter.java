package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeMainActivity;
import com.example.yobo_android.etc.Main_cooking_ingredient;
import com.example.yobo_android.etc.Recipe;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ItemViewHolder>{

    // adapter에 들어갈 list 입니다.
    private ArrayList<Main_cooking_ingredient> mListIngredient = new ArrayList<>();
    private Context mContext;

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    // 즉 item view를 저장하는 뷰홀드 클래스
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mIngredientName;
        private TextView mIngredientQty;
        private TextView mIngredientUnit;

        ItemViewHolder(View itemView) {
            super(itemView);

            mIngredientName = itemView.findViewById(R.id.ingredientname2);
            mIngredientQty = itemView.findViewById(R.id.ingredientqty2);
            mIngredientUnit = itemView.findViewById(R.id.ingredientqty2);
        }

        void onBind(Main_cooking_ingredient ingredient) {
            mIngredientName.setText(ingredient.getIngredients_name());
            mIngredientQty.setText(ingredient.getQty()+"");
            mIngredientQty.setText(ingredient.getUnit());
        }
    }

    // item view를 위한 viewHolder 객체 생성 및 return
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(mListIngredient.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return mListIngredient.size();
    }

    public void addItem(Main_cooking_ingredient ingredient) {
        // 외부에서 item을 추가시킬 함수입니다.
        mListIngredient.add(ingredient);
    }
}
