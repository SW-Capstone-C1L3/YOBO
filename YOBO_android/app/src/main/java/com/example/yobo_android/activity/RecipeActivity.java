package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yobo_android.R;

import com.example.yobo_android.fragment.RecipeOrderFragment;
import com.google.android.material.snackbar.Snackbar;

import me.relex.circleindicator.CircleIndicator;

import java.util.ArrayList;

/*
* 레시피 Activity
* 이후 RecipeOrderFragment로 조리법 나열
*/

public class RecipeActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;

    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private Button mLike;
    private Button mComments;
    private Button mAlert;
    private static ArrayList<String> description;

    private static final String TAG2 ="MyTag2";
    private static final String TAG ="MyTag";
    private int REQUEST_TEST=1000;
    final int PERMISSION = 1;
    String message;
    Intent intent;
    SpeechRecognizer mRecognizer;
    ViewPager vpPager;
    String result;
    int cnt=2;
    private int i=0;
    private int j=0;
    private static String recipeId;
    private static int recipeDescriptionNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeId = getIntent().getStringExtra("recipeId");
        recipeDescriptionNum = getIntent().getIntExtra("recipeDescriptionNum",recipeDescriptionNum) - 1;

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        mLike = (Button)findViewById(R.id.textLike);
        mComments = (Button)findViewById(R.id.textComments);
        mAlert = (Button)findViewById(R.id.textAlert);

        adapterViewPager  = new MyPagerAdapter(getSupportFragmentManager());
        adapterViewPager.notifyDataSetChanged();
        vpPager.setAdapter(adapterViewPager);

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);
        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        mComments.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(RecipeActivity.this,CommentActivity.class);
                intent.putExtra("recipe_id",recipeId);
                startActivityForResult(intent,REQUEST_TEST);
            }
        });
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        //        private static int NUM_ITEMS = 10;
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return recipeDescriptionNum;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return RecipeOrderFragment.newInstance(recipeId, position + 1);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TEST) {
            if (resultCode == RESULT_OK) {
                String snackBarMessage;
                snackBarMessage = "댓글이 존재하지 않습니다.";
                Snackbar make = Snackbar.make(getWindow().getDecorView().getRootView(),
                        snackBarMessage, Snackbar.LENGTH_LONG);
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

    public void selectIndex(int nexIdx){
        Log.i("cccccccccccccccc","selectItem 입장");
        vpPager.setCurrentItem(nexIdx,true);
    }

    public void start(){
        Log.i("cccccccccccccccc", "start");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(RecipeActivity.this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(intent);
        Log.i(TAG, "음성인식 시작");
    }
    ///********************************여기서부터 추가
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.i(TAG2,"onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            float f = rmsdB*100;
            if(f>900) {
                String str = Float.toString(rmsdB * 100);
                Log.i(TAG, str);
            }
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.i(TAG2,"onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.i(TAG2,"onBufferReceived");
        }

        @Override
        public void onError(int error) {
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            //Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            result="";
            for(int i = 0; i < matches.size() ; i++){
                //textView.setText(matches.get(i));
                result+=matches.get(i);
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            Log.i("aaaaaaaa",result);
            if(result.equals("다음")) {
                //Toast.makeText(getApplicationContext(), "다음을 입력받았습니다." , Toast.LENGTH_SHORT).show();
                mRecognizer.stopListening();
                selectIndex(++cnt);
            }
            else if(result.equals("다시")){
                adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
                vpPager.setAdapter(adapterViewPager);
                selectIndex(cnt);
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };

    public void checkInput(String str) {
        String snackBarMessage = null;
        if (snackBarMessage==null) {
            if(str.equals("like")) {
                if (i == 1)
                    snackBarMessage = "즐겨찾기가 등록되었습니다";
                else if (i == 0)
                    snackBarMessage = "즐겨찾기가 해제되었습니다";
            }
            else if(str.equals("alert")){
                if(j==1)
                    snackBarMessage = "신고가 접수되었습니다";
                else if(j==0)
                    snackBarMessage = "신고가 취소되었습니다";
            }
            Snackbar make = Snackbar.make(getWindow().getDecorView().getRootView(),
                    snackBarMessage, Snackbar.LENGTH_LONG);
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
