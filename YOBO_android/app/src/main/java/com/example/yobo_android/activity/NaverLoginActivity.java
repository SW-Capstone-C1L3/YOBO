package com.example.yobo_android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private ArrayList<String> tmp;
    View View;
    private OAuthLoginButton mOAuthLoginButton;
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
    }

    protected void initView() {
        View =this.findViewById(android.R.id.content);
        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    }

    public void getUserdata(String at){
            Call<UserData> call = RetrofitClient.getInstance().getApiService().getUserData(at);
            if (call != null) {
                call.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        Toast.makeText(NaverLoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        userdata = response.body();
                        Intent intent = new Intent();
                        intent.putExtra("user_id", userdata.get_id());
                        intent.putExtra("user_email", userdata.getUser_id());
                        intent.putExtra("user_name", userdata.getUser_name());
                        intent.putExtra("user_phone",userdata.getUser_phone_num());
                        tmp = new ArrayList<>();
                        for(int i=0;i<userdata.getInterest_category().size();i++){
                            if(!userdata.getInterest_category().get(i).equals("미선택"))
                                tmp.add(userdata.getInterest_category().get(i));
                            else
                                tmp.add("");
                        }
                        intent.putStringArrayListExtra("interest_category",tmp);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Toast.makeText(NaverLoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
//        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        mOAuthLoginInstance.logout(mContext);
    }
    private void updateView() {
        mOauthAT = mOAuthLoginInstance.getAccessToken(mContext);
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                mOauthAT = accessToken;
                getUserdata(mOauthAT);
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }

    };
}
