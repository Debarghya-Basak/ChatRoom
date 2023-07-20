package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        screenTransition();

    }

    private void screenTransition() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Pair pairs[] = new Pair[1];
                pairs[0] = new Pair<View, String>(binding.appName, "appNameTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                Intent intent = new Intent(SplashScreen.this, AuthenticationPage.class);
                startActivity(intent,options.toBundle());
            }
        },1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2500);

    }
}