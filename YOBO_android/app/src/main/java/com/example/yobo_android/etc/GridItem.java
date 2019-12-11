package com.example.yobo_android.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yobo_android.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class GridItem extends LinearLayout {

    CircleImageView ingredientImage;
    TextView ingredientName;

    public GridItem(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredients_drag,this);
        ingredientImage = findViewById(R.id.ingredient_drag_img);
        ingredientName = findViewById(R.id.ingredient_drag_text);
    }

    public void setData(IngredientData data){
        ingredientImage.setImageResource(data.getIngreImagePath());
        ingredientImage.setTag(data.getIngreImagePath());
        ingredientName.setText(data.getIngreName());
    }

    public IngredientData getData(){
        return new IngredientData(ingredientName.getText().toString(), (Integer) ingredientImage.getTag());
    }

}