package com.dbtapps.chatroom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.models.ChatModel;
import com.dbtapps.chatroom.utilities.BitmapManipulator;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ChatFragmentRecyclerViewAdapter.ViewHolder> {

    private Context context;
    ArrayList<ChatModel> chatList;

    public ChatFragmentRecyclerViewAdapter(Context context, ArrayList<ChatModel> chatList){
        this.context = context;
        this.chatList = chatList;
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
        holder.chatUserProfilePicCiv.setImageBitmap(BitmapManipulator.stringToBitMap(chatList.get(position).userProfilePicture));
        holder.chatUserNameTv.setText(chatList.get(position).userName);
        holder.chatUserLastMessageTv.setText(chatList.get(position).lastText);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
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
