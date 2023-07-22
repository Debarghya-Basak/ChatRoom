package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityLoginPageBinding;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginPage extends AppCompatActivity {

    private ActivityLoginPageBinding binding;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        loginBtnListener();
    }

    private void loginBtnListener() {
        binding.loginBtn.setOnClickListener(v -> {
            //TODO: Add checks for Edit Text
            if(binding.passwordEt.getText().toString().equals(Constants.getKeyPassword())){
                MakeToast.makeToast(getApplicationContext(), "LOGIN successfull");
            }
            else{
                MakeToast.makeToast(getApplicationContext(), "Incorrect password entered");
            }
        });
    }

}