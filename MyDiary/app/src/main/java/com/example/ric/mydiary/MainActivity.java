package com.example.ric.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ric.mydiary.Database.Category;
import com.example.ric.mydiary.Database.Event;
import com.example.ric.mydiary.Database.EventsDataSource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton addEventButton;
    private FloatingActionButton addReminderButton;
    private FloatingActionButton exportButton;
    private ListView listView;
    EventsDataSource mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new EventsDataSource(this);
        ArrayList<Event> list = mydb.getEventsByCategory(Category.Car);
        ArrayAdapter<Event> arrayAdapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, list);

        listView = (ListView) findViewById(R.id.list_of_todays_events);
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


        addEventButton = (FloatingActionButton) findViewById(R.id.btn_add_event);
        addEventButton.setOnClickListener(this);
        addReminderButton = (FloatingActionButton) findViewById(R.id.btn_add_reminder);
        addReminderButton.setOnClickListener(this);
        exportButton = (FloatingActionButton) findViewById(R.id.btn_export);
        exportButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_add_event: {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_add_reminder: {
                Intent intent = new Intent(MainActivity.this, RemindActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_export: {
                Intent intent = new Intent(MainActivity.this, ExportActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        mydb.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mydb.close();
        super.onPause();
    }
}