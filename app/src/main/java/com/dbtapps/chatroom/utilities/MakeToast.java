package com.dbtapps.chatroom.utilities;

import android.content.Context;
import android.widget.Toast;

public class MakeToast {

    public static void makeToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void makeLongToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
