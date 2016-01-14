package com.example.ric.mydiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ric.mydiary.Database.Category;
import com.example.ric.mydiary.Database.Event;
import com.example.ric.mydiary.Database.EventsDataSource;

import java.util.ArrayList;

public class MainSearchFragment extends Fragment implements View.OnClickListener{
    private ListView listView;
    EventsDataSource mydb;
    private View rootView;
    private Context context;
    Button searchByCategoryButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_search, container, false);
        this.context= getActivity();
        mydb = new EventsDataSource(context);

        searchByCategoryButton= (Button)rootView.findViewById(R.id.btn_search);
        searchByCategoryButton.setOnClickListener(this);
        listView = (ListView) rootView.findViewById(R.id.list_of_found_events);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_search: {
                ArrayList<Event> list = mydb.getEventsByCategory(Category.Birthday);
                ArrayAdapter<Event> arrayAdapter = new ArrayAdapter<Event>(context, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(arrayAdapter);
                break;
            }
        }
    }
}