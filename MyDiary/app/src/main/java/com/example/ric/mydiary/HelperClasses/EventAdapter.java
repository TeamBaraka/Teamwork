package com.example.ric.mydiary.HelperClasses;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ric.mydiary.Database.Category;
import com.example.ric.mydiary.Database.Event;
import com.example.ric.mydiary.R;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final List<Event> values;
    private TextView title;
    private TextView description;
    private TextView color;
    private ImageView imageView;
    String category;
    String[] categories = new String[]{"Car", "Birthday", "Cafe", "Cinema", "Sport", "Lectures", "Shopping", "Period", "Trip"};
    String[] colors = new String[]{"red", "blue", "yellow", "black", "magenta", "green", "white", "cyan", "gray"};

    public EventAdapter(Context context, List<Event> events) {
        super(context, R.layout.list_item, events);
        this.context = context;
        this.values = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        Event event = getItem(position);
        if (event != null) {
            title = (TextView) rowView.findViewById(R.id.title);
            description = (TextView) rowView.findViewById(R.id.description);
            color = (TextView) rowView.findViewById(R.id.color);
            imageView = (ImageView) rowView.findViewById(R.id.icon);

            if (title != null) {
                title.setText(event.getTitle());
            }
            if (description != null) {
                description.setText(event.getDescription());
            }
            if (color != null) {
                category = event.getCategory();
                color.setText(category);

                color.setBackgroundColor(Color.parseColor(colors[java.util.Arrays.asList(categories).indexOf(category)]));
            }
        }

        return rowView;
    }
}
