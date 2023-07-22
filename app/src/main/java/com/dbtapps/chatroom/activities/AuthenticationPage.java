package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.dbtapps.chatroom.databinding.ActivityAuthenticationPageBinding;
import com.dbtapps.chatroom.token.TokenRegistration;
import com.dbtapps.chatroom.utilities.PermissionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

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
        PermissionManager.permissionManager(this);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                String token = s;
                Log.d("Debug", "Token : " + token);
            }
        });
    }

    private void loginButtonListener() {

        binding.loginBtn.setOnClickListener(v -> {
                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(binding.appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(binding.loginBtn, "sendOTPBtnTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AuthenticationPage.this, pairs);
                Intent intent = new Intent(AuthenticationPage.this, OTPSendPage.class);
                intent.putExtra("LoginRegisterFlag", LOGIN_FLAG);
                startActivity(intent, options.toBundle());
        });

    }

    private void registerButtonListener() {

        binding.registerBtn.setOnClickListener(v -> {
                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(binding.appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(binding.registerBtn, "sendOTPBtnTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AuthenticationPage.this, pairs);
                Intent intent = new Intent(AuthenticationPage.this, OTPSendPage.class);
                intent.putExtra("LoginRegisterFlag", REGISTER_FLAG);
                startActivity(intent, options.toBundle());
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}