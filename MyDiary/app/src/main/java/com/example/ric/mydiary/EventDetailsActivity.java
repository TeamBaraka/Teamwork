package com.example.ric.mydiary;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private TextView description;
    private TextView category;
    private TextView date;
    private TextView time;
    private ImageView imageView;
    private TextView place;
    private FloatingActionButton closeButton;
    EventsDataSource mydb;
    Event currentEvent;
    String sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        closeButton = (FloatingActionButton) findViewById(R.id.btn_close_details);
        closeButton.setOnClickListener(this);

        mydb = new EventsDataSource(this);

        Bundle bundle = getIntent().getExtras();
        Long id = bundle.getLong("id");
        sender = bundle.get("SENDER_CLASS_NAME").toString();

        currentEvent = mydb.getEventsById(id);

        title = (TextView) findViewById(R.id.tv_title);
        description = (TextView) findViewById(R.id.tv_description);
        category = (TextView) findViewById(R.id.tv_category);
        date = (TextView) findViewById(R.id.tv_date);
        place = (TextView) findViewById(R.id.tv_place);
        imageView = (ImageView) this.findViewById(R.id.iv_image);

        title.setText(currentEvent.getTitle());
        description.setText(currentEvent.getDescription());
        category.setText(currentEvent.getCategory());
        date.setText(DateTimeSetter.setDateToDisplayString(currentEvent.getDateTime()));
        place.setText(currentEvent.getPlace());
        //new DownloadImageTask(imageView).execute(currentEvent.getImage());
        Bitmap bitmap = BitmapFactory.decodeFile(currentEvent.getImage());
        imageView.setImageBitmap(bitmap);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_close_details: {
                Toast.makeText(getApplicationContext(), "Event details closed!", Toast.LENGTH_SHORT).show();
                Intent intent;
                switch (sender) {
                    case "class com.example.ric.mydiary.MainActivityFragment":
                        intent = new Intent(EventDetailsActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case "class com.example.ric.mydiary.MainSearchFragment":
                        intent = new Intent(EventDetailsActivity.this, MainActivity.class);
                        MainActivity.viewPager.setCurrentItem(1);
                        startActivity(intent);

//                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        MainSearchFragment fr =new MainSearchFragment();
//                        ft.add(fr, null);
//                        ft.commit();
//                        Fragment mFragment = new MainSearchFragment();
//                        getSupportFragmentManager().beginTransaction().replace(EventDetailsActivity.this, mFragment).commit();


                        break;
                }
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return image;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}