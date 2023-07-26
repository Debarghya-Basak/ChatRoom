package com.dbtapps.chatroom.models;

public class ChatModel {

    public String chatId;
    public String userName;
    public String lastText;
    public String userProfilePicture;

    public ChatModel(String chatId, String userName, String lastText, String userProfilePicture) {
        this.chatId = chatId;
        this.userName = userName;
        this.lastText = lastText;
        this.userProfilePicture = userProfilePicture;
    }



}
