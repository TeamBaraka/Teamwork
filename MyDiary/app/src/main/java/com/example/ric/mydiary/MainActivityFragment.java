package com.example.ric.mydiary;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ric.mydiary.Database.Event;
import com.example.ric.mydiary.Database.EventsDataSource;
import com.example.ric.mydiary.HelperClasses.EventAdapter;

import java.util.ArrayList;
import java.util.Date;

public class MainActivityFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private FloatingActionButton addEventButton;
    private FloatingActionButton addReminderButton;
    private FloatingActionButton exportButton;
    private ListView listView;
    EventsDataSource mydb;
    private View rootView;
    private Context context;
    private EventAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.context = getActivity();
        mydb = new EventsDataSource(context);
        //mydb.delete();

        ArrayList<Event> list = mydb.getEventsForToday();
        adapter = new EventAdapter(context, list);

        listView = (ListView) rootView.findViewById(R.id.list_of_todays_events);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

        addEventButton = (FloatingActionButton) rootView.findViewById(R.id.btn_add_event);
        addEventButton.setOnClickListener(this);
        addReminderButton = (FloatingActionButton) rootView.findViewById(R.id.btn_add_reminder);
        addReminderButton.setOnClickListener(this);
        exportButton = (FloatingActionButton) rootView.findViewById(R.id.btn_export);
        exportButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event event = adapter.getItem(position);

        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra("id", event.getId());
        intent.putExtra("SENDER_CLASS_NAME", this.getClass());

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_add_event: {
                Intent intent = new Intent(context, CreateActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_add_reminder: {
                Intent intent = new Intent(context, RemindActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_export: {
                Intent intent = new Intent(context, ExportActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        mydb.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        mydb.close();
        super.onPause();
    }


}





