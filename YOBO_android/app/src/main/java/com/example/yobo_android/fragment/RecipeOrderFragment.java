package com.example.yobo_android.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView;

import com.example.yobo_android.R;
import com.example.yobo_android.activity.RecipeActivity;
import com.example.yobo_android.activity.RecipeMainActivity;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.etc.RecipeOrder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
/*
 * RecipeActivity에 띄워지는 fragment
 * 레시피 요리 순서가 띄워짐
 */
public class RecipeOrderFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener{
    private String recipeId;
    private int position;

    ApiService apiService;
    Retrofit retrofit;

    Recipe recipe;
    ImageView mCurImage;
    TextView mCurDescription;

    private Button btn;
    private Button speak;
    private TextToSpeech tts;
    private static final int MY_DATA_CHECK =1234;
    /////////이 밑으로 부터 추가
    private static final String TAG2 ="MyTag2";
    private static final String TAG ="MyTag";
    Thread thread = null;
    Handler handler = null;
    private  int flag = 0;

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
  
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_order, container, false);

        Log.i("ddd3","in view2");
        // Retrofit2 때문에 final로, 그래서 다른 함수에서 재활용이 안되네
        mCurImage = view.findViewById(R.id.curimage);
        mCurDescription = view.findViewById(R.id.curdescription);

        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build())
                .build();
        apiService = retrofit.create(ApiService.class);
        Call<Recipe> call = null;
        call = apiService.getReicpebyDid(recipeId);
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
                }
                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {
                    //Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    Log.i("ERROR", t.toString());
                    Log.i("ERROR", call.toString());
                }
            });
        }

        btn =(Button)view.findViewById(R.id.btnOnOff);
        speak=view.findViewById(R.id.btnSpeak);
        speak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((RecipeActivity)getActivity()).start();
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
        Log.i("ccccccccccc","recipeorder onCreateView");

        return view;
    }
    public void onViewCreated (View view,
                        Bundle savedInstanceState){
        Log.i("cccccccccc","RecipeOrderFrag onViewCreated called");
    }
    // Store instance variables based on arguments passed
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        if(isVisibleToUser){
            Log.i("ccccccccccc","현재 3frag가 보여짐");
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
        else{
            Log.i("ccccccccccc","현재 3frag가 보여지지 않음");
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    // 글자 읽어주기
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Speech() {
        Log.i("aaaaaaaa","Speech3");
        String text = mCurDescription.getText().toString().trim();
        tts.setPitch((float) 1.0);      // 음량
        tts.setSpeechRate((float) 1.5); // 재생속도
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"ababa");
        while(tts.isSpeaking());
        ((RecipeActivity)getActivity()).start();
    }

    @Override
    public void onInit(int status) {
        Log.i("cccccccccccc","onInit3");
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
            case R.id.btnOnOff:
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
