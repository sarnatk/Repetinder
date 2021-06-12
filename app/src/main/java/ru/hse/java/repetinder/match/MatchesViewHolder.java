package ru.hse.java.repetinder.match;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.java.repetinder.R;

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView matchEmail, matchFullname;
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

    }
}
