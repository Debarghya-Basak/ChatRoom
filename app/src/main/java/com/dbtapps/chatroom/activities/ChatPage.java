package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityChatPageBinding;
import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.models.DataLoaderModel;
import com.dbtapps.chatroom.utilities.BitmapManipulator;

import java.util.ArrayList;

public class ChatPage extends AppCompatActivity {

    private ActivityChatPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        DataLoaderModel userData = (DataLoaderModel) args.getSerializable("DataLoaderModel");

        Constants.db.collection(Constants.DB_USERS)
                .document(userData.chatUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    binding.userProfilePicCiv.setImageBitmap(BitmapManipulator.stringToBitMap(documentSnapshot.get(Constants.DB_PROFILE_PICTURE).toString()));
                    binding.userNameTv.setText(documentSnapshot.get(Constants.DB_NAME).toString());
                });


    }
}