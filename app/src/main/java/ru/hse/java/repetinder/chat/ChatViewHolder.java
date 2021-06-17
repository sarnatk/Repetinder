package ru.hse.java.repetinder.chat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.java.repetinder.R;

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView message;
    public TextView time;
    public LinearLayout container;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        message = itemView.findViewById(R.id.messageText);
        time = itemView.findViewById(R.id.timeMessage);
        container = itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View v) {
    }
}
