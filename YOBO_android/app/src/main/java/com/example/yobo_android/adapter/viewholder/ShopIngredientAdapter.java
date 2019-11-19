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

import java.util.ArrayList;

public class ShopIngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 1;
    // adapter에 들어갈 list 입니다.
    private ArrayList<ShoppingIngredientData> listShopIngredient = new ArrayList<>();
    private Context context;

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    // 즉 item view를 저장하는 뷰홀드 클래스
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String sel_ingredient_Id;
        private ImageView product_image;
        private TextView product_name;
        private TextView product_price;
        private String product_unit;
        private String product_description;
        private String company_name;

        ItemViewHolder(View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.ShopIngredientImage);
            product_name = itemView.findViewById(R.id.ShopIngredientName);
            product_price = itemView.findViewById(R.id.ShopIngredientPrice);
        }

        void onBind(ShoppingIngredientData shoppingIngredientData, int position) {
            //product_image.setImageResource(Integer.parseInt(shoppingIngredientData.getProduct_image()));
            sel_ingredient_Id = shoppingIngredientData.getSel_id();
            product_unit = shoppingIngredientData.getProduct_unit();
            product_description = shoppingIngredientData.getProduct_description();
            company_name = shoppingIngredientData.getCompany_name();
            product_name.setText(shoppingIngredientData.getProduct_name());
            product_price.setText(shoppingIngredientData.getProduct_price() + "원");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition() ;
            if (pos != RecyclerView.NO_POSITION) { // 갱신하는 과정에서 뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 NO_POSITION 리턴
                Intent intent = new Intent(context, ShowSelectedIngredientInfoActivity.class);
                //doc Id를 넘기고 recipeActivity에서 이걸로 레시피 정보를 서버에 요청
                intent.putExtra("Ingredient_id",sel_ingredient_Id);
                intent.putExtra("Ingredient_image",product_image.getResources().toString());
                intent.putExtra("Ingredient_name",product_name.getText().toString());
                intent.putExtra("Ingredient_description",product_description);
                intent.putExtra("Company_name",company_name);
                intent.putExtra("Ingredient_price",product_price.getText().toString());
                intent.putExtra("Ingredient_unit",product_unit);
                context.startActivity(intent);
            }
        }
    }

    // item view를 위한 viewHolder 객체 생성 및 return
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_ingredient, parent, false);
        holder = new ShopIngredientAdapter.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        ShopIngredientAdapter.ItemViewHolder itemViewHolder = (ShopIngredientAdapter.ItemViewHolder) holder;
        itemViewHolder.onBind(listShopIngredient.get(position), position); // if add header, listRecipe.get(position - 1)
    }

    @Override
    public int getItemViewType(int position){
        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listShopIngredient.size();
    }

    public void addItem(ShoppingIngredientData shoppingIngredientData) {
        // 외부에서 item을 추가시킬 함수입니다.
        listShopIngredient.add(shoppingIngredientData);
    }
}