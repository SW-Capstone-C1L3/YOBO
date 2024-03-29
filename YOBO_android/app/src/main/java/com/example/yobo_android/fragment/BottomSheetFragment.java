package com.example.yobo_android.fragment;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.ShowSelectedIngredientInfoActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    TextView mOnePrice;
    TextView mTotalPrice;
    Button mbtnMinus;
    Button mbtnQuantity;
    Button mbtnPlus;
    Button mbtnPurchase;
    public BottomSheetFragment(){

    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        String price = ((ShowSelectedIngredientInfoActivity)getActivity()).getIngredientPrice() + "";
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(view);
        mOnePrice = view.findViewById(R.id.one_price);
        mTotalPrice = view.findViewById(R.id.total_price);
        mbtnMinus = view.findViewById(R.id.btnMinus);
        mbtnQuantity = view.findViewById(R.id.btnIngredientQuantity);
        mbtnPlus = view.findViewById(R.id.btnPlus);
        mbtnPurchase = view.findViewById(R.id.btnPurchase);

        mOnePrice.setText(price);
        mTotalPrice.setText(price);
        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str = mbtnQuantity.getText().toString();
                String price = mOnePrice.getText().toString();
                Integer amount = Integer.parseInt(str);
                Integer one_price = Integer.parseInt(price);
                switch(v.getId()){
                    case R.id.btnMinus:
                        if(amount>0){
                            mbtnQuantity.setText(String.valueOf(--amount));
                            mTotalPrice.setText(String.valueOf(amount*one_price));
                        }
                        break;
                    case R.id.btnPlus:
                        mbtnQuantity.setText(String.valueOf(++amount));
                        mTotalPrice.setText(String.valueOf(amount*one_price));
                        break;
                    case R.id.btnPurchase:
                        if(amount>0) {
                            ((ShowSelectedIngredientInfoActivity) getActivity()).goToBasket(amount);
                        }
                        else
                            Toast.makeText(getContext(), "1개 이상 담아주시기 바랍니다.",Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
            }
        };
        mbtnMinus.setOnClickListener(onClickListener);
        mbtnPlus.setOnClickListener(onClickListener);
        mbtnPurchase.setOnClickListener(onClickListener);
    }
}