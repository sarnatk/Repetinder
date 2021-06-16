package ru.hse.java.repetinder.match;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.activities.ChatActivity;
import ru.hse.java.repetinder.activities.MatchProfileActivity;

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView matchEmail, matchFullname;
    public String matchUserRole, matchId;
    public ImageView matchImage;
    public ImageButton sendButton;
    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        matchEmail = itemView.findViewById(R.id.matchEmail);
        matchFullname = itemView.findViewById(R.id.matchFullname);
        matchImage = itemView.findViewById(R.id.matchImage);
        sendButton = itemView.findViewById(R.id.match_sendbutton);

        sendButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ChatActivity.TEXT1, matchId);
            bundle.putString(ChatActivity.TEXT2, matchUserRole);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), MatchProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MatchProfileActivity.TEXT1, matchId);
        bundle.putString(MatchProfileActivity.TEXT2, matchUserRole);
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
    }
}
