package com.dbtapps.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class RegisterPage extends AppCompatActivity {

    TextView appName;
    MaterialButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        appName = findViewById(R.id.app_name);
        registerButton = findViewById(R.id.register_button);

        registerButtonListener();
    }

    private void registerButtonListener() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(registerButton, "verifyButtonTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterPage.this, pairs);
                Intent intent = new Intent(RegisterPage.this, OTPVerificationPage.class);
                startActivity(intent, options.toBundle());
            }
        });
    }
}