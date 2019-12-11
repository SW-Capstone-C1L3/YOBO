package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
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
import com.squareup.picasso.Picasso;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BasketIngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static final String ImageURL = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=";
    private static final int TYPE_ITEM = 1;
    private ArrayList<IngredientsBasketData> listBasketIngredient = new ArrayList<>();
    private Context context;
    private String basket_product_Id;

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String user_id;
        private String ingredientDescription;
        private ImageView ingredient_image;
        private TextView ingredient_name;
        private TextView ingredient_price;
        private TextView basket_qty;
        private TextView tot_price;
        private Button btnDelete;
        private Integer cost;
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
            String temp = ingredientsBasketData.getIngredientImage();
            temp = temp.replace("/", "%2F");
            String sum = ImageURL + temp;
            try {
                URL url = new URL(sum);
                Picasso.get().load(url.toString()).into(ingredient_image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            basket_product_Id = ingredientsBasketData.getBasket_product_id();
            btnDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    cost = listBasketIngredient.get(getAdapterPosition()).getBasket_qty()*listBasketIngredient.get(getAdapterPosition()).getIngredientPrice();
                    ((BasketActivity)context).delete(listBasketIngredient.get(getAdapterPosition()).getBasket_product_id(),listBasketIngredient.get(getAdapterPosition()).getIngredientName(),getAdapterPosition(),cost);
                }
            });
            ingredient_name.setText(ingredientsBasketData.getIngredientName());
            ingredient_price.setText(String.valueOf(ingredientsBasketData.getIngredientPrice()) + "원/");
            basket_qty.setText("수량 : " +String.valueOf(ingredientsBasketData.getBasket_qty()));
            tot_price.setText("총 금액 : "+String.valueOf(ingredientsBasketData.getBasket_qty()*ingredientsBasketData.getIngredientPrice())+"원");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { int pos = getAdapterPosition() ;}
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basket_ingredient, parent, false);
        holder = new BasketIngredientAdapter.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BasketIngredientAdapter.ItemViewHolder itemViewHolder = (BasketIngredientAdapter.ItemViewHolder) holder;
        itemViewHolder.onBind(listBasketIngredient.get(position), position);
    }

    @Override
    public int getItemViewType(int position){ return TYPE_ITEM; }

    @Override
    public int getItemCount() {
        return listBasketIngredient.size();
    }

    public void addItem(IngredientsBasketData ingredientsBasketData) {
        listBasketIngredient.add(ingredientsBasketData);
    }
    public void removeItem(int position){
        listBasketIngredient.remove(position);
        notifyItemRemoved(position);
    }
}
