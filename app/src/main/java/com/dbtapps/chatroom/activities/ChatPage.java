package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.dbtapps.chatroom.adapters.ChatMessageRecyclerViewAdapter;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityChatPageBinding;
import com.dbtapps.chatroom.models.ChatUserPairModel;
import com.dbtapps.chatroom.models.MessageModel;
import com.dbtapps.chatroom.models.UserModel;
import com.dbtapps.chatroom.utilities.BitmapManipulator;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ChatPage extends AppCompatActivity {

    private ActivityChatPageBinding binding;
    private UserModel userData;
    private String chatDocumentId = null;
    private boolean chatExistsFlag = false;
    private ArrayList<MessageModel> chatMessages;
    private ChatMessageRecyclerViewAdapter adapter;
    private boolean adapterCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        userData = (UserModel) args.getSerializable("UserModel");

        initializeChatAdapter();
        initializeChatPage();
        backBtnListener();
        sendBtnListener();
//        startChatMessageSnapshotListener();

    }

    private void initializeChatAdapter() {

        chatMessages = new ArrayList<>();
        adapter = new ChatMessageRecyclerViewAdapter(this, chatMessages);
        binding.messagesRv.setAdapter(adapter);
        binding.messagesRv.setLayoutManager(new LinearLayoutManager(this));
        adapterCounter = true;

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
                        startChatMessageSnapshotListener();
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
                String tempMessage = binding.messageEt.getText().toString();
                ChatUserPairModel userPair = new ChatUserPairModel(user_ids
                        , tempMessage
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
                            .set(new MessageModel(tempMessage, Constants.getKeyUserid(), userData.user_id, tempTimeStamp));

                    binding.messageEt.setText("");
                }
                else{
                    chatExistsFlag = true;

                    Constants.db.collection(Constants.DB_CHATS)
                            .document().set(userPair)
                            .addOnCompleteListener(unused -> {
                                Constants.db.collection(Constants.DB_CHATS)
                                        .whereEqualTo(Constants.DB_USER_IDS, user_ids)
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE, tempMessage)
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE_SENDER_ID, Constants.getKeyUserid())
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE_RECEIVER_ID, userData.user_id)
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE_USER_TOKEN, Constants.getKeyToken())
                                        .whereEqualTo(Constants.DB_LAST_MESSAGE_TIMESTAMP, tempTimeStamp)
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                                                chatDocumentId = d.getId();
                                                Log.d("Debug" , "New Chat document id : " + chatDocumentId);
                                            }

                                            Constants.db.collection(Constants.DB_CHATS)
                                                    .document(chatDocumentId)
                                                    .collection(Constants.DB_MESSAGES)
                                                    .document()
                                                    .set(new MessageModel(tempMessage, Constants.getKeyUserid(), userData.user_id, Timestamp.now()));

                                            startChatMessageSnapshotListener();
                                        });
                            });
                    binding.messageEt.setText("");
                }
            }
        });
    }

    private void startChatMessageSnapshotListener(){

        Constants.db.collection(Constants.DB_CHATS)
                .document(chatDocumentId)
                .collection(Constants.DB_MESSAGES)
                .addSnapshotListener((value, error) -> {

                    for(DocumentChange d : value.getDocumentChanges()) {
                        chatMessages.add(new MessageModel(d.getDocument().get("message").toString(), d.getDocument().get("sender_id").toString(), d.getDocument().get("receiver_id").toString(), (Timestamp) d.getDocument().get("timestamp")));
                    }

                    chatMessages.sort((ob1, ob2) -> ob1.timestamp.compareTo(ob2.timestamp));

                    if(chatMessages.isEmpty()){
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        adapter.notifyItemRangeChanged(chatMessages.size(), chatMessages.size());
                        Log.d("Debug", "Smooth scroll happened : " + chatMessages.size());
                        if(adapterCounter) {
                            adapterCounter = false;
                            new Handler().postDelayed(() -> {
                                binding.messagesRv.scrollToPosition(chatMessages.size() - 1);
                            }, 10);
                        }
                        else{
                            binding.messagesRv.smoothScrollToPosition(chatMessages.size() - 1);
                        }
                    }

                    //TODO: Remove temp debug lines
                    Log.d("Debug", "NEW --------------------------------------");
                    for(MessageModel model : chatMessages) {
                        Log.d("Debug" , "Incoming messages : " + model.message + ", TIMESTAMP : " + model.timestamp.toString());

                    }
                });
    }

    private void backBtnListener() {
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}