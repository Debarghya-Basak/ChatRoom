package com.dbtapps.chatroom.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.adapters.UserListRecyclerViewAdapter;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityUserListBinding;
import com.dbtapps.chatroom.models.ContactModel;
import com.dbtapps.chatroom.models.DataLoaderModel;
import com.dbtapps.chatroom.models.UserModel;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.dbtapps.chatroom.utilities.PermissionManager;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;

public class UserListPage extends AppCompatActivity {
    ActivityUserListBinding binding;

    ArrayList<ContactModel> contact = new ArrayList<>();
    Activity activity;

    private final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.actionBar);
        getSupportActionBar().setTitle("");

        Constants.KEY_USERS_FROM_CONTACTS = new ArrayList<>();
        activity = this;

        PermissionManager.permissionManager(this);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            getContactList();
            compareContactsWithFirebase();
            backBtnListener();
        }
        else
            MakeToast.makeToast(this, "Contact Permission not granted");


    }

    private void backBtnListener() {
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
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
                        contact.add(new ContactModel(name, number));

                        mobileNoSet.add(number);
                        Log.d("hvy", "name = " + name
                                + " , No = " + number);
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }

    private void compareContactsWithFirebase() {

        ArrayList<UserModel> firebaseUsers = new ArrayList<>();
        ArrayList<String> firebaseContacts = new ArrayList<>();

        Constants.db.collection(Constants.DB_USERS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot d : queryDocumentSnapshots.getDocuments()) {

                        if(d.get(Constants.DB_PHONE_NUMBER).toString().equals(Constants.getKeyPhone()))
                            continue;

                        firebaseUsers.add(new UserModel(d.get(Constants.DB_NAME).toString(),
                                d.get(Constants.DB_PASSWORD).toString(),
                                d.get(Constants.DB_PHONE_NUMBER).toString(),
                                d.get(Constants.DB_PROFILE_PICTURE).toString(),
                                d.getId()));
                        firebaseContacts.add(d.get(Constants.DB_PHONE_NUMBER).toString());

                        Log.d("Debug", "Phone Number = " + d.get(Constants.DB_PHONE_NUMBER).toString() + ", ID = " + d.getId());
                    }

                    for(int i=0;i<contact.size();i++){
                        int index = firebaseContacts.indexOf(contact.get(i).getPhoneNumber());
                        if(index >= 0) {
                            Constants.KEY_USERS_FROM_CONTACTS.add(firebaseUsers.get(index));
                        }
                    }

                    UserListRecyclerViewAdapter adapter = new UserListRecyclerViewAdapter(this, Constants.KEY_USERS_FROM_CONTACTS);
                    binding.userListRv.setAdapter(adapter);
                    binding.userListRv.setLayoutManager(new LinearLayoutManager(activity));
                })
                .addOnFailureListener(e -> {
                    Log.d("Debug", e.getLocalizedMessage());
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_userlistpage, menu);
        return super.onCreateOptionsMenu(menu);
    }
}