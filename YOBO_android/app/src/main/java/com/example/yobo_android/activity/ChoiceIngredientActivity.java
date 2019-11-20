package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yobo_android.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

/*
* 재료를 선택해서 레시피를 검색하는 Activity
* 재료선택완료시 BoardActivity로 이동
*/

public class ChoiceIngredientActivity extends AppCompatActivity {
    GridView srcGrid;
    GridView destGrid;

    ArrayList<String> srcIngredient
            = new ArrayList<>(Arrays.asList("김","돼지고기","닭고기","소고기","계란",
            "고등어","갈치","오징어","낙지",
            "파","양파","당근","콩나물","무","감자","마늘","호박","고구마","양배추",
            "김치","두부","국수","스파게티면","당면","라면","치즈"));
    ArrayList<String> destIngredient = new ArrayList<>();

    GridAdapter srcAdapter = new GridAdapter(this, srcIngredient);
    GridAdapter destAdapter = new GridAdapter(this, destIngredient);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_ingredient);

        srcGrid = findViewById(R.id.srcIngredient);
        srcGrid.setOnDragListener(new MyDragListener());
        srcGrid.setAdapter(srcAdapter);

        destGrid = findViewById(R.id.destIngredient);
        destGrid.setOnDragListener(new MyDragListener());
        destGrid.setAdapter(destAdapter);

        findViewById(R.id.btnSearch).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(destIngredient.size() == 0){
                    Toast.makeText(getApplicationContext(),"재료를 한가지 이상 선택해요",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(getApplication(),BoardActivity.class);
                    String ingredients = "";
                    for(int i=0; i < destIngredient.size(); i++){
                        ingredients += ("ingredients=" + destIngredient.get(i) + "&");
                    }
                    ingredients = ingredients.substring(0,ingredients.length() - 1);
                    intent.putExtra("ingredients", ingredients);
                    startActivity(intent);
                }
            }
        });
    }

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

//            Button btnIngre = new Button(context);
//            btnIngre.setLayoutParams(new GridView.LayoutParams(250,150));
//            btnIngre.setText(ingreName.get(position));
//            btnIngre.setBackgroundResource(R.drawable.btn_rounded);
//            btnIngre.setOnLongClickListener(new MyTouchListener());

            LinearLayout btnIngre = new LinearLayout(ChoiceIngredientActivity.this);
            btnIngre.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(320,180);
            btnIngre.setLayoutParams(layoutParams);

            ImageView image = new ImageView(ChoiceIngredientActivity.this);
            image.setImageResource(R.drawable.heart);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(80,80);
            imageParams.setMargins(30,10,10,10);
            imageParams.gravity = Gravity.CENTER;
            image.setLayoutParams(imageParams);

            TextView name = new TextView(ChoiceIngredientActivity.this);
            name.setText(ingreName.get(position));
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(180,180);
            textParams.gravity = Gravity.CENTER;
            name.setLayoutParams(textParams);
            name.setGravity(Gravity.CENTER);

            btnIngre.addView(image,0);
            btnIngre.addView(name,1);
            btnIngre.setOnLongClickListener(new MyTouchListener());

            return btnIngre;
        }
    }

    private final class MyTouchListener implements View.OnLongClickListener {
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                ClipData data = ClipData.newPlainText("", "");
//                view.startDrag(data, new View.DragShadowBuilder(view), view, 0);
//                view.setVisibility(View.INVISIBLE);
//                return true;
//            } else {
//                return false;
//            }
//        }
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("", "");
            view.startDrag(data, new View.DragShadowBuilder(view), view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    break;

                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();

                    int preView = owner.getId();
                    int curView = v.getId();

                    if(preView != curView){
                        if(v.getId() == destGrid.getId()){
//                            srcIngredient.remove(((Button)view).getText().toString());
                            srcIngredient.remove(((TextView)((LinearLayout)view).getChildAt(1)).getText().toString());

                            destIngredient.add(((TextView)((LinearLayout)view).getChildAt(1)).getText().toString());
                        }
                        else if(v.getId() == srcGrid.getId()){
                            destIngredient.remove(((TextView)((LinearLayout)view).getChildAt(1)).getText().toString());
                            srcIngredient.add(((TextView)((LinearLayout)view).getChildAt(1)).getText().toString());
                        }
                    }

                    srcAdapter.notifyDataSetChanged();
                    destAdapter.notifyDataSetChanged();

                    view.setVisibility(View.VISIBLE);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    break;

                default:
                    break;
            }
            return true;
        }
    }

}
