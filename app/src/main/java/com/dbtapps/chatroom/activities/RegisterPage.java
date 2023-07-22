package com.dbtapps.chatroom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.authentication.FirebaseAuthentication;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityRegisterPageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    private ActivityRegisterPageBinding binding;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        registerButtonListener();
    }

    private void registerButtonListener() {
        binding.registerBtn.setOnClickListener(v -> {
            //TODO: Add checks for edit text
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", binding.nameEt.getText().toString());
            userData.put("password", binding.passwordEt.getText().toString());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(Constants.getUSERID().getUid())
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Debug", "User Data added");
                    })
                    .addOnFailureListener(exception -> {
                        Log.d("Debug", "User Data could not be added");
                    });


        });
    }
}