package com.dbtapps.chatroom.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.activities.UserList;
import com.dbtapps.chatroom.adapters.ChatFragmentRecyclerViewAdapter;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.models.ChatModel;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView chatRv;
    private View view;
    FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);

        fab = view.findViewById(R.id.floatingActionBarFab);
        chatRv = view.findViewById(R.id.chatContainerRv);

        setFabListener();
        loadChatData();

        return view;
    }

    private void loadChatData() {
        //TODO: Load Chat Data properly
        Log.d("Debug", "ChatFragment : " + Constants.getKeyUserid());
        db.collection("chats")
                .whereArrayContains("user_names", Constants.getKeyUserid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){

                        ArrayList<ChatModel> chatList = new ArrayList<>();
                        for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                            Log.d("Debug" , "ChatFragment : " + d.get("user_names") + " , " + d.getId());


                            chatList.add(new ChatModel(d.getId(), Constants.TEMP_OTHER_NAME, "Hello", Constants.TEMP_PROFILE_PIC));

                        }

                        chatList.add(new ChatModel(Constants.TEMP_OTHER_USER_ID, "Ma", "Hello", Constants.TEMP_PROFILE_PIC));
                        chatList.add(new ChatModel(Constants.TEMP_OTHER_USER_ID, "Debayan", "Hello", Constants.TEMP_PROFILE_PIC));
                        chatList.add(new ChatModel(Constants.TEMP_OTHER_USER_ID, "Baba", "Hello", Constants.TEMP_PROFILE_PIC));
                        chatList.add(new ChatModel(Constants.TEMP_OTHER_USER_ID, "Debarghya", "Hello", Constants.TEMP_PROFILE_PIC));

                        ChatFragmentRecyclerViewAdapter adapter = new ChatFragmentRecyclerViewAdapter(getContext(),chatList);
                        chatRv.setAdapter(adapter);
                        chatRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                    else{
                        Log.d("Debug" , "ChatFragment : nothing");
                    }
                });
    }

    private void setFabListener() {
        fab.setOnClickListener(v -> {
            MakeToast.makeToast(getContext(), "Clicked");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
            Intent intent = new Intent(getActivity(), UserList.class);
            startActivity(intent, options.toBundle());
        });
    }
}