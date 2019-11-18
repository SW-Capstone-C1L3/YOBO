package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yobo_android.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    GridView grid;
    GridView grid2;

    ArrayList<String> name = new ArrayList<String>(Arrays.asList("고기","꿀","간장"));
    ArrayList<String> name2 = new ArrayList<String>(Arrays.asList("감"));

    GridAdapter adapter = new GridAdapter(this, name);
    GridAdapter adapter2 = new GridAdapter(this, name2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        grid = findViewById(R.id.grid);
        grid.setOnDragListener(new MyDragListener());
        grid.setAdapter(adapter);


        grid2 = findViewById(R.id.grid2);
        grid2.setOnDragListener(new MyDragListener());
        grid2.setAdapter(adapter2);
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
            Button btnIngre = new Button(context);
            btnIngre.setLayoutParams(new GridView.LayoutParams(300,300));
            btnIngre.setText(ingreName.get(position));
            btnIngre.setOnTouchListener(new MyTouchListener());

            return btnIngre;
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                view.startDrag(data, new View.DragShadowBuilder(view), view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
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
                    ViewGroup owner = (ViewGroup) view.getParent(); // drop된 layout
//                    owner.removeView(view);
//                    LinearLayout container = (LinearLayout) v;
//                    container.addView(view);
                    if(owner.getId() == grid.getId()){
                        name.remove(((Button)view).getText().toString());
                        name2.add(((Button)view).getText().toString());
                    }
                    else if(owner.getId() == grid2.getId()){
                        name2.remove(((Button)view).getText().toString());
                        name.add(((Button)view).getText().toString());
                    }
                    adapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();

                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }
}