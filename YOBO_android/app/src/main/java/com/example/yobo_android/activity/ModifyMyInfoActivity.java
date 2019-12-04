package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.etc.UserData;

import java.util.ArrayList;
import java.util.List;

public class ModifyMyInfoActivity extends AppCompatActivity {

    ArrayList<String> userAddress;
    ArrayList<String> userInterestCategory;

    EditText mEditUserName;
    EditText mEditUserAddress1;
    EditText mEditUserAddress2;
    Spinner mUserFavorite1;
    Spinner mUserFavorite2;
    Spinner mUserFavorite3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_my_info);

        mEditUserName = findViewById(R.id.edit_userName);
        mEditUserAddress1 = findViewById(R.id.edit_userAddress1);
        mEditUserAddress2 = findViewById(R.id.edit_userAddress2);
        mUserFavorite1 = findViewById(R.id.userFavorite1);
        mUserFavorite2 = findViewById(R.id.userFavorite2);
        mUserFavorite3 = findViewById(R.id.userFavorite3);

        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<UserData> call = apiService.getbyDid(MainActivity.u_id);
        if (call != null) {
            call.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    UserData userData = response.body();
                    mEditUserName.setText(userData.getUser_name());
                    mEditUserAddress1.setText(userData.getUser_address().get(0));
                    mEditUserAddress2.setText(userData.getUser_address().get(1));
                    if(userData.getInterest_category().size() > 0)
                        mUserFavorite1.setSelection(getSpinnerIndex(mUserFavorite1, userData.getInterest_category().get(0)));
                    if(userData.getInterest_category().size() > 1)
                        mUserFavorite2.setSelection(getSpinnerIndex(mUserFavorite2, userData.getInterest_category().get(1)));
                    if(userData.getInterest_category().size() > 2)
                        mUserFavorite3.setSelection(getSpinnerIndex(mUserFavorite3, userData.getInterest_category().get(2)));

                }
                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"asd",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private int getSpinnerIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
                return index;
            }
        }
        return index;
    }
}
