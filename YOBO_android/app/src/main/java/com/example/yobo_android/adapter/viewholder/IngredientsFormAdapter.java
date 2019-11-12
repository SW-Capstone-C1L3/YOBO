package com.example.yobo_android.adapter.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
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
        return new ViewHolder(view, new EditTextListenerForName(), new EditTextListenerForQuantity());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IngredientsFormData item = mDataList.get(position);

        holder.textListenerForName.updatePosition(holder.getAdapterPosition());
        holder.textListenerForQuantity.updatePosition(holder.getAdapterPosition());

        holder.mIngredientNameText.setText(item.getIngredientsName());
        holder.mIngredientQuantityText.setText(item.getIngredientsQuantity());

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    // ViewHolder 만든 것
    public static class ViewHolder extends RecyclerView.ViewHolder{
        EditTextListenerForName textListenerForName;
        EditTextListenerForQuantity textListenerForQuantity;
        EditText mIngredientNameText;
        EditText mIngredientQuantityText;

        public ViewHolder(@NonNull View itemView, EditTextListenerForName textListenerForName,
                          EditTextListenerForQuantity textListenerForQuantity) {
            super(itemView);
            mIngredientNameText = itemView.findViewById(R.id.ingredientNameText);
            mIngredientQuantityText = itemView.findViewById(R.id.ingredientQuantityText);

            this.textListenerForName = textListenerForName;
            mIngredientNameText.addTextChangedListener(textListenerForName);
            this.textListenerForQuantity = textListenerForQuantity;
            mIngredientQuantityText.addTextChangedListener(textListenerForQuantity);
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
            mDataList.get(position).setIngredientsName(s.toString());
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
            mDataList.get(position).setIngredientsQuantity(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
