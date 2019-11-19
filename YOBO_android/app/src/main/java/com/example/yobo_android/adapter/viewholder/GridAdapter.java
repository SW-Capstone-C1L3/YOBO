package com.example.yobo_android.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> ingreName;

    public GridAdapter(Context context, ArrayList<String> ingreName){
        this.context = context;
        this.ingreName = ingreName;
    }

    @Override
    public int getCount() {
        return ingreName.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        Button btnIngre = new Button(context);
        btnIngre.setLayoutParams(new GridView.LayoutParams(300,300));
        btnIngre.setText(ingreName.get(position));

        return btnIngre;
    }
}
