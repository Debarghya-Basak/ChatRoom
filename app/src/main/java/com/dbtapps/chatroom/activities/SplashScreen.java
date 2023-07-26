package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivitySplashScreenBinding;
import com.dbtapps.chatroom.utilities.FinishCurrentActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreen extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;
    private FirebaseFirestore db;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        activity = this;

        tokenManager();

    }

    private void tokenManager() {
        FirebaseMessaging.getInstance()
                .getToken()
                .addOnSuccessListener(s -> {
                    Constants.setKeyToken(s);
                    String token = s;
                    Log.d("Debug", "Token : " + token);
                    screenTransition();
                });
    }

    private void screenTransition() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                db.collection("users")
                        .whereEqualTo("token", Constants.getKeyToken())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if(queryDocumentSnapshots.isEmpty()) {
                                Log.d("Debug", "Not logged in");
                                Pair pairs[] = new Pair[1];
                                pairs[0] = new Pair<View, String>(binding.appName, "appNameTransition");
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                                Intent intent = new Intent(SplashScreen.this, AuthenticationPage.class);
                                startActivity(intent, options.toBundle());
                                FinishCurrentActivity.finish(activity);
                            }
                            else{
                                Log.d("Debug", "User is logged in");

                                for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                                    Log.d("Debug", "SplashScreen : " + d.getId());
                                    Constants.setKeyUserid(d.getId());
                                }
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this);
                                Intent intent = new Intent(SplashScreen.this, HomePage.class);
                                startActivity(intent, options.toBundle());
                                FinishCurrentActivity.finish(activity);
                            }
                        });
            }
        },1500);

    }

}