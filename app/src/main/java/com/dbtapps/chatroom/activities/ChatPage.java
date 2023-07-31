package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityChatPageBinding;
import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.models.DataLoaderModel;
import com.dbtapps.chatroom.models.UserModel;
import com.dbtapps.chatroom.utilities.BitmapManipulator;
import com.google.firebase.firestore.auth.User;

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
        UserModel userData = (UserModel) args.getSerializable("UserModel");

        binding.userProfilePicCiv.setImageBitmap(BitmapManipulator.stringToBitMap(userData.profile_picture));
        binding.userNameTv.setText(userData.name);
    }
}