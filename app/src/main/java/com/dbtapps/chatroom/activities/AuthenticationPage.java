package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.dbtapps.chatroom.databinding.ActivityAuthenticationPageBinding;

public class AuthenticationPage extends AppCompatActivity {

    private ActivityAuthenticationPageBinding binding;
    private final int LOGIN_FLAG = 0;
    private final int REGISTER_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginButtonListener();
        registerButtonListener();
    }

    private void loginButtonListener() {

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(binding.appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(binding.loginBtn, "sendOTPBtnTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AuthenticationPage.this, pairs);
                Intent intent = new Intent(AuthenticationPage.this, OTPSendPage.class);
                intent.putExtra("LoginRegisterFlag", LOGIN_FLAG);
                startActivity(intent, options.toBundle());
            }
        });

    }

    private void registerButtonListener() {

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(binding.appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(binding.registerBtn, "sendOTPBtnTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AuthenticationPage.this, pairs);
                Intent intent = new Intent(AuthenticationPage.this, OTPSendPage.class);
                intent.putExtra("LoginRegisterFlag", REGISTER_FLAG);
                startActivity(intent, options.toBundle());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}