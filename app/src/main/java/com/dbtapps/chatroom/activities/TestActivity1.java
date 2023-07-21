package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;

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

        Dialog d = new Dialog(TestActivity1.this);

        binding.showDialog.setOnClickListener(v -> {
            d.setContentView(R.layout.dialog_test1);
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 100);
            d.setCancelable(true);
            //d.getWindow().getAttributes().windowAnimations = R.style.animation;

            if(c == 0){
                d.show();
                c++;
            }
            else {
                d.dismiss();
                c--;
            }

        });


    }
}