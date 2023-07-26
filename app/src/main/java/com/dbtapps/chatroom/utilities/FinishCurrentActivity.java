package com.dbtapps.chatroom.utilities;

import android.app.Activity;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class FinishCurrentActivity extends AppCompatActivity {

    public static void finish(Activity activity){
        new Handler().postDelayed(() -> {
            activity.finish();
        },400);
    }
}
