package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityLoginPageBinding;
import com.dbtapps.chatroom.utilities.BitmapManipulator;
import com.dbtapps.chatroom.utilities.FinishCurrentActivity;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    private ActivityLoginPageBinding binding;
    Activity activity;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;
        db = FirebaseFirestore.getInstance();

        loginBtnListener();
        profilePicLoader();
        userNameLoader();
    }

    private void userNameLoader() {
        binding.userNameTv.setText(Constants.getKeyName());
    }

    private void profilePicLoader() {
        Bitmap bitmap = BitmapManipulator.stringToBitMap(Constants.getKeyProfilePicture());
        binding.profilePicCiv.setImageBitmap(bitmap);
    }

    private void loginBtnListener() {
        binding.loginBtn.setOnClickListener(v -> {
            //TODO: Add checks for Edit Text
            if(binding.passwordEt.getText().toString().equals(Constants.getKeyPassword())){
                if(binding.signInFlagCb.isChecked()){
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("name", Constants.getKeyName());
                    userData.put("password", Constants.getKeyPassword());
                    userData.put("phone_number", Constants.getKeyPhone());
                    userData.put("profile_picture", Constants.getKeyProfilePicture());
                    userData.put("token", Constants.getKeyToken());

                    db.collection("users")
                            .document(Constants.getUSERID().getUid())
                            .set(userData);

                    MakeToast.makeToast(getApplicationContext(), "LOGIN successfull. Token inserted");
                }
                else{
                    MakeToast.makeToast(getApplicationContext(), "LOGIN successfull. Token not inserted");
                }

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginPage.this);
                Intent intent = new Intent(LoginPage.this, HomePage.class);
                startActivity(intent, options.toBundle());
                FinishCurrentActivity.finish(activity);

            }
            else{
                MakeToast.makeToast(getApplicationContext(), "Incorrect password entered");
            }
        });
    }

}