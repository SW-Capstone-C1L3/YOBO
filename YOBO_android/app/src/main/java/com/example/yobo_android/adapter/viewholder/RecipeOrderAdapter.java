package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.etc.RecipeOrder;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeOrderAdapter extends RecyclerView.Adapter<RecipeOrderAdapter.ItemViewHolder> {

    private ArrayList<RecipeOrder> listRecipeOrder = new ArrayList<>();
    private Context context;

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Button btnRecipeOrderNumber;
        private TextView mRecipeDescription;
        private ImageView mRecipeOrderImage;

        ItemViewHolder(View itemView){
            super(itemView);

            btnRecipeOrderNumber = itemView.findViewById(R.id.recipeOrderNumber);
            mRecipeDescription = itemView.findViewById(R.id.recipeDescription);
            mRecipeOrderImage = itemView.findViewById(R.id.recipeOrderImage);
        }

        void onBind(RecipeOrder recipeOrder) {
            btnRecipeOrderNumber.setText("" + recipeOrder.getRecipeOrderNumber());
            mRecipeDescription.setText(recipeOrder.getRecipeOrderDescription());
            mRecipeOrderImage.setImageResource(recipeOrder.getRecipeOrderImageId());

            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){

        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        this.context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_order, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position){
        holder.onBind(listRecipeOrder.get(position));
    }

    @Override
    public int getItemCount(){
        return listRecipeOrder.size();
    }

    public void addItem(RecipeOrder recipeOrder){
        listRecipeOrder.add(recipeOrder);
    }
}
