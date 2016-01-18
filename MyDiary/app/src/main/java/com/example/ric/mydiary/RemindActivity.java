package com.example.ric.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class RemindActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton btnSetReminder;
    EditText inputdays;
    Long days;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        btnSetReminder = (FloatingActionButton) findViewById(R.id.btn_add_reminder_activity);
        btnSetReminder.setOnClickListener(this);
        inputdays = (EditText) findViewById(R.id.input_days);
//        if (inputdays.getText().toString()==""){
//            days=0L;
//        }
//        else {
//            days = Long.parseLong(inputdays.getText().toString());
//        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_add_reminder_activity: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
