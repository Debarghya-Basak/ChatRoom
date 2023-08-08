package com.dbtapps.chatroom.adapters;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.models.MessageModel;

import java.util.ArrayList;

public class ChatMessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEWTYPE_SENDER = 0;
    private static final int VIEWTYPE_RECEIVER = 1;
    private Context context;
    private ArrayList<MessageModel> chatMessages;

    public ChatMessageRecyclerViewAdapter(Context context, ArrayList<MessageModel> chatMessages){
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEWTYPE_SENDER) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.send_message_chat_layout_chatpage, parent, false);
            return new SenderViewHolder(view);
        }
        else{
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.receive_message_chat_layout_chatpage, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == VIEWTYPE_SENDER) {
            ((SenderViewHolder) holder).messageTv.setText(chatMessages.get(position).message);
            ((SenderViewHolder) holder).timestampTv.setText(chatMessages.get(position).timestamp.getSeconds() + "");
        }
        else{
            ((ReceiverViewHolder) holder).messageTv.setText(chatMessages.get(position).message);
            ((ReceiverViewHolder) holder).timestampTv.setText(chatMessages.get(position).timestamp.getSeconds() + "");
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).sender_id.equals(Constants.getKeyUserid()))
            return VIEWTYPE_SENDER;
        else
            return VIEWTYPE_RECEIVER;
    }

    public static class SenderViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTv, timestampTv;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTv = (TextView) itemView.findViewById(R.id.messageTv);
            timestampTv = (TextView) itemView.findViewById(R.id.timestampTv);
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTv, timestampTv;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTv = (TextView) itemView.findViewById(R.id.messageTv);
            timestampTv = (TextView) itemView.findViewById(R.id.timestampTv);
        }
    }
}
