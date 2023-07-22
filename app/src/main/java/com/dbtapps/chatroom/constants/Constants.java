package com.dbtapps.chatroom.constants;

import com.google.firebase.auth.FirebaseUser;

public class Constants {

    private static FirebaseUser USERID;
    private static String KEY_PASSWORD;
    private static String KEY_NAME;
    private static String KEY_PROFILE_PICTURE;

    public static String getKeyName() {
        return KEY_NAME;
    }

    public static void setKeyName(String keyName) {
        KEY_NAME = keyName;
    }

    public static String getKeyProfilePicture() {
        return KEY_PROFILE_PICTURE;
    }

    public static void setKeyProfilePicture(String keyProfilePicture) {
        KEY_PROFILE_PICTURE = keyProfilePicture;
    }

    public static FirebaseUser getUSERID() {
        return USERID;
    }

    public static void setUSERID(FirebaseUser USER_ID) {
        USERID = USER_ID;
    }

    public static String getKeyPassword() {
        return KEY_PASSWORD;
    }

    public static void setKeyPassword(String keyPassword) {
        KEY_PASSWORD = keyPassword;
    }



}
