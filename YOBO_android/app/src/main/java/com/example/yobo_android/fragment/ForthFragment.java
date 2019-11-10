package com.example.yobo_android.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeActivity;
import com.example.yobo_android.etc.Recipe;

import java.util.Locale;

public class ForthFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener{
    // Store instance variables
    private String title;
    private int page;
    Button btn;
    private TextToSpeech tts;
    private static final int MY_DATA_CHECK =1234;
    /////////이 밑으로 부터 추가
    private static final String TAG2 ="MyTag2";
    private static final String TAG ="MyTag";
    TextView textView;
    EditText tvLabel;

    public ForthFragment(){

    }
    // newInstance constructor for creating fragment with arguments
    public static ForthFragment newInstance(int page, String title) {
        ForthFragment fragment = new ForthFragment();
        Log.i("cccccccccccc4",String.valueOf(page));
        return fragment;
    }
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_forth, container, false);
        tvLabel = (EditText) view.findViewById(R.id.editText4);
        btn =(Button)view.findViewById(R.id.btnOnOff2);
        tts = new TextToSpeech(getActivity(), this);
        btn.setOnClickListener(this);
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent,MY_DATA_CHECK);
        return view;
    }
    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        if(isVisibleToUser){
            Log.i("aaaaaaaaaaaaaa","현재 4frag가 보여짐");
            Speech();
        }
        else{
            Log.i("aaaaaaaaaaaaa","현재 4frag가 보여지지 않음");
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
    // 글자 읽어주기
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Speech() {
        Log.i("aaaaaaaa","Speech4");
        String text = tvLabel.getText().toString().trim();
        tts.setPitch((float) 1.0);      // 음량
        tts.setSpeechRate((float) 1.5); // 재생속도
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"ababa");
        while(tts.isSpeaking());
        ((RecipeActivity)getActivity()).start();
    }

    @Override
    public void onInit(int status) {
        Log.i("aaaaaaa","onInit4");
        if (status == TextToSpeech.SUCCESS) {
            // 작업 성공
            int language = tts.setLanguage(Locale.KOREAN);  // 언어 설정
            if (language == TextToSpeech.LANG_MISSING_DATA
                    || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어 데이터가 없거나, 지원하지 않는경우
                btn.setEnabled(false);
                Toast.makeText(getActivity(), "지원하지 않는 언어입니다.", Toast.LENGTH_SHORT).show();
            } else {
                // 준비 완료
                btn.setEnabled(true);
            }
        } else {
            // 작업 실패
            Toast.makeText(getActivity(), "TTS 작업에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View view) {
        Log.i("aaaaaaa","onClick");
        switch (view.getId()) {
            case R.id.btnOnOff2:
                Speech();
                break;
            default:
                break;
        }
    }
    @Override
    public void onDestroy() {
        Log.i("aaaaaaa","onDestroy");
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
