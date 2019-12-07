package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.ShopIngredientActivity;
import com.example.yobo_android.etc.Cooking_ingredient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Cooking_ingredient> mListIngredient = new ArrayList<>();
    private Context mContext;

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mIngredientName;
        private TextView mIngredientQty;
        private TextView mIngredientUnit;

        ItemViewHolder(View itemView) {
            super(itemView);

            mIngredientName = itemView.findViewById(R.id.ingredientname2);
            mIngredientQty = itemView.findViewById(R.id.ingredientqty2);
            mIngredientUnit = itemView.findViewById(R.id.ingredientunit2);
        }

        void onBind(Cooking_ingredient ingredient, int position) {
            mIngredientName.setText(ingredient.getIngredients_name());
            if(Math.floor(ingredient.getQty()) == ingredient.getQty()) // integer
                mIngredientQty.setText(Integer.valueOf(ingredient.getQty().intValue()).toString());
            else
                mIngredientQty.setText(ingredient.getQty().toString()); // float
            mIngredientUnit.setText(ingredient.getUnit());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(getAdapterPosition() != RecyclerView.NO_POSITION){
                Intent intent = new Intent(mContext, ShopIngredientActivity.class);
                intent.putExtra("query", mIngredientName.getText().toString());
                mContext.startActivity(intent);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.onBind(mListIngredient.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mListIngredient.size();
    }

    public void addItem(Cooking_ingredient ingredient) {
        mListIngredient.add(ingredient);
    }
}
