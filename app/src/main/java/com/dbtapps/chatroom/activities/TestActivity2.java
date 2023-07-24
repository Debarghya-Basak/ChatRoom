package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.sip.SipProfile;
import android.os.Bundle;
import com.dbtapps.chatroom.R;

import java.text.ParseException;

public class TestActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        try {
            setupSIPProfile();
        }catch (Exception e){}
    }

    private void setupSIPProfile() throws ParseException {
        SipProfile sipProfile = new SipProfile.Builder("anonymous","localhost:1000")
                .setPassword("anonymous").build();
    }

}