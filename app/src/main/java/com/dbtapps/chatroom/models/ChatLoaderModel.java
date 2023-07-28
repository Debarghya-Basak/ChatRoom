package com.dbtapps.chatroom.models;

public class ChatLoaderModel {

    public String chatDocumentId;
    public String chatUserId;
    public Long chatPositionInList;

    public ChatLoaderModel(String chatDocumentId, String chatUserId, Long chatPositionInList) {
        this.chatDocumentId = chatDocumentId;
        this.chatUserId = chatUserId;
        this.chatPositionInList = chatPositionInList;
    }
}
