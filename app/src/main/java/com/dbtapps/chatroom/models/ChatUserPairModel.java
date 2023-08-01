package com.dbtapps.chatroom.models;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Map;

public class ChatUserPairModel {
//    public Map<String, Long> chat_position;
    public ArrayList<String> user_ids;
    public String last_message;
    public String last_message_sender_id;
    public String last_message_receiver_id;
    public String last_message_user_token;
    public Timestamp last_message_timestamp;

    public ChatUserPairModel(ArrayList<String> user_ids, String last_message, String last_message_sender_id, String last_message_receiver_id, String last_message_user_token, Timestamp last_message_timestamp) {
        this.user_ids = user_ids;
        this.last_message = last_message;
        this.last_message_sender_id = last_message_sender_id;
        this.last_message_receiver_id = last_message_receiver_id;
        this.last_message_user_token = last_message_user_token;
        this.last_message_timestamp = last_message_timestamp;
    }
}
