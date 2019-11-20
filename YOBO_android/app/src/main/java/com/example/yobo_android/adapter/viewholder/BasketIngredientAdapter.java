package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.BasketActivity;
import com.example.yobo_android.etc.IngredientsBasketData;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BasketIngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 1;
    // adapter에 들어갈 list 입니다.
    private ArrayList<IngredientsBasketData> listBasketIngredient = new ArrayList<>();
    private Context context;
    private String basket_product_Id;
    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    // 즉 item view를 저장하는 뷰홀드 클래스
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String user_id;
        private String ingredientDescription;
        private ImageView ingredient_image;
        private TextView ingredient_name;
        private TextView ingredient_price;
        private TextView basket_qty;
        private TextView tot_price;
        private Button btnDelete;

        ItemViewHolder(View itemView) {
            super(itemView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ingredient_image = itemView.findViewById(R.id.ShopIngredientImage);
            ingredient_name = itemView.findViewById(R.id.BaksetIngredientName);
            basket_qty = itemView.findViewById(R.id.basket_ingredient_qty);
            ingredient_price = itemView.findViewById(R.id.BaksetIngredientPrice);
            tot_price = itemView.findViewById(R.id.ingredient_total_Price);
        }

        void onBind(IngredientsBasketData ingredientsBasketData, int position) {
            //ingredient_image.setImageResource(Integer.parseInt(ingredientsBasketData.getIngredientImage()));
            basket_product_Id = ingredientsBasketData.getBasket_product_id();
            btnDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Log.i("kkkkkkk click, pos: ",String.valueOf(position));
                    Log.i("kkkkkk,return",listBasketIngredient.get(getAdapterPosition()).getBasket_product_id());
                    ((BasketActivity)context).delete(listBasketIngredient.get(getAdapterPosition()).getBasket_product_id(),listBasketIngredient.get(getAdapterPosition()).getIngredientName(),getAdapterPosition());
                }
            });
            ingredient_name.setText(ingredientsBasketData.getIngredientName());
            ingredient_price.setText(String.valueOf(ingredientsBasketData.getIngredientPrice()) + "원");
            basket_qty.setText("개수: " +String.valueOf(ingredientsBasketData.getBasket_qty()));
            tot_price.setText("총 금액: "+String.valueOf(ingredientsBasketData.getBasket_qty()*ingredientsBasketData.getIngredientPrice())+"원");       /***1개당 가격도 곱해야됨***/
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { int pos = getAdapterPosition() ;}
    }

    // item view를 위한 viewHolder 객체 생성 및 return
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basket_ingredient, parent, false);
        holder = new BasketIngredientAdapter.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        BasketIngredientAdapter.ItemViewHolder itemViewHolder = (BasketIngredientAdapter.ItemViewHolder) holder;
        itemViewHolder.onBind(listBasketIngredient.get(position), position); // if add header, listRecipe.get(position - 1)
    }

    @Override
    public int getItemViewType(int position){ return TYPE_ITEM; }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listBasketIngredient.size();
    }

    public void addItem(IngredientsBasketData ingredientsBasketData) {
        // 외부에서 item을 추가시킬 함수입니다.
        listBasketIngredient.add(ingredientsBasketData);
    }
    public void removeItem(int position){
        Log.i("kkkkk remove",String.valueOf(position));
        listBasketIngredient.remove(position);
        notifyItemRemoved(position);
    }
}
