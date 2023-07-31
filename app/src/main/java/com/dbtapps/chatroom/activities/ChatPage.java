package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityChatPageBinding;
import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.models.DataLoaderModel;
import com.dbtapps.chatroom.models.TEMPSendMessageModel;
import com.dbtapps.chatroom.models.UserModel;
import com.dbtapps.chatroom.utilities.BitmapManipulator;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatPage extends AppCompatActivity {

    private ActivityChatPageBinding binding;
    private UserModel userData;
    private String chatDocumentId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        userData = (UserModel) args.getSerializable("UserModel");

        initializeChatPage();
        backBtnListener();
        sendBtnListener();

    }

    private void initializeChatPage() {
        binding.userProfilePicCiv.setImageBitmap(BitmapManipulator.stringToBitMap(userData.profile_picture));
        binding.userNameTv.setText(userData.name);

        ArrayList<String> chatPair = new ArrayList<>();
        chatPair.add(Constants.getKeyUserid());
        chatPair.add(userData.user_id);
        chatPair.sort((ob1, ob2) -> ob1.compareTo(ob2));

        Log.d("Debug", "ChatPair : " + chatPair.toString());

        Constants.db.collection(Constants.DB_CHATS)
                .whereEqualTo(Constants.DB_USER_IDS, chatPair)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(DocumentSnapshot d : queryDocumentSnapshots)
                        chatDocumentId = d.getId();

                    if(chatDocumentId != null){
                        Log.d("Debug", "chat exists : " + chatDocumentId);
                        //TODO: LOAD ALL CHATS OF THE PARTICULAR USER PAIR
                    }
                    else
                        Log.d("Debug", "No chat exists : " + chatDocumentId);

                });
    }

    private void sendBtnListener(){
        binding.sendBtn.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(binding.messageEt.getText())){
                Constants.db.collection(Constants.DB_CHATS)
                        .document(chatDocumentId)
                        .collection(Constants.DB_MESSAGES)
                        .document()
                        .set(new TEMPSendMessageModel(binding.messageEt.getText().toString(), Constants.getKeyUserid(), userData.user_id, Timestamp.now()));
            }
        });
    }

    private void backBtnListener() {
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}