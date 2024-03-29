package com.example.yobo_android.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeActivity;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.Recipe;
import com.squareup.picasso.Picasso;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
/*
 * RecipeActivity에 띄워지는 fragment
 * 레시피 요리 순서가 띄워짐
 */
public class RecipeOrderFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener{
    private String recipeId;
    private int position;

    Recipe recipe;
    ImageView mCurImage;
    TextView mCurDescription;

    private Button btn;
    private Button sst;
    private Button btnStart;
    private Button btnStop;
    private TextToSpeech tts;
    private static final int MY_DATA_CHECK =1234;
    Thread thread = null;
    Handler handler = null;
    private int flag = 0;

    // newInstance constructor for creating fragment with arguments
    public static RecipeOrderFragment newInstance(String recipeId, int position) {
        RecipeOrderFragment fragment = new RecipeOrderFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("recipeId", recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            recipeId = getArguments().getString("recipeId");
            position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_order, container, false);
        mCurImage = view.findViewById(R.id.curimage);
        mCurDescription = view.findViewById(R.id.curdescription);
        btnStop = view.findViewById(R.id.btnStop);
        mCurDescription.setHint(position+"");
        Call<Recipe> call = null;
        call = RetrofitClient.getInstance().getApiService().getReicpebyDid(recipeId);
        if(call != null) {
            call.enqueue(new Callback<Recipe>() {
                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    recipe = response.body();

                    String temp = recipe.getCooking_description().get(position).getImage();
                    temp = temp.replace("/", "%2F");
                    String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                    try {
                        URL url = new URL(sum);
                        Picasso.get().load(url.toString()).into(mCurImage);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    mCurDescription.setText(recipe.getCooking_description().get(position).getDescription());
                    ((RecipeActivity)getActivity()).addDescription(mCurDescription.getText().toString());
                }
                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {

                }
            });
        }
        btnStart = view.findViewById(R.id.btnStart);
        btn =(Button)view.findViewById(R.id.btnOnOff);
        sst=view.findViewById(R.id.btnSST);
        sst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((RecipeActivity)getActivity()).start();
                RecipeActivity.startTTS = true;
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                RecipeActivity.startTTS = true;
                Speech();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                RecipeActivity.startTTS = false;
            }
        });
        tts = new TextToSpeech(getActivity(), this);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String text = mCurDescription.getText().toString().trim();
                tts.setPitch((float) 1.0);      // 음량
                tts.setSpeechRate((float) 1.5); // 재생속도
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"ababa");
            }
        });
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent,MY_DATA_CHECK);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        if(isVisibleToUser && RecipeActivity.startTTS){
            flag = 1;
            handler = new Handler(){
                public void handleMessage(android.os.Message msg) {
                    if(flag==1) {
                        Speech();
                        flag=0;
                    }
                }
            };
            thread = new Thread(){
                public void run() {
                    super.run();
                    while(true){
                        try {
                            Thread.sleep(1000);
                            handler.sendEmptyMessage(0);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    // 글자 읽어주기
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Speech() {
        if(RecipeActivity.startTTS) {
            String text = mCurDescription.getText().toString().trim();
            tts.setPitch((float) 1.0);      // 음량
            tts.setSpeechRate((float) 1.5); // 재생속도
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ababa");
            while (tts.isSpeaking()) ;
            ((RecipeActivity) getActivity()).start();
        }
    }

    public void Speech(String str){
        String text = str;
        tts.setPitch((float) 1.0);      // 음량
        tts.setSpeechRate((float) 1.5); // 재생속도
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"ababa");
        while(tts.isSpeaking());
        ((RecipeActivity)getActivity()).start();
    }

    @Override
    public void onInit(int status) {
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
        switch (view.getId()) {
            case R.id.btnOnOff:
                Speech();
                break;
            default:
                break;
        }
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
