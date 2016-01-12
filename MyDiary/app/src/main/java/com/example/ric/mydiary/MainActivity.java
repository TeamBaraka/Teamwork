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
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton addEventButton;
    private FloatingActionButton addReminderButton;
    private FloatingActionButton exportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
