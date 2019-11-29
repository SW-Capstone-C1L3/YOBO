package com.example.yobo_android.adapter.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.etc.Cooking_ingredient;

import java.util.List;

public class IngredientsFormAdapter extends RecyclerView.Adapter<IngredientsFormAdapter.ViewHolder> {

    private List<Cooking_ingredient> mDataList;

    public IngredientsFormAdapter(List<Cooking_ingredient> dataList){
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient_form,parent,false);
        return new ViewHolder(view,
                new EditTextListenerForName(),
                new EditTextListenerForQuantity(),
                new EditTextListenerForUnit());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cooking_ingredient item = mDataList.get(position);

        holder.textListenerForName.updatePosition(holder.getAdapterPosition());
        holder.textListenerForQuantity.updatePosition(holder.getAdapterPosition());
        holder.textListenerForUnit.updatePosition(holder.getAdapterPosition());

        holder.mIngredientNameText.setText(item.getIngredients_name());
        if(item.getQty() == null)
            holder.mIngredientQuantityText.setText("");
        else
            holder.mIngredientQuantityText.setText(item.getQty().toString());
        holder.mIngredientUnitText.setText(item.getUnit());

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    // ViewHolder 만든 것
    public static class ViewHolder extends RecyclerView.ViewHolder{
        EditTextListenerForName textListenerForName;
        EditTextListenerForQuantity textListenerForQuantity;
        EditTextListenerForUnit textListenerForUnit;
        EditText mIngredientNameText;
        EditText mIngredientQuantityText;
        EditText mIngredientUnitText;

        public ViewHolder(@NonNull View itemView, EditTextListenerForName textListenerForName,
                          EditTextListenerForQuantity textListenerForQuantity,
                          EditTextListenerForUnit textListenerForUnit) {
            super(itemView);
            mIngredientNameText = itemView.findViewById(R.id.ingredientNameText);
            mIngredientQuantityText = itemView.findViewById(R.id.ingredientQuantityText);
            mIngredientUnitText = itemView.findViewById(R.id.ingredientUnitText);

            this.textListenerForName = textListenerForName;
            mIngredientNameText.addTextChangedListener(textListenerForName);
            this.textListenerForQuantity = textListenerForQuantity;
            mIngredientQuantityText.addTextChangedListener(textListenerForQuantity);
            this.textListenerForUnit = textListenerForUnit;
            mIngredientUnitText.addTextChangedListener(textListenerForUnit);
        }
    }

    public class EditTextListenerForName implements TextWatcher{
        private int position;

        public void updatePosition(int position){
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDataList.get(position).setIngredients_name(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class EditTextListenerForQuantity implements TextWatcher{
        private int position;

        public void updatePosition(int position){
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                mDataList.get(position).setQty(Double.valueOf(s.toString().trim()));
            }catch (NumberFormatException e){
                Log.e("ERROR", e.toString());
                mDataList.get(position).setQty(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class EditTextListenerForUnit implements TextWatcher{
        private int position;

        public void updatePosition(int position){
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDataList.get(position).setUnit(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
