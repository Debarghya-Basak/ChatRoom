package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.databinding.ActivityTest1Binding;

public class TestActivity1 extends AppCompatActivity {

    private ActivityTest1Binding binding;
    int c = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTest1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        showDialogBtnListener();
    }

    private void showDialogBtnListener(){
        binding.showDialog.setOnClickListener(v -> {
            if(c == 0){
                animateStart();
                c++;
            }
            else{
                animateStop();
                c--;
            }

        });
    }

    private void animateStart(){
        binding.loadingAnimation.animate().alpha(1).setDuration(1000).start();
    }

    private void animateStop(){
        binding.loadingAnimation.animate().alpha(0).setDuration(1000).start();
    }
}