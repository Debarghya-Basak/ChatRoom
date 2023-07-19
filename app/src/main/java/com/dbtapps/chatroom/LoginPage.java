package com.dbtapps.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.dbtapps.chatroom.java.FirebaseAuthentication;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginPage extends AppCompatActivity {

    TextView appName;
    MaterialButton loginButton;
    TextInputEditText phoneNumber, password;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        appName = findViewById(R.id.app_name);
        loginButton = findViewById(R.id.login_button);
        phoneNumber = findViewById(R.id.phone_editText);
        password = findViewById(R.id.password_editText);

        context = this;

        loginButton.animate().alpha(1).setDuration(500).start();

        loginButtonListener();

    }

    private void loginButtonListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                Pair pairs[] = new Pair[2];
//                pairs[0] = new Pair<View,String>(appName, "appNameTransition");
//                pairs[1] = new Pair<View,String>(loginButton, "verifyButtonTransition");
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginPage.this, pairs);
//                Intent intent = new Intent(LoginPage.this, OTPVerificationPage.class);
//                startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public void onBackPressed() {
        loginButton.animate().alpha(0).setDuration(500).start();
        super.onBackPressed();
    }
}