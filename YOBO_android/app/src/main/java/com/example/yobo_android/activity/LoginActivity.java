package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yobo_android.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signupButton;
    private CheckBox rememberLogin;
    private Button skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        rememberLogin = (CheckBox) findViewById(R.id.rememberLogin);
        skipButton = (Button) findViewById(R.id.skipButton);

        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                switch (v.getId()){
                    case R.id.loginButton :
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email",emailInput.getText());
                        break;
                    case R.id.skipButton :
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };
        loginButton.setOnClickListener(onClickListener);
        skipButton.setOnClickListener(onClickListener);
    }


}
