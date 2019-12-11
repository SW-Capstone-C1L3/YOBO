package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.ShowSelectedIngredientInfoActivity;
import com.example.yobo_android.etc.ProductData;
import java.util.ArrayList;
import java.util.List;

public class SelectedShopLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 1;
    private List<ProductData> productDataList = new ArrayList<>();
    private Context context;
    private List<String> statusList = new ArrayList<>();
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private String product_id;
        private String company_id;
        private TextView product_qty;
        private TextView product_name;
        private TextView tot_price;
        private TextView transition_status;


        ItemViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.item_name);
            product_qty = itemView.findViewById(R.id.item_qty);
            tot_price = itemView.findViewById(R.id.item_total_price);
            transition_status = itemView.findViewById(R.id.transition_status);
        }

        void onBind(ProductData productData, int position) {
            product_qty.setText("개수: " +String.valueOf(productData.getProduct_qty()));
            product_name.setText(productData.getProduct_name());
            tot_price.setText(String.valueOf("가격: "+ productData.getPrice()*productData.getProduct_qty()+"원"));
            transition_status.setText("배송 진행 상태 :" + statusList.get(position));
            product_id = productData.getProduct_id();
            company_id = productData.getCompany_id();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition() ;
            if (pos != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, ShowSelectedIngredientInfoActivity.class);
                intent.putExtra("Ingredient_id",product_id);
                intent.putExtra("fromLog","true");
                context.startActivity(intent);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_shop_log, parent, false);
        holder = new SelectedShopLogAdapter.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SelectedShopLogAdapter.ItemViewHolder itemViewHolder = (SelectedShopLogAdapter.ItemViewHolder) holder;
        itemViewHolder.onBind(productDataList.get(position), position);
    }

    @Override
    public int getItemViewType(int position){ return TYPE_ITEM; }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    public void addItem(ProductData productData, int position,String status) {
        productDataList.add(productData);
        statusList.add(status);
        notifyItemChanged(position);
    }
}
