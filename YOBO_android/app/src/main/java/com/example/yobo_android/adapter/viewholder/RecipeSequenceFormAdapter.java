package com.example.yobo_android.adapter.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
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
        EditTextListenerForRecipeSeqForm editTextListenerForRecipeSeqForm;
        ImageView mRecipeSequenceFormImage;
        EditText mRecipeSequenceFormText;

        public ViewHolder(@NonNull View itemView,
                          EditTextListenerForRecipeSeqForm editTextListenerForRecipeSeqForm) {
            super(itemView);
            mRecipeSequenceFormText = itemView.findViewById(R.id.recipeSequenceFormText);
            mRecipeSequenceFormImage = itemView.findViewById(R.id.recipeSequenceFormImage);

            this.editTextListenerForRecipeSeqForm = editTextListenerForRecipeSeqForm;
            mRecipeSequenceFormText.addTextChangedListener(editTextListenerForRecipeSeqForm);

            mRecipeSequenceFormImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecipeSequenceFormText.clearFocus();
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
        return new ViewHolder(view, new EditTextListenerForRecipeSeqForm());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        RecipeSequenceFormData item = mDataList.get(position);
        //holder.mRecipeSequenceFormImage.setImageResource(item.getRecipeSequenceFormImageId());
        holder.editTextListenerForRecipeSeqForm.updatePosition(holder.getAdapterPosition());
        holder.mRecipeSequenceFormImage.setImageBitmap(item.getRecipeSequenceFormImageId());
        holder.mRecipeSequenceFormText.setText(item.getRecipeSequenceFormDescription());

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class EditTextListenerForRecipeSeqForm implements TextWatcher{
        private int position;

        public void updatePosition(int position){
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDataList.get(position).setRecipeSequenceFormDescription(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
