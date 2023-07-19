package com.dbtapps.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.dbtapps.chatroom.java.FirebaseAuthentication;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class OTPVerificationPage extends AppCompatActivity {

    TextInputEditText otpEditText;
    MaterialButton verifyButton;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification_page);

        otpEditText = findViewById(R.id.otpVerify_editText);
        verifyButton = findViewById(R.id.otpVerify_button);

        context = this;

        verifyButtonListener();

    }

    private void verifyButtonListener() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuthentication.verifyOTP(context, otpEditText.getText().toString());
            }
        });
    }
}