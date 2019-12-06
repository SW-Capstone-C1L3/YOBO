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
import com.example.yobo_android.activity.ShowDetailedShopLogActivity;
import com.example.yobo_android.etc.ProductData;
import com.example.yobo_android.etc.ShopLogData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ShopLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 1;
    private List<ShopLogData> listLog = new ArrayList<>();
    private List<ProductData> productData = new ArrayList<>();
    private Context context;

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private String _id;
        private String company_id;
        private String invoice_company;
        private String invoice_number;
        private Integer total_price;
        private String transaction_status;
        private String user_Did;
        private String user_address;
        private String user_id;
        private String user_phone_num;
        private String YYMMDD;
        private TextView title;
        private TextView ts;
        private TextView buyDate;

        ItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.BuyLog);
            ts = itemView.findViewById(R.id.tot_price);
            buyDate = itemView.findViewById(R.id.buyDate);
        }

        void onBind(ShopLogData shopLogData, int position) {
            _id = shopLogData.get_id();
            company_id = shopLogData.getCompany_id();
            invoice_company = shopLogData.getInvoice_company();
            invoice_number = shopLogData.getInvoice_number();
            total_price = shopLogData.getTotal_price();
            transaction_status = shopLogData.getTransaction_status();
            user_Did = shopLogData.getUser_Did();
            user_address = shopLogData.getUser_address();
            user_id = shopLogData.getUser_id();
            user_phone_num = shopLogData.getUser_phone_num();
            productData = shopLogData.getProducts();
            //제목에 관한 setting도 해줘야함
            if(shopLogData.getProducts().size()==1)
                title.setText(shopLogData.getProducts().get(0).getProduct_name());
            else
                title.setText(shopLogData.getProducts().get(0).getProduct_name() + " 외 "+String.valueOf(shopLogData.getProducts().size()-1)+"종");
            ts.setText("구매금액: "+ total_price+"원");
            YYMMDD = shopLogData.getTimestamp();
            String[] splitText = YYMMDD.split("T");
            for(int i=0;i<splitText.length;i++) {
                if(i==0)
                    YYMMDD=splitText[i];
            }
            buyDate.setText("구매날짜: "+YYMMDD);
            itemView.setOnClickListener(this);
            Log.i("TEST222",_id);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition() ;
            if (pos != RecyclerView.NO_POSITION) { // 갱신하는 과정에서 뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 NO_POSITION 리턴
                Intent intent = new Intent(context, ShowDetailedShopLogActivity.class);
                intent.putExtra("log_id",_id);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_log, parent, false);
        holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i("TEST333",position+"");
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Log.i("TEST113","onBind");
        itemViewHolder.onBind(listLog.get(position), position); // if add header, listRecipe.get(position - 1)
    }

    @Override
    public int getItemViewType(int position){ return TYPE_ITEM; }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listLog.size();
    }

    public void addItem(ShopLogData shopLogData, int position) {
        // 외부에서 item을 추가시킬 함수입니다.
        listLog.add(shopLogData);
        notifyItemChanged(position);
    }
}
