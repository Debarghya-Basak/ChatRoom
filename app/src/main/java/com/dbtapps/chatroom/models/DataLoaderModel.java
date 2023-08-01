package com.dbtapps.chatroom.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class DataLoaderModel implements Serializable {

    public String chatDocumentId = null;
    public String chatUserId = null;
//    public Long chatPositionInList = null;

    public DataLoaderModel(String chatDocumentId, String chatUserId) {
        this.chatDocumentId = chatDocumentId;
        this.chatUserId = chatUserId;
//        this.chatPositionInList = chatPositionInList;
    }

    public DataLoaderModel(String chatUserId){
        this.chatUserId = chatUserId;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
