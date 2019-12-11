package com.example.yobo_android.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.yobo_android.R;
import com.example.yobo_android.activity.BasketActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class BottomSheetFragBasket extends BottomSheetDialogFragment {

    private TextView totPrice;
    private Button mbtmBuy;
    private String price;
    private EditText etDestination;

    public BottomSheetFragBasket(){

    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet_basket, null);
        dialog.setContentView(view);
        price = ((BasketActivity)getActivity()).giveSum();
        totPrice = view.findViewById(R.id.basket_total_price);
        totPrice.setText(price);
        mbtmBuy = view.findViewById(R.id.btnBasketPurchase);
        mbtmBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkDestination())
                    ((BasketActivity)getActivity()).buy(Integer.parseInt(price),etDestination.getText().toString());
            }
        });
        etDestination = view.findViewById(R.id.editDestination);
    }

    public boolean checkDestination(){
        if(etDestination.getText().toString().equals("")) {
            checkInput();
            return false;
        }
        else{
            return true;
        }
    }
    public void checkInput() {
        String snackBarMessage = null;
        if (snackBarMessage==null) {
            Snackbar make = Snackbar.make(getDialog().getWindow().getDecorView(),
                    "배송지를 입력해주세요", Snackbar.LENGTH_LONG);
            make.setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            make.setActionTextColor(Color.RED);
            make.show();
        }
    }
}
