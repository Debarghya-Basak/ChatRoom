package com.dbtapps.chatroom.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.adapters.UserListRecyclerViewAdapter;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityUserListBinding;
import com.dbtapps.chatroom.models.ContactModel;
import com.dbtapps.chatroom.models.DataLoaderModel;
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

        Constants.KEY_USERLIST_FROM_CONTACTS = new ArrayList<>();
        activity = this;

        getContactList();
        compareContactsWithFirebase();
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

        ArrayList<String> firebaseContacts = new ArrayList<>();
        ArrayList<DataLoaderModel> firebaseContactIds = new ArrayList<>();

        Constants.db.collection(Constants.DB_USERS)
                .orderBy("phone_number")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot d : queryDocumentSnapshots.getDocuments()) {
                        if(d.get("phone_number").toString().equals(Constants.getKeyPhone()))
                            continue;
                        firebaseContacts.add(d.get("phone_number").toString());
                        firebaseContactIds.add(new DataLoaderModel(d.getId()));
                        Log.d("Debug", "Phone Number = " + d.get("phone_number").toString() + ", ID = " + d.getId());
                    }

                    for(int i=0;i<contact.size();i++){
                        int index = firebaseContacts.indexOf(contact.get(i).getPhoneNumber());
                        if(index >= 0)
                            Constants.KEY_USERLIST_FROM_CONTACTS.add(firebaseContactIds.get(index));
                    }

                    UserListRecyclerViewAdapter adapter = new UserListRecyclerViewAdapter(this, Constants.KEY_USERLIST_FROM_CONTACTS);
                    binding.userListRv.setAdapter(adapter);
                    binding.userListRv.setLayoutManager(new LinearLayoutManager(activity));

                    for (DataLoaderModel dlm : Constants.KEY_USERLIST_FROM_CONTACTS)
                        Log.d("Debug", "USERLIST : " + dlm.chatUserId);


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