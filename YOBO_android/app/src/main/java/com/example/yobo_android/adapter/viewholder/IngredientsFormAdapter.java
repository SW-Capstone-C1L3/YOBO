package com.example.yobo_android.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.etc.IngredientsFormData;

import java.util.List;

public class IngredientsFormAdapter extends RecyclerView.Adapter<IngredientsFormAdapter.ViewHolder> {

    private List<IngredientsFormData> mDataList;

    public IngredientsFormAdapter(List<IngredientsFormData> dataList){
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient_form,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IngredientsFormData item = mDataList.get(position);
        holder.ingredientNameText.setText(item.getIngredientsText());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    // ViewHolder 만든 것
    public static class ViewHolder extends RecyclerView.ViewHolder{
        EditText ingredientNameText;
        EditText ingredientQuantityText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientNameText = itemView.findViewById(R.id.ingredientNameDescription);
            ingredientQuantityText = itemView.findViewById(R.id.ingredientQuantityDescription);
        }
    }

}
