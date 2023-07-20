package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.dbtapps.chatroom.authentication.FirebaseAuthentication;
import com.dbtapps.chatroom.databinding.ActivityOtpverificationPageBinding;

public class OTPVerificationPage extends AppCompatActivity {

    private ActivityOtpverificationPageBinding binding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpverificationPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;

        verifyBtnListener();

    }

    private void verifyBtnListener() {
        binding.otpVerifyBtn.setOnClickListener(v -> {
                FirebaseAuthentication.verifyOTP(activity, binding.otpVerifyEt.getText().toString());
        });
    }
}