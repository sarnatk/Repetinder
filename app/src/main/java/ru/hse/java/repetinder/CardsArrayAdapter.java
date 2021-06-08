package ru.hse.java.repetinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.card_image);

        name.setText(card.getFullname());
        image.setImageResource(R.drawable.reverse_gradient);

        return convertView;
    }
}
