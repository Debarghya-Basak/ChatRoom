package com.dbtapps.chatroom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.models.ContactModel;
import com.dbtapps.chatroom.utilities.PermissionManager;

import java.util.ArrayList;
import java.util.HashSet;

public class TestActivity2 extends AppCompatActivity {

    ArrayList<ContactModel> contactModelList = new ArrayList<>();
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        PermissionManager.permissionManager(this);

        getContactList();

    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null) {
            HashSet<String> mobileNoSet = new HashSet<String>();
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name, number;
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    number = number.replace(" ", "");
                    if (!mobileNoSet.contains(number)) {
                        contactModelList.add(new ContactModel(name, number));
                        mobileNoSet.add(number);
                        Log.d("hvy", "onCreaterrView  Phone Number: name = " + name
                                + " No = " + number);
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }


}