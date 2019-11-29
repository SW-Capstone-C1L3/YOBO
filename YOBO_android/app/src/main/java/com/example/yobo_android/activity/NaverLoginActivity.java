package com.example.yobo_android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yobo_android.R;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.UserData;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NaverLoginActivity  extends AppCompatActivity {

    private static final String TAG = "NaverLoginActivity";
    private static String UserEmail;
    public static boolean flag = false;
    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "PliqQslrTlh8T2BTKNRb";
    private static String OAUTH_CLIENT_SECRET = "w6FO8vNLH6";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    public static OAuthLogin mOAuthLoginInstance;
    public static Context mContext;

    /**
     * UI 요소들
     */
    private TextView mApiResultText;
    private static TextView mOauthAT;
    private static TextView mOauthRT;
    private static TextView mOauthExpires;
    private static TextView mOauthTokenType;
    private static TextView mOAuthState;
    private static TextView userDataText;
    View View;
    private OAuthLoginButton mOAuthLoginButton;
    Button  buttonOAuth;
    Button buttonRefresh;
    Button buttonOAuthLogout;
    Button getUserData;
    Thread thread = null;
    Handler handler = null;
    UserData userdata = new UserData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_login);
        mContext = this;
        initData();
        initView();

        handler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                if(flag) {
                    getUserdata(mOauthAT.getText().toString());
                    flag = false;
                }
            }
        };
        thread = new Thread(){
            //run은 jvm이 쓰레드를 채택하면, 해당 쓰레드의 run메서드를 수행한다.
            public void run() {
                super.run();
                while(true){
                    try {
                        Thread.sleep(500);
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

    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

    protected void initView() {
        mApiResultText = (TextView) findViewById(R.id.api_result_text);
        View =this.findViewById(android.R.id.content);
        buttonOAuth=View.findViewById(R.id.buttonOAuth);
        buttonRefresh=View.findViewById(R.id.buttonRefresh);
        buttonOAuthLogout=View.findViewById(R.id.buttonOAuthLogout);
        getUserData=View.findViewById(R.id.getUserdata);
        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonOAuth: {
                        mOAuthLoginInstance.startOauthLoginActivity(NaverLoginActivity.this, mOAuthLoginHandler);
                        break;
                    }
                    case R.id.buttonRefresh: {
                        new RefreshTokenTask().execute();
                        break;
                    }
                    case R.id.buttonOAuthLogout: {
                        mOAuthLoginInstance.logout(mContext);
                        updateView();
                        break;
                    }
                    case R.id.getUserdata:{
                        getUserdata(mOauthAT.getText().toString());
                        Log.i("Logrd",userdata.toString());
                        updateView();
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        mOauthAT = (TextView) findViewById(R.id.oauth_access_token);
        mOauthRT = (TextView) findViewById(R.id.oauth_refresh_token);
        mOauthExpires = (TextView) findViewById(R.id.oauth_expires);
        mOauthTokenType = (TextView) findViewById(R.id.oauth_type);
        mOAuthState = (TextView) findViewById(R.id.oauth_state);
        userDataText=(TextView)findViewById(R.id.api_result_text);
        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        buttonOAuth.setOnClickListener(onClickListener);
        buttonRefresh.setOnClickListener(onClickListener);
        buttonOAuthLogout.setOnClickListener(onClickListener);
        getUserData.setOnClickListener(onClickListener);
        updateView();
    }

    public void getUserdata(String at){
        Retrofit retrofit;
        ApiService apiService;
        if(mOAuthState.getText().equals("NEED_LOGIN")){
            //아무런 처리해주지 않음
        }
        else {
            this.setTitle("OAuthLoginSample Ver." + OAuthLogin.getVersion());
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
            Call<UserData> call = apiService.getUserData(at);
            if (call != null) {
                call.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        Toast.makeText(NaverLoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Log.i("TEST", call.toString());
                        Log.i("TEST", response.toString());
                        userdata = response.body();
                        Intent intent = new Intent();
                        intent.putExtra("user_id", userdata.get_id());
                        intent.putExtra("user_email", userdata.getUser_id());
                        intent.putExtra("user_name", userdata.getUser_name());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Toast.makeText(NaverLoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", call.toString());
                        Log.e("ERROR", t.toString());
                    }
                });
            }
        }
    }

    private void updateView() {
        mOauthAT.setText(mOAuthLoginInstance.getAccessToken(mContext));
        mOauthRT.setText(mOAuthLoginInstance.getRefreshToken(mContext));
        mOauthExpires.setText(String.valueOf(mOAuthLoginInstance.getExpiresAt(mContext)));
        mOauthTokenType.setText(mOAuthLoginInstance.getTokenType(mContext));
        mOAuthState.setText(mOAuthLoginInstance.getState(mContext).toString());
        userDataText.setText(userdata.getUser_email());
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                mOauthAT.setText(accessToken);
                mOauthRT.setText(refreshToken);
                mOauthExpires.setText(String.valueOf(expiresAt));
                mOauthTokenType.setText(tokenType);
                mOAuthState.setText(mOAuthLoginInstance.getState(mContext).toString());
                flag = true;
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }

    };

    private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return mOAuthLoginInstance.refreshAccessToken(mContext);
        }

        protected void onPostExecute(String res) {
            updateView();
        }
    }

}
