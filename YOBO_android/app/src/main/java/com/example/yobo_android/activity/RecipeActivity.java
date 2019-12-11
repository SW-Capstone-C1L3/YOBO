package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.view.View;
import android.widget.Toast;
import com.example.yobo_android.R;
import com.example.yobo_android.fragment.RecipeOrderFragment;
import com.google.android.material.snackbar.Snackbar;
import me.relex.circleindicator.CircleIndicator;
import java.util.ArrayList;
import java.util.List;

/*
* 레시피 Activity
* 이후 RecipeOrderFragment로 조리법 나열
*/

public class RecipeActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;
    private List<String> descriptionlist = new ArrayList<>();
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    private static final String TAG2 ="MyTag2";
    private static final String TAG ="MyTag";
    private int REQUEST_TEST=1000;
    final int PERMISSION = 1;
    String message;
    Intent intent;
    SpeechRecognizer mRecognizer;
    ViewPager vpPager;
    String result;
    private int i=0;
    private int j=0;
    private static String recipeId;
    public static int recipeDescriptionNum;
    public static boolean startTTS=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        startTTS=false;
        recipeId = getIntent().getStringExtra("recipeId");
        recipeDescriptionNum = getIntent().getIntExtra("recipeDescriptionNum",recipeDescriptionNum) - 1;

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager  = new MyPagerAdapter(getSupportFragmentManager());
        adapterViewPager.notifyDataSetChanged();
        vpPager.setAdapter(adapterViewPager);

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);
        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return recipeDescriptionNum;
        }

        @Override
        public Fragment getItem(int position) {
            return RecipeOrderFragment.newInstance(recipeId, position + 1);
        }

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

    public void selectIndex(){
        vpPager.setCurrentItem(vpPager.getCurrentItem() + 1,true);
    }

    public void start(){
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(RecipeActivity.this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(intent);
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech(){
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
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
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            result="";
            for(int i = 0; i < matches.size() ; i++)
                result+=matches.get(i);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            if(result.equals("다음")) {
                mRecognizer.stopListening();
                if(vpPager.getCurrentItem()+1!=recipeDescriptionNum)
                    selectIndex();
            }
            else if(result.equals("다시")){
                mRecognizer.stopListening();
                vpPager.setCurrentItem(vpPager.getCurrentItem(),true);
                RecipeOrderFragment tf = (RecipeOrderFragment) getSupportFragmentManager().findFragmentById(R.id.vpPager);
                tf.Speech(descriptionlist.get(vpPager.getCurrentItem()));
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };

    public void addDescription(String str){
        descriptionlist.add(str);
    }
}
