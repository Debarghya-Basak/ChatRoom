package com.dbtapps.chatroom.models;

import com.google.firebase.Timestamp;

public class MessageModel {
    public String message;
    public String sender_id;
    public String receiver_id;
    public Timestamp timestamp;

    public MessageModel(String message, String sender_id, String receiver_id, Timestamp timestamp) {
        this.message = message;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.timestamp = timestamp;
    }
}
