package com.dbtapps.chatroom.models;

public class DataLoaderModel {

    public String chatDocumentId = null;
    public String chatUserId = null;
    public Long chatPositionInList = null;

    public DataLoaderModel(String chatDocumentId, String chatUserId, Long chatPositionInList) {
        this.chatDocumentId = chatDocumentId;
        this.chatUserId = chatUserId;
        this.chatPositionInList = chatPositionInList;
    }

    public DataLoaderModel(String chatUserId){
        this.chatUserId = chatUserId;
    }
}
