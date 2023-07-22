package com.dbtapps.chatroom.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class PermissionManager extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_REQCODE = 100;
    private static final int READ_MEDIA_IMAGES_REQCODE = 101;

    public static void permissionManager(Activity activity){
        if(ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
            Log.d("Debug", "PERMISSION GRANTED");
        }
        else{
            MakeToast.makeToast(activity.getApplicationContext(), "Please accept the permissions");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, READ_MEDIA_IMAGES_REQCODE);
            else
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQCODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case READ_EXTERNAL_STORAGE_REQCODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    MakeToast.makeToast(this, "READ EXTERNAL STORAGE PERMISSION GRANTED");
                else
                    MakeToast.makeToast(this, "READ EXTERNAL STORAGE PERMISSION NOT GRANTED");
            case READ_MEDIA_IMAGES_REQCODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    MakeToast.makeToast(this, "READ MEDIA IMAGES PERMISSION GRANTED");
                else
                    MakeToast.makeToast(this, "READ MEDIA IMAGES PERMISSION NOT GRANTED");

        }
    }
}
