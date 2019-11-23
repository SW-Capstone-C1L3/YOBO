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
import com.example.yobo_android.etc.GridItem;
import com.example.yobo_android.etc.IngredientData;

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

    ArrayList<IngredientData> srcIngredient
            = new ArrayList<>(Arrays.asList(
            new IngredientData("김",R.drawable.burger),
            new IngredientData("돼지고기",R.drawable.cake),
            new IngredientData("닭고기",R.drawable.carrot),
            new IngredientData("소고기",R.drawable.fish),
            new IngredientData("계란",R.drawable.burger),
            new IngredientData("고추",R.drawable.cake),
            new IngredientData("시금치",R.drawable.burger),
            new IngredientData("피망",R.drawable.carrot)));

    ArrayList<IngredientData> destIngredient = new ArrayList<>();

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
                    intent.putExtra("ingredients", destIngredient);
                    startActivity(intent);
                }
            }
        });
    }

    public class GridAdapter extends BaseAdapter {
        Context context;
        ArrayList<IngredientData> ingreData;

        public GridAdapter(Context context, ArrayList<IngredientData> ingreData){
            this.context = context;
            this.ingreData = ingreData;
        }

        @Override
        public int getCount() {
            return ingreData.size();
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

            if(convertview == null)
                convertview = new GridItem(context);
            ((GridItem)convertview).setData(ingreData.get(position));
            convertview.setOnLongClickListener(new MyTouchListener());
            return convertview;
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

                    IngredientData selectedIngredient = ((GridItem)view).getData();
                    if(preView != curView){
                        if(v.getId() == destGrid.getId()){
                            srcIngredient.remove(selectedIngredient);
                            destIngredient.add(selectedIngredient);
                        }
                        else if(v.getId() == srcGrid.getId()){
                            destIngredient.remove(selectedIngredient);
                            srcIngredient.add(selectedIngredient);
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
