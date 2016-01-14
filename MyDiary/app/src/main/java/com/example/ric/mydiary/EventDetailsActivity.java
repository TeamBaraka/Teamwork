package com.example.ric.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ric.mydiary.Database.Category;
import com.example.ric.mydiary.Database.Event;
import com.example.ric.mydiary.Database.EventsDataSource;
import com.example.ric.mydiary.HelperClasses.DateSetter;
import com.example.ric.mydiary.HelperClasses.DateTimeSetter;
import com.example.ric.mydiary.HelperClasses.TimeSetter;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private TextView description;
    private TextView category;
    private TextView date;
    private TextView time;
    private ImageView imageView;
    private TextView place;
    private ImageButton closeButton;
    EventsDataSource mydb;
    Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        closeButton = (ImageButton)findViewById(R.id.btn_close_details);
        closeButton.setOnClickListener(this);

        mydb = new EventsDataSource(this);

        Bundle bundle = getIntent().getExtras();
        Long id = bundle.getLong("id");

        currentEvent = mydb.getEventsById(id);

        title = (TextView) findViewById(R.id.tv_title);
        description = (TextView) findViewById(R.id.tv_description);
        category = (TextView) findViewById(R.id.tv_category);
        date = (TextView) findViewById(R.id.tv_date);
        time = (TextView) findViewById(R.id.tv_time);
        place = (TextView) findViewById(R.id.tv_place);
        imageView = (ImageView) this.findViewById(R.id.photo_taken);

        title.setText(currentEvent.getTitle());
        description.setText(currentEvent.getDescription());
        category.setText(currentEvent.getCategory());
        date.setText(DateTimeSetter.setDateToDisplayString(currentEvent.getDateTime()));
        place.setText(currentEvent.getPlace());
        //imageView.setImageBitmap();

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_close_details: {
                Toast.makeText(getApplicationContext(), "Event details closed!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EventDetailsActivity.this, MainActivity.class);

                startActivity(intent);
                break;
            }
        }
    }
}
