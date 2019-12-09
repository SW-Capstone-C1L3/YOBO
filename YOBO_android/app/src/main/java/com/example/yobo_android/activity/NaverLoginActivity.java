package com.example.yobo_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.yobo_android.R;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.UserData;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NaverLoginActivity  extends AppCompatActivity {

    private static final String TAG = "NaverLoginActivity";
    private static String UserEmail;
    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "PliqQslrTlh8T2BTKNRb";
    private static String OAUTH_CLIENT_SECRET = "w6FO8vNLH6";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    public static OAuthLogin mOAuthLoginInstance;
    public static Context mContext;
    private ArrayList<String> tmp = new ArrayList<>();
    /**
     * UI 요소들
     */
//    private TextView mApiResultText;
//    private static TextView mOauthAT;
//    private static TextView mOauthRT;
//    private static TextView mOauthExpires;
//    private static TextView mOauthTokenType;
//    private static TextView mOAuthState;
//    private static TextView userDataText;
    View View;
    private OAuthLoginButton mOAuthLoginButton;
//    Button  buttonOAuth;
//    Button buttonRefresh;
//    Button buttonOAuthLogout;
//    Button getUserData;
    Thread thread = null;
    Handler handler = null;
    UserData userdata = new UserData();
    String mOauthAT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_login);
        mContext = this;
        thread = null;
        handler = null;
        initData();
        initView();
    }

    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
//        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

    protected void initView() {
//        mApiResultText = (TextView) findViewById(R.id.api_result_text);
        View =this.findViewById(android.R.id.content);
//        buttonOAuth=View.findViewById(R.id.buttonOAuth);
//        buttonRefresh=View.findViewById(R.id.buttonRefresh);
//        buttonOAuthLogout=View.findViewById(R.id.buttonOAuthLogout);
//        getUserData=View.findViewById(R.id.getUserdata);
//        Button.OnClickListener onClickListener = new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.buttonOAuth: {
//                        mOAuthLoginInstance.startOauthLoginActivity(NaverLoginActivity.this, mOAuthLoginHandler);
//                        break;
//                    }
//                    case R.id.buttonRefresh: {
//                        new RefreshTokenTask().execute();
//                        break;
//                    }
//                    case R.id.buttonOAuthLogout: {
//                        mOAuthLoginInstance.logout(mContext);
//                        updateView();
//                        break;
//                    }
//                    case R.id.getUserdata:{
//                        getUserdata(mOauthAT.getText().toString());
//                        Log.i("Logrd",userdata.toString());
//                        updateView();
//                        break;
//                    }
//                    default:
//                        break;
//                }
//            }
//        };
//        mOauthAT = (TextView) findViewById(R.id.oauth_access_token);
//        mOauthRT = (TextView) findViewById(R.id.oauth_refresh_token);
//        mOauthExpires = (TextView) findViewById(R.id.oauth_expires);
//        mOauthTokenType = (TextView) findViewById(R.id.oauth_type);
//        mOAuthState = (TextView) findViewById(R.id.oauth_state);
//        userDataText=(TextView)findViewById(R.id.api_result_text);
        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
//        buttonOAuth.setOnClickListener(onClickListener);
//        buttonRefresh.setOnClickListener(onClickListener);
//        buttonOAuthLogout.setOnClickListener(onClickListener);
//        getUserData.setOnClickListener(onClickListener);
//        updateView();
    }

    public void getUserdata(String at){
//        if(mOAuthState.getText().equals("NEED_LOGIN")){
//            아무런 처리해주지 않음
//        }
//        else {
//            this.setTitle("OAuthLoginSample Ver." + OAuthLogin.getVersion());
            Call<UserData> call = RetrofitClient.getInstance().getApiService().getUserData(at);
            if (call != null) {
                call.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        Toast.makeText(NaverLoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Log.i("TEST", call.toString());
                        Log.i("TEST", response.toString());
                        userdata = response.body();
                        Log.i("kkkkk naverLogin pnum",userdata.getUser_phone_num());
                        Intent intent = new Intent();
                        intent.putExtra("user_id", userdata.get_id());
                        intent.putExtra("user_email", userdata.getUser_id());
                        intent.putExtra("user_name", userdata.getUser_name());
                        intent.putExtra("user_phone",userdata.getUser_phone_num());
                        for(int i=0;i<userdata.getInterest_category().size();i++)
                            tmp.add(userdata.getInterest_category().get(i));
                        intent.putStringArrayListExtra("interest_category",tmp);
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
//        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Activity가 종료되기 전에 저장한다.
        //SharedPreferences를 sFile이름, 기본모드로 설정
        SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        mOAuthLoginInstance.logout(mContext);
        editor.clear();
        editor.commit();
    }
    private void updateView() {
        mOauthAT = mOAuthLoginInstance.getAccessToken(mContext);
//        mOauthAT.setText(mOAuthLoginInstance.getAccessToken(mContext));
//        mOauthRT.setText(mOAuthLoginInstance.getRefreshToken(mContext));
//        mOauthExpires.setText(String.valueOf(mOAuthLoginInstance.getExpiresAt(mContext)));
//        mOauthTokenType.setText(mOAuthLoginInstance.getTokenType(mContext));
//        mOAuthState.setText(mOAuthLoginInstance.getState(mContext).toString());
//        userDataText.setText(userdata.getUser_email());
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                mOauthAT = accessToken;
//                mOauthAT.setText(accessToken);
//                mOauthRT.setText(refreshToken);
//                mOauthExpires.setText(String.valueOf(expiresAt));
//                mOauthTokenType.setText(tokenType);
//                mOAuthState.setText(mOAuthLoginInstance.getState(mContext).toString());
                getUserdata(mOauthAT);
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
