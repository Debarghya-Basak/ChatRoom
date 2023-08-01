package com.dbtapps.chatroom.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.activities.ChatPage;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.models.DataLoaderModel;
import com.dbtapps.chatroom.models.ChatModel;
import com.dbtapps.chatroom.models.UserModel;
import com.dbtapps.chatroom.utilities.BitmapManipulator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ChatFragmentRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DataLoaderModel> dataLoaderModel;
    //ArrayList<ChatModel> chatList;

    public ChatFragmentRecyclerViewAdapter(Context context, ArrayList<DataLoaderModel> dataLoaderModel){
        this.context = context;
//        this.chatAndGroupLoader = chatAndGroupLoader;
        this.dataLoaderModel = dataLoaderModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.chat_layout_chatfragment, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.chatUserProfilePicCiv.setImageBitmap(BitmapManipulator.stringToBitMap(chatAndGroupLoader.get(position).userProfilePicture));
//        holder.chatUserNameTv.setText(chatAndGroupLoader.get(position).userName);
//        holder.chatUserLastMessageTv.setText("Temp");

        if(TextUtils.isEmpty(holder.chatUserNameTv.getText())){

            Constants.db.collection(Constants.DB_CHATS)
                    .document(dataLoaderModel.get(position).chatDocumentId)
                    .addSnapshotListener((value, error) -> {
                        try {
                            holder.chatUserLastMessageTv.setText(value.get(Constants.DB_LAST_MESSAGE).toString());
                        }
                        catch (Exception e){
                            Log.d("Debug", "Cannot get last message");
                        }
                    });

            Constants.db.collection(Constants.DB_USERS)
                    .document(dataLoaderModel.get(position).chatUserId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        UserModel user = new UserModel(documentSnapshot.get(Constants.DB_NAME).toString()
                                , documentSnapshot.get(Constants.DB_PASSWORD).toString()
                                , documentSnapshot.get(Constants.DB_PHONE_NUMBER).toString()
                                , documentSnapshot.get(Constants.DB_PROFILE_PICTURE).toString()
                                , dataLoaderModel.get(position).chatUserId);
                        holder.chatUserNameTv.setText(documentSnapshot.get(Constants.DB_NAME).toString());
                        holder.chatUserProfilePicCiv.setImageBitmap(BitmapManipulator.stringToBitMap(documentSnapshot.get(Constants.DB_PROFILE_PICTURE).toString()));

                        holder.clickLayout.setOnClickListener(v -> {
                                    Intent intent = new Intent(context, ChatPage.class);
                                    Bundle args = new Bundle();
                                    args.putSerializable("UserModel", (Serializable) user);
                                    intent.putExtra("Bundle", args);
                                    context.startActivity(intent);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.d("Debug", "Failed to load chat");
                    });

        }
    }

    @Override
    public int getItemCount() {
        return dataLoaderModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView chatUserNameTv, chatUserLastMessageTv;
        CircleImageView chatUserProfilePicCiv;
        FrameLayout clickLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatUserNameTv = itemView.findViewById(R.id.chatUserNameTv);
            chatUserLastMessageTv = itemView.findViewById(R.id.chatUserLastMessageTv);
            chatUserProfilePicCiv = itemView.findViewById(R.id.chatUserProfilePicCiv);
            clickLayout = itemView.findViewById(R.id.clickLayout);

        }
    }
}
