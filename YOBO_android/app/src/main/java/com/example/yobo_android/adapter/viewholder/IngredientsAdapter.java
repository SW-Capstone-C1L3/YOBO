package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.etc.Cooking_ingredient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ItemViewHolder>{

    // adapter에 들어갈 list 입니다.
    private ArrayList<Cooking_ingredient> mListIngredient = new ArrayList<>();
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
            mIngredientUnit = itemView.findViewById(R.id.ingredientunit2);
        }

        void onBind(Cooking_ingredient ingredient) {
            mIngredientName.setText(ingredient.getIngredients_name());
            if(Math.floor(ingredient.getQty()) == Integer.valueOf(ingredient.getQty().intValue())){
                mIngredientQty.setText(Integer.valueOf(ingredient.getQty().intValue())+"");
            }
            else{
                mIngredientQty.setText(ingredient.getQty()+"");
            }
            mIngredientUnit.setText(ingredient.getUnit());
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

    public void addItem(Cooking_ingredient ingredient) {
        // 외부에서 item을 추가시킬 함수입니다.
        mListIngredient.add(ingredient);
    }
}
