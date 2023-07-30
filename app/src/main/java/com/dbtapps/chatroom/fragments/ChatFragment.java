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
        //TODO: Load Chat Data properly
        Log.d("Debug", "ChatFragment : " + Constants.getKeyUserid());

        ArrayList<DataLoaderModel> chatAndGroupLoader = new ArrayList<>();

        Constants.db.collection("chats")
                .whereArrayContains("user_ids", Constants.getKeyUserid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){

                        for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                            Log.d("Debug" , "ChatFragment : " + d.get("user_ids") + " , " + d.getId());

                            ArrayList<String> chatUserIds = (ArrayList<String>) d.get(Constants.DB_USER_IDS);
                            HashMap<String, Long> chatPositionInList = (HashMap<String, Long>) d.get(Constants.DB_CHAT_POSITION);

                            chatAndGroupLoader.add(new DataLoaderModel(d.getId(), getChatUserId(chatUserIds), chatPositionInList.get(Constants.getKeyUserid())));
                        }

                        chatAndGroupLoader.sort(((o1, o2) -> (int) (o1.chatPositionInList - o2.chatPositionInList)));

                        ChatFragmentRecyclerViewAdapter adapter = new ChatFragmentRecyclerViewAdapter(getContext(),chatAndGroupLoader);
                        chatRv.setAdapter(adapter);
                        chatRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                    else{
                        Log.d("Debug" , "ChatFragment : nothing");
                    }
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
            MakeToast.makeToast(getContext(), "Clicked");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
            Intent intent = new Intent(getActivity(), UserListPage.class);
            startActivity(intent, options.toBundle());
        });
    }
}