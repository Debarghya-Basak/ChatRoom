package com.dbtapps.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class AuthenticationPage extends AppCompatActivity {

    TextView appName;
    MaterialButton loginButton, registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_page);

        appName = findViewById(R.id.app_name);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        loginButtonListener();
        registerButtonListener();
    }

    private void loginButtonListener() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(loginButton, "loginButtonTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AuthenticationPage.this, pairs);
                Intent intent = new Intent(AuthenticationPage.this, LoginPage.class);
                startActivity(intent, options.toBundle());
            }
        });

    }

    private void registerButtonListener() {

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View,String>(appName, "appNameTransition");
                pairs[1] = new Pair<View,String>(registerButton, "registerButtonTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AuthenticationPage.this, pairs);
                Intent intent = new Intent(AuthenticationPage.this, RegisterPage.class);
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