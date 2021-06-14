package ru.hse.java.repetinder.match;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.activities.ChatActivity;

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView matchEmail, matchFullname;
    public String matchUserRole, matchId;
    public ImageView matchImage;
    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        matchEmail = itemView.findViewById(R.id.matchEmail);
        matchFullname = itemView.findViewById(R.id.matchFullname);
        matchImage = itemView.findViewById(R.id.matchImage);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ChatActivity.TEXT1, matchId);
        bundle.putString(ChatActivity.TEXT2, matchUserRole);
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
    }
}
