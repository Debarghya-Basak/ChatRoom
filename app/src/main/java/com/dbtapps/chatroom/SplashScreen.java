package com.dbtapps.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appName = findViewById(R.id.app_name);

        screenTransition();

    }

    private void screenTransition() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Pair pairs[] = new Pair[1];
                pairs[0] = new Pair<View, String>(appName, "appNameTransition");
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