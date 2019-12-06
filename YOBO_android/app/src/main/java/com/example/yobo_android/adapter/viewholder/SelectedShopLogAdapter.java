package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
            if (pos != RecyclerView.NO_POSITION) { // 갱신하는 과정에서 뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 NO_POSITION 리턴
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
        Log.i("TEST333",position+"");
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        SelectedShopLogAdapter.ItemViewHolder itemViewHolder = (SelectedShopLogAdapter.ItemViewHolder) holder;
        Log.i("TEST113","onBind");
        itemViewHolder.onBind(productDataList.get(position), position); // if add header, listRecipe.get(position - 1)
    }

    @Override
    public int getItemViewType(int position){ return TYPE_ITEM; }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return productDataList.size();
    }

    public void addItem(ProductData productData, int position,String status) {
        // 외부에서 item을 추가시킬 함수입니다.
        productDataList.add(productData);
        statusList.add(status);
        notifyItemChanged(position);
    }
}
