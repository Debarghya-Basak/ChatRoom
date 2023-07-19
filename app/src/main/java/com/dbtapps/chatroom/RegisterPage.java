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

public class RegisterPage extends AppCompatActivity {

    TextView appName;
    MaterialButton registerButton;
    TextInputEditText userName, phoneNumber, password, confirmPassword;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        appName = findViewById(R.id.app_name);
        registerButton = findViewById(R.id.register_button);
        userName = findViewById(R.id.name_editText);
        phoneNumber = findViewById(R.id.phone_editText);
        password = findViewById(R.id.password_editText);
        confirmPassword = findViewById(R.id.confirmPassword_editText);

        context = this;

        registerButtonListener();
    }

    private void registerButtonListener() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuthentication.sendOTP(context, "+918240787009", appName, registerButton);

//                Pair pairs[] = new Pair[2];
//                pairs[0] = new Pair<View,String>(appName, "appNameTransition");
//                pairs[1] = new Pair<View,String>(registerButton, "verifyButtonTransition");
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterPage.this, pairs);
//                Intent intent = new Intent(RegisterPage.this, OTPVerificationPage.class);
//                startActivity(intent, options.toBundle());
            }
        });
    }
}