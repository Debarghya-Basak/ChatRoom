package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.authentication.FirebaseAuthentication;
import com.dbtapps.chatroom.databinding.ActivityOtpsendPageBinding;
import com.dbtapps.chatroom.utilities.MakeToast;

public class OTPSendPage extends AppCompatActivity {

    private ActivityOtpsendPageBinding binding;
    Activity activity;
    private int LOGIN_REGISTER_FLAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpsendPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        LOGIN_REGISTER_FLAG = intent.getIntExtra("LoginRegisterFlag",-1);

        activity = this;

        binding.sendOTPBtn.animate().alpha(1).setDuration(500).start();

        sendOTPBtnListener();

    }

    private void sendOTPBtnListener() {
        binding.sendOTPBtn.setOnClickListener(v -> {
            String phoneNumber = binding.phoneEt.getText().toString();
            if(TextUtils.isEmpty(phoneNumber))
                MakeToast.makeToast(this, "Please enter a phone number");
            else if(phoneNumber.charAt(0) != '+')
                MakeToast.makeToast(this, "Please enter country code");
            else{
                FirebaseAuthentication.sendOTP(activity, phoneNumber, binding.appName, binding.sendOTPBtn, LOGIN_REGISTER_FLAG);
            }
        });
    }

    @Override
    public void onBackPressed() {
        binding.sendOTPBtn.animate().alpha(0).setDuration(500).start();
        super.onBackPressed();
    }
}