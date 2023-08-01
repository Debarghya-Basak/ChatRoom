package com.dbtapps.chatroom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityChatPageBinding;
import com.dbtapps.chatroom.models.ChatUserPairModel;
import com.dbtapps.chatroom.models.MessageModel;
import com.dbtapps.chatroom.models.UserModel;
import com.dbtapps.chatroom.utilities.BitmapManipulator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatPage extends AppCompatActivity {

    private ActivityChatPageBinding binding;
    private UserModel userData;
    private String chatDocumentId = null;
    private boolean chatExistsFlag = false;

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
                        chatExistsFlag = true;
                        Log.d("Debug", "chat exists : " + chatDocumentId);
                        //TODO: LOAD ALL CHATS OF THE PARTICULAR USER PAIR
                    }
                    else {
                        chatExistsFlag = false;
                        Log.d("Debug", "No chat exists : " + chatDocumentId);
                    }

                });
    }

    private void sendBtnListener(){
        binding.sendBtn.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(binding.messageEt.getText())){

                ArrayList<String> user_ids = new ArrayList<>();
                user_ids.add(Constants.getKeyUserid());
                user_ids.add(userData.user_id);
                user_ids.sort((ob1, ob2) -> ob1.compareTo(ob2));

                Timestamp tempTimeStamp = Timestamp.now();
                ChatUserPairModel userPair = new ChatUserPairModel(user_ids
                        , binding.messageEt.getText().toString()
                        , Constants.getKeyUserid()
                        , userData.user_id
                        , Constants.getKeyToken()
                        , tempTimeStamp);

                if(chatExistsFlag) {

                    Constants.db.collection(Constants.DB_CHATS)
                            .document(chatDocumentId).set(userPair);

                    Constants.db.collection(Constants.DB_CHATS)
                            .document(chatDocumentId)
                            .collection(Constants.DB_MESSAGES)
                            .document()
                            .set(new MessageModel(binding.messageEt.getText().toString(), Constants.getKeyUserid(), userData.user_id, Timestamp.now()));
                }
                else{

                    Constants.db.collection(Constants.DB_CHATS)
                            .document().set(userPair)
                            .addOnSuccessListener(unused -> {
                                Constants.db.collection(Constants.DB_CHATS)
                                        .whereEqualTo(Constants.DB_USER_IDS, user_ids)
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE, binding.messageEt.getText().toString())
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE_SENDER_ID, Constants.getKeyUserid())
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE_RECEIVER_ID, userData.user_id)
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE_USER_TOKEN, Constants.getKeyToken())
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE_TIMESTAMP, tempTimeStamp)
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            for(DocumentSnapshot d : queryDocumentSnapshots){
                                                chatDocumentId = d.getId();
                                            }
                                            Constants.db.collection(Constants.DB_CHATS)
                                                    .document(chatDocumentId)
                                                    .collection(Constants.DB_MESSAGES)
                                                    .document()
                                                    .set(new MessageModel(binding.messageEt.getText().toString(), Constants.getKeyUserid(), userData.user_id, Timestamp.now()));
                                        });
                            });
                }
            }
        });
    }

    private void backBtnListener() {
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}