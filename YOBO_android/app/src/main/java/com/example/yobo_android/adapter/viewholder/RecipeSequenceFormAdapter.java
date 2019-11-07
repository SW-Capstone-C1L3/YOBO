package com.example.yobo_android.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.etc.RecipeSequenceFormData;

import java.util.List;

public class RecipeSequenceFormAdapter extends RecyclerView.Adapter<RecipeSequenceFormAdapter.ViewHolder> {
    public List<RecipeSequenceFormData> mDataList = null;

    public RecipeSequenceFormAdapter(List<RecipeSequenceFormData> mDataList) {
        this.mDataList = mDataList;
    }

    public interface OnItemClickListener{
        void onItemClick(View v,int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mRecipeSequenceFormImage;
        EditText mRecipeSequenceFormText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeSequenceFormImage = itemView.findViewById(R.id.recipeSequenceFormImage);
            mRecipeSequenceFormText = itemView.findViewById(R.id.recipeSequenceFormText);

            mRecipeSequenceFormImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_sequence_form, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        RecipeSequenceFormData item = mDataList.get(position);
        //holder.mRecipeSequenceFormImage.setImageResource(item.getRecipeSequenceFormImageId());
        holder.mRecipeSequenceFormImage.setImageBitmap(item.getRecipeSequenceFormImageId());
        holder.mRecipeSequenceFormText.setText(item.getRecipeSequenceFormDescription());

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
