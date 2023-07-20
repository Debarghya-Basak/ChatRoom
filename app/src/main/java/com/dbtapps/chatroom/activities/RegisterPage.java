package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.authentication.FirebaseAuthentication;
import com.dbtapps.chatroom.databinding.ActivityRegisterPageBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterPage extends AppCompatActivity {

    private ActivityRegisterPageBinding binding;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        registerButtonListener();
    }

    private void registerButtonListener() {
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FirebaseAuthentication.sendOTP(context, "+918240787009", appName, registerButton);
//
////                Pair pairs[] = new Pair[2];
////                pairs[0] = new Pair<View,String>(appName, "appNameTransition");
////                pairs[1] = new Pair<View,String>(registerButton, "verifyButtonTransition");
////                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterPage.this, pairs);
////                Intent intent = new Intent(RegisterPage.this, OTPVerificationPage.class);
////                startActivity(intent, options.toBundle());
//            }
//        });
    }
}