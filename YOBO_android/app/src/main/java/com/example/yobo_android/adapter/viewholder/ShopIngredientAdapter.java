package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yobo_android.R;
import com.example.yobo_android.activity.ShowSelectedIngredientInfoActivity;
import com.example.yobo_android.etc.ShoppingIngredientData;
import com.squareup.picasso.Picasso;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ShopIngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static final String ImageURL = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=";
    private static final int TYPE_ITEM = 1;
    private ArrayList<ShoppingIngredientData> listShopIngredient = new ArrayList<>();
    private Context context;

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String sel_ingredient_Id;
        private ImageView product_image;
        private TextView product_name;
        private TextView product_price;
        private TextView mCompanyName;

        private String product_unit;
        private String product_description;
        private String company_name;
        private String company_id;

        ItemViewHolder(View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.ShopIngredientImage);
            product_name = itemView.findViewById(R.id.ShopIngredientName);
            product_price = itemView.findViewById(R.id.ShopIngredientPrice);
            mCompanyName = itemView.findViewById(R.id.companyName);
        }

        void onBind(ShoppingIngredientData shoppingIngredientData, int position) {
            String temp = shoppingIngredientData.getProduct_image();
            temp = temp.replace("/", "%2F");
            String sum = ImageURL + temp;
            try {
                URL url = new URL(sum);
                Picasso.get().load(url.toString()).into(product_image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            company_id = shoppingIngredientData.getProvided_company_id();
            sel_ingredient_Id = shoppingIngredientData.get_id();
            product_unit = shoppingIngredientData.getProduct_unit();
            product_description = shoppingIngredientData.getProduct_description();
            company_name = shoppingIngredientData.getCompany_name();
            product_name.setText(shoppingIngredientData.getProduct_name());
            product_price.setText(shoppingIngredientData.getProduct_price() + "원/"+shoppingIngredientData.getProduct_qty()+product_unit);
            mCompanyName.setText("판매자 : " + shoppingIngredientData.getCompany_name());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition() ;
            if (pos != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, ShowSelectedIngredientInfoActivity.class);
                intent.putExtra("Ingredient_id",sel_ingredient_Id);
                context.startActivity(intent);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_ingredient, parent, false);
        holder = new ShopIngredientAdapter.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShopIngredientAdapter.ItemViewHolder itemViewHolder = (ShopIngredientAdapter.ItemViewHolder) holder;
        itemViewHolder.onBind(listShopIngredient.get(position), position);
    }

    @Override
    public int getItemViewType(int position){
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listShopIngredient.size();
    }

    public void addItem(ShoppingIngredientData shoppingIngredientData, int position) {
        listShopIngredient.add(shoppingIngredientData);
        notifyItemChanged(position);
    }
}
