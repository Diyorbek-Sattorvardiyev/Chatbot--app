package com.example.chatbotapp;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> chatMessages;
    private OnMessageClickListener listener;


    public interface OnMessageClickListener {
        void onMessageClick(Message message);
    }


    public ChatAdapter(List<Message> chatMessages, OnMessageClickListener listener) {
        this.chatMessages = chatMessages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_received, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message chatMessage = chatMessages.get(position);
        holder.messageText.setText(chatMessage.getMessage());

        if (chatMessage.isSentByUser()) {

            holder.messageContainer.setGravity(Gravity.END);
            holder.messageText.setBackgroundResource(R.drawable.bg_sent_message);
            holder.messageText.setTextColor(holder.itemView.getResources().getColor(android.R.color.white));
        } else {

            holder.messageContainer.setGravity(Gravity.START);
            holder.messageText.setBackgroundResource(R.drawable.bg_received_message);
            holder.messageText.setTextColor(holder.itemView.getResources().getColor(android.R.color.black));
        }


        holder.itemView.setOnClickListener(v -> {
            if (!chatMessage.isSentByUser() && listener != null) {
                listener.onMessageClick(chatMessage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        LinearLayout messageContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            messageContainer = itemView.findViewById(R.id.message_container);
        }
    }
}
