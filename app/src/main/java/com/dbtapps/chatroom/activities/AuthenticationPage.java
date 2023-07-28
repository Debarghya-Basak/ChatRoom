package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityAuthenticationPageBinding;
import com.dbtapps.chatroom.token.TokenRegistration;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.dbtapps.chatroom.utilities.PermissionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.List;

public class AuthenticationPage extends AppCompatActivity {

    private ActivityAuthenticationPageBinding binding;
    private final int LOGIN_FLAG = 0;
    private final int REGISTER_FLAG = 1;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;

        loginButtonListener();
        registerButtonListener();
        PermissionManager.permissionManager(this);
    }

    private void loginButtonListener() {

        binding.loginBtn.setOnClickListener(v -> {

            Log.d("Debug", "Not logged in");
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

}