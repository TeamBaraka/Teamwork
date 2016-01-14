package com.example.ric.mydiary;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ric.mydiary.Database.Event;
import com.example.ric.mydiary.Database.EventsDataSource;
import com.example.ric.mydiary.HelperClasses.PagerAdapter;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton addEventButton;
    private FloatingActionButton addReminderButton;
    private FloatingActionButton exportButton;
    private ListView listView;
    EventsDataSource mydb;
    private View rootView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.context= getActivity();
        mydb = new EventsDataSource(context);
        //ArrayList<Event> list = mydb.getEventsByCategory(Category.Birthday);
        ArrayList<Event> list = mydb.getEventsByDate();
        ArrayAdapter<Event> arrayAdapter = new ArrayAdapter<Event>(context, android.R.layout.simple_list_item_1, list);

        listView = (ListView) rootView.findViewById(R.id.list_of_todays_events);
        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//                int id_To_Search = arg2 + 1;
//
//                Bundle dataBundle = new Bundle();
//                dataBundle.putInt("id", id_To_Search);
//
//                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
//
//                intent.putExtras(dataBundle);
//                startActivity(intent);
//            }
//        });


        addEventButton = (FloatingActionButton) rootView.findViewById(R.id.btn_add_event);
        addEventButton.setOnClickListener(this);
        addReminderButton = (FloatingActionButton) rootView.findViewById(R.id.btn_add_reminder);
        addReminderButton.setOnClickListener(this);
        exportButton = (FloatingActionButton) rootView.findViewById(R.id.btn_export);
        exportButton.setOnClickListener(this);

        return rootView;
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
//    @Override
//    protected void onResume() {
//        mydb.open();
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        mydb.close();
//        super.onPause();
//    }
}





