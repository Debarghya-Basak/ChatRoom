package com.dbtapps.chatroom.constants;

import com.google.firebase.auth.FirebaseUser;

public class Constants {

    private static FirebaseUser USERID;
    private static String KEY_PASSWORD;

    public static FirebaseUser getUSERID() {
        return USERID;
    }

    public static void setUSERID(FirebaseUser USERID) {
        Constants.USERID = USERID;
    }

    public static String getKeyPassword() {
        return KEY_PASSWORD;
    }

    public static void setKeyPassword(String keyPassword) {
        KEY_PASSWORD = keyPassword;
    }



}
