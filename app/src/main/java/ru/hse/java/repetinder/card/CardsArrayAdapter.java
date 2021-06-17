package ru.hse.java.repetinder.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.hse.java.repetinder.R;

public class CardsArrayAdapter extends ArrayAdapter<Card> {
    Context context;

    public CardsArrayAdapter(Context context, int recourseId, List<Card> cardList) {
        super(context, recourseId, cardList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Card card = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.card_image);
        name.setText(card.getFullname());
        String profileImageUrl = card.getProfileImageUrl();
        if (profileImageUrl.equals("default")) {
            Glide.clear(image);
            Glide.with(convertView.getContext()).load(R.drawable.default_icon).into(image);
        } else {
            Glide.clear(image);
            Glide.with(convertView.getContext()).load(card.getProfileImageUrl()).into(image);
        }


        return convertView;
    }
}
