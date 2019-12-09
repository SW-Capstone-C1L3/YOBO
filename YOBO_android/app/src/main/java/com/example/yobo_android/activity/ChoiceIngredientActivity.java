package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.etc.GridItem;
import com.example.yobo_android.etc.IngredientData;


import java.util.ArrayList;
import java.util.Arrays;

/*
* 재료를 선택해서 레시피를 검색하는 Activity
* 재료선택완료시 BoardActivity로 이동
*/

public class ChoiceIngredientActivity extends AppCompatActivity {
    GridView mSrcGrid;
    GridView mDestGrid;

    ArrayList<IngredientData> srcIngredient
            = new ArrayList<>(Arrays.asList(
            new IngredientData("김",R.drawable.ingre_seaweed),
            new IngredientData("돼지고기",R.drawable.ingre_pork),
            new IngredientData("닭고기",R.drawable.ingre_chicken),
            new IngredientData("소고기",R.drawable.ingre_beef),
            new IngredientData("계란",R.drawable.ingre_egg),
            new IngredientData("고등어",R.drawable.ingre_fish),
            new IngredientData("새우",R.drawable.ingre_shrimp),
            new IngredientData("낙지",R.drawable.ingre_octopus1), // 낙지
            new IngredientData("문어",R.drawable.ingre_octopus2), // 문어
            new IngredientData("두부",R.drawable.ingre_tofu),
            new IngredientData("당면",R.drawable.ingre_dangnoodle),
            new IngredientData("파스타면",R.drawable.ingre_pastanoodle),
            new IngredientData("라면",R.drawable.ingre_ranoodle),
            new IngredientData("김치",R.drawable.ingre_kimchi),
            new IngredientData("파",R.drawable.ingre_greenonion),
            new IngredientData("양파",R.drawable.ingre_onion),
            new IngredientData("콩",R.drawable.ingre_bean),
            new IngredientData("배추",R.drawable.ingre_cabbage),
            new IngredientData("치즈",R.drawable.ingre_cheese),
            new IngredientData("밀가루",R.drawable.ingre_flour),
            new IngredientData("양고기",R.drawable.ingre_lamb),
            new IngredientData("우유",R.drawable.ingre_milk),
            new IngredientData("파프리카",R.drawable.ingre_paprika),
            new IngredientData("감자",R.drawable.ingre_potato),
            new IngredientData("호박",R.drawable.ingre_pumpkin),
            new IngredientData("고추",R.drawable.ingre_red_pepper),
            new IngredientData("밥",R.drawable.ingre_rice),
            new IngredientData("소세지",R.drawable.ingre_sausage),
            new IngredientData("미역",R.drawable.ingre_seaweed2),
            new IngredientData("오징어",R.drawable.ingre_squid),
            new IngredientData("고구마",R.drawable.ingre_sweet_potato),
            new IngredientData("토마토",R.drawable.ingre_tomato)
            ));
    ArrayList<IngredientData> destIngredient = new ArrayList<>();

    GridAdapter srcAdapter = new GridAdapter(this, srcIngredient);
    GridAdapter destAdapter = new GridAdapter(this, destIngredient);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_ingredient);

        mSrcGrid = findViewById(R.id.srcIngredient);
        mSrcGrid.setOnDragListener(new MyDragListener());
        mSrcGrid.setAdapter(srcAdapter);

        mDestGrid = findViewById(R.id.destIngredient);
        mDestGrid.setOnDragListener(new MyDragListener());
        mDestGrid.setAdapter(destAdapter);

        findViewById(R.id.btnSearch).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(destIngredient.size() == 0){
                    Toast.makeText(getApplicationContext(),"재료를 한가지 이상 선택해요",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(getApplication(),BoardActivity.class);
                    ArrayList<String> ingredientNames = new ArrayList<>();
                    for (int i = 0; i < destIngredient.size(); i++) {
                        ingredientNames.add(destIngredient.get(i).getIngreName());
                    }
                    intent.putExtra("ingredients", ingredientNames);
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
                        if(v.getId() == mDestGrid.getId()){
                            srcIngredient.remove(selectedIngredient);
                            destIngredient.add(selectedIngredient);
                        }
                        else if(v.getId() == mSrcGrid.getId()){
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
