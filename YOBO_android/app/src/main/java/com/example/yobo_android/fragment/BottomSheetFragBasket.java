package com.example.yobo_android.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.example.yobo_android.R;
import com.example.yobo_android.activity.BasketActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
                //Toast.makeText(getActivity(), "주문이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                if(checkDestination())
                    ((BasketActivity)getActivity()).buy(Integer.parseInt(price),etDestination.getText().toString());
            }
        });
        etDestination = view.findViewById(R.id.editDestination);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    String state = "";

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            state = "DRAGGING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            state = "SETTLING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            state = "EXPANDED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            state = "COLLAPSED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            dismiss();
                            state = "HIDDEN";
                            break;
                        }
                    }
                    //Toast.makeText(getContext(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
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
