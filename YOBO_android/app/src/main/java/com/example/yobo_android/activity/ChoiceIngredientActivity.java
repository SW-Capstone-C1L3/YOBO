package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yobo_android.R;

/*
* 재료를 선택해서 레시피를 검색하는 Activity
* 재료선택완료시 BoardActivity로 이동
*/

public class ChoiceIngredientActivity extends AppCompatActivity {
    Button btnSource;

    Button btnTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_ingredient);

        btnSource = (Button)findViewById(R.id.source);

        //버턴의 롱클릭 리스너 등록
        btnSource.setOnLongClickListener(new View.OnLongClickListener() {

            @Override

            public boolean onLongClick(View v) {

                // TODO Auto-generated method stub
                //롱클릭시 클립데이터를 만듬
                ClipData clip = ClipData.newPlainText("dragtext", "dragtext");

                //드래그할 데이터, 섀도우 지정, 드래그 앤 드롭 관련 데이터를 가지는 객체 지정, 0
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);

                return false;

            }

        });

        Button btnSource2 = (Button)findViewById(R.id.source2);

        btnSource2.setOnLongClickListener(new View.OnLongClickListener() {



            @Override

            public boolean onLongClick(View v) {

                // TODO Auto-generated method stub
                ClipData clip = ClipData.newRawUri("uri",
                        Uri.parse("content://com.example.ch20_contentprovider/word/boy"));

                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);

                return false;
            }

        });

        btnTarget = (Button)findViewById(R.id.target);

        btnTarget.setOnDragListener(mDragListener);

    }

    View.OnDragListener mDragListener = new View.OnDragListener() {



        @Override

        public boolean onDrag(View v, DragEvent event) {

            // TODO Auto-generated method stub

            Button btn;

            //드래그 객체가 버튼인지 확인
            if(v instanceof Button){

                btn = (Button)v;

            }else{

                return false;

            }
            //이벤트를 받음
            switch(event.getAction()){

                //드래그가 시작되면

                case DragEvent.ACTION_DRAG_STARTED:

                    //클립 설명이 텍스트면

                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){

                        btn.setText("Drop OK");//버튼의 글자를 바꿈

                        return true;

                    }else{//인텐트의 경우 이쪽으로 와서 드래그를 받을 수가 없다.

                        return false;

                    }

                    //드래그가 뷰의 경계안으로 들어오면

                case DragEvent.ACTION_DRAG_ENTERED:

                    btn.setText("Enter");//버튼 글자 바꿈

                    return true;

                //드래그가 뷰의 경계밖을 나가면

                case DragEvent.ACTION_DRAG_EXITED:

                    btn.setText("Exit");//버튼 글자 바꿈

                    return true;

                //드래그가 드롭되면

                case DragEvent.ACTION_DROP:

                    //클립데이터의 값을 가져옴

                    String text = event.getClipData().getItemAt(0).getText().toString();

                    btn.setText(text);

                    return true;

                //드래그 성공 취소 여부에 상관없이 모든뷰에게

                case DragEvent.ACTION_DRAG_ENDED:

                    if(event.getResult()){//드래그 성공시

                        Toast.makeText(getApplicationContext(), "Drag & Drop 완료", Toast.LENGTH_SHORT).show();

                    }else{//드래그 실패시

                        btn.setText("Target");

                    }

                    return true;

            }

            return true;

        }

    };


}
