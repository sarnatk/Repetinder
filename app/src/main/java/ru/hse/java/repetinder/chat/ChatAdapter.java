package ru.hse.java.repetinder.chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.hse.java.repetinder.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private final List<Chat> chatList;

    public ChatAdapter(List<Chat> matchList, Context context) {
        this.chatList = matchList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        return new ChatViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.message.setText(chatList.get(position).getMessage());
        holder.time.setText(chatList.get(position).getTimeString());
        holder.message.setTextColor(Color.parseColor("#404040"));
        holder.time.setTextColor(Color.parseColor("#404040"));
        if (chatList.get(position).isCurrentUser()) {
            holder.message.setGravity(Gravity.END);
            holder.time.setGravity(Gravity.END);
            //holder.container.setBackgroundColor(Color.parseColor("#F4F4F4"));
        } else {
            holder.message.setGravity(Gravity.START);
            holder.time.setGravity(Gravity.START);
            //holder.container.setBackgroundColor(Color.parseColor("#BDBDBD"));
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
