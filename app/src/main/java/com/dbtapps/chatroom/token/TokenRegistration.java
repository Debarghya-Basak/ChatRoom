package com.dbtapps.chatroom.token;

import android.util.Log;
import androidx.annotation.NonNull;

import com.dbtapps.chatroom.utilities.MakeToast;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class TokenRegistration extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("Debug", token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        MakeToast.makeToast(this, remoteMessage.getFrom());
        Log.d("Debug", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            MakeToast.makeToast(this, remoteMessage.getNotification().getBody());
            Log.d("Debug", "Message data payload: " + remoteMessage.getNotification().getBody());
        }
    }

}
