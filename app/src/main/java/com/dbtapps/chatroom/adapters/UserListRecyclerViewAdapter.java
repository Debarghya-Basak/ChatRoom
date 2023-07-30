package com.dbtapps.chatroom.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.utilities.BitmapManipulator;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<UserListRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> userListLoader;

    public UserListRecyclerViewAdapter(Context context, ArrayList<String> userListLoader){
        this.context = context;
        this.userListLoader = userListLoader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.userlist_layout_userlistpage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(TextUtils.isEmpty(holder.chatUserNameTv.getText())){

            Constants.db.collection(Constants.DB_CHATS)
                    .document(userListLoader.get(position))
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        holder.chatUserPhoneNumberTv.setText(documentSnapshot.get(Constants.DB_PHONE_NUMBER).toString());
                    })
                    .addOnFailureListener(e -> {
                        Log.d("Debug", "Failed to load last message of the chat");
                    });

            Constants.db.collection(Constants.DB_USERS)
                    .document(userListLoader.get(position))
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        holder.chatUserNameTv.setText(documentSnapshot.get(Constants.DB_NAME).toString());
                        holder.chatUserProfilePicCiv.setImageBitmap(BitmapManipulator.stringToBitMap(documentSnapshot.get(Constants.DB_PROFILE_PICTURE).toString()));
                    })
                    .addOnFailureListener(e -> {
                        Log.d("Debug", "Failed to load chat");
                    });
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView chatUserNameTv, chatUserPhoneNumberTv;
        CircleImageView chatUserProfilePicCiv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatUserNameTv = itemView.findViewById(R.id.chatUserNameTv);
            chatUserPhoneNumberTv = itemView.findViewById(R.id.chatUserPhoneNumberTv);
            chatUserProfilePicCiv = itemView.findViewById(R.id.chatUserProfilePicCiv);
        }
    }

}
