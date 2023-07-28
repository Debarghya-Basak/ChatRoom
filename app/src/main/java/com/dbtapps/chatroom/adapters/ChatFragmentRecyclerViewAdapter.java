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
import com.dbtapps.chatroom.models.ChatLoaderModel;
import com.dbtapps.chatroom.models.ChatModel;
import com.dbtapps.chatroom.utilities.BitmapManipulator;
import com.dbtapps.chatroom.utilities.MakeToast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ChatFragmentRecyclerViewAdapter.ViewHolder> {

    private Context context;
    ArrayList<ChatLoaderModel> chatIds;
    ArrayList<ChatModel> chatList;

    public ChatFragmentRecyclerViewAdapter(Context context, ArrayList<ChatLoaderModel> chatIds){
        this.context = context;
        this.chatIds = chatIds;
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
//        holder.chatUserProfilePicCiv.setImageBitmap(BitmapManipulator.stringToBitMap(chatIds.get(position).userProfilePicture));
//        holder.chatUserNameTv.setText(chatIds.get(position).userName);
//        holder.chatUserLastMessageTv.setText("Temp");

        if(TextUtils.isEmpty(holder.chatUserNameTv.getText())){

            Constants.db.collection(Constants.DB_CHATS)
                    .document(chatIds.get(position).chatDocumentId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        holder.chatUserLastMessageTv.setText(documentSnapshot.get(Constants.DB_LAST_MESSAGE).toString());
                    })
                    .addOnFailureListener(e -> {
                        Log.d("Debug", "Failed to load last message of the chat");
                    });

            Constants.db.collection(Constants.DB_USERS)
                    .document(chatIds.get(position).chatUserId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        holder.chatUserNameTv.setText(documentSnapshot.get(Constants.DB_NAME).toString());
                        holder.chatUserProfilePicCiv.setImageBitmap(BitmapManipulator.stringToBitMap(documentSnapshot.get("profile_picture").toString()));
                    })
                    .addOnFailureListener(e -> {
                        Log.d("Debug", "Failed to load chat");
                    });
        }
    }

    @Override
    public int getItemCount() {
        return chatIds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView chatUserNameTv, chatUserLastMessageTv;
        CircleImageView chatUserProfilePicCiv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatUserNameTv = itemView.findViewById(R.id.chatUserNameTv);
            chatUserLastMessageTv = itemView.findViewById(R.id.chatUserLastMessageTv);
            chatUserProfilePicCiv = itemView.findViewById(R.id.chatUserProfilePicCiv);

        }
    }
}
