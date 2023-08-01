package com.dbtapps.chatroom.fragments;

import android.app.ActivityOptions;
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
import com.dbtapps.chatroom.activities.UserListPage;
import com.dbtapps.chatroom.adapters.ChatFragmentRecyclerViewAdapter;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.models.DataLoaderModel;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView chatRv;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //TODO: PERMANENT CODE OF RECYCLER VIEW UPDATE USING NOTIFY FUNCTIONS

//        ArrayList<DataLoaderModel> dataLoaderModel = new ArrayList<>();
//
//        ChatFragmentRecyclerViewAdapter adapter = new ChatFragmentRecyclerViewAdapter(getContext(),dataLoaderModel);
//        chatRv.setAdapter(adapter);
//        chatRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        Constants.db.collection("chats")
                .whereArrayContains("user_ids", Constants.getKeyUserid())
                .addSnapshotListener((value, error) -> {

                    //TODO: TEMPORARY CODE OF RECYCLER VIEW UPDATE
                    ArrayList<DataLoaderModel> dataLoaderModel = new ArrayList<>();
                    for(DocumentSnapshot d : value.getDocuments()){


                        ArrayList<String> chatUserIds = (ArrayList<String>) d.get(Constants.DB_USER_IDS);
                        dataLoaderModel.add(new DataLoaderModel(d.getId(), getChatUserId(chatUserIds)));


                    }
                    ChatFragmentRecyclerViewAdapter adapter = new ChatFragmentRecyclerViewAdapter(getContext(),dataLoaderModel);
                    chatRv.setAdapter(adapter);
                    chatRv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    //TODO: PERMANENT CODE OF RECYCLER VIEW UPDATE USING NOTIFY FUNCTIONS

//                    if(value.getDocuments().size() > dataLoaderModel.size()){
//
//                        for(DocumentChange d : value.getDocumentChanges()) {
//
//                            ArrayList<String> chatUserIds = (ArrayList<String>) d.getDocument().get(Constants.DB_USER_IDS);
//                            Log.d("MessageChange" , "Message added : " + getChatUserId(chatUserIds));
//
//                            dataLoaderModel.add(new DataLoaderModel(d.getDocument().getId(), getChatUserId(chatUserIds)));
//                            adapter.notifyItemInserted(dataLoaderModel.size());
//
//                        }
//
//
//                    }
//                    else if(value.getDocuments().size() < dataLoaderModel.size()){
//
//                        Log.d("MessageChange" , "Message deleted");
//                        for(DocumentChange d : value.getDocumentChanges()) {
//                            for (int i = 0; i < dataLoaderModel.size(); i++) {
//                                if (dataLoaderModel.get(i).chatDocumentId.equals(d.getDocument().getId())) {
//                                    dataLoaderModel.remove(i);
//                                    adapter.notifyItemRemoved(i);
//
//                                }
//                            }
//                        }
//
//
//                    }



                });
    }

    private String getChatUserId(ArrayList<String> chatUserIds) {
        if(chatUserIds.get(0).equals(Constants.getKeyUserid()))
            return chatUserIds.get(1);
        else
            return chatUserIds.get(0);
    }

    private void setFabListener() {
        fab.setOnClickListener(v -> {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
            Intent intent = new Intent(getActivity(), UserListPage.class);
//            startActivity(intent, options.toBundle());
            startActivity(intent);
        });
    }
}