package com.example.ric.mydiary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ric.mydiary.Database.Category;
import com.example.ric.mydiary.Database.Event;
import com.example.ric.mydiary.Database.EventsDataSource;
import com.example.ric.mydiary.HelperClasses.AlarmReceiver;
import com.example.ric.mydiary.HelperClasses.DateSetter;
import com.example.ric.mydiary.HelperClasses.DateTimeSetter;
import com.example.ric.mydiary.HelperClasses.ResultsContainer;
import com.example.ric.mydiary.HelperClasses.TimeSetter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;


public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputTitle;
    private EditText inputDescription;
    private Spinner inputCategory;
    private EditText inputDate;
    private EditText inputTime;
    private ImageView imageView;
    private EditText inputPlace;
    private Spinner placesSpinner;
    private String inputImage;
    private FloatingActionButton cancelButton;
    private FloatingActionButton saveButton;
    private ImageButton placeButton;
    int myRequestCode = 4;
    private LocationManager locationManager;
    private android.location.LocationListener locationListener;
    private boolean GoogleAsked = false;
    final private String DefaultRadiusInMeters = "50";
    final private String ApiKey = "AIzaSyD0P_UWoN2NkmoGf4_-tHFqdPtds1iEVuk";
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    DateSetter dateSetter;
    NotificationManager notificationManager;
    Long id;
    String sender;
    Bundle bundle;
    ArrayAdapter<Category> categoryArrayAdapter;


    EventsDataSource mydb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mydb = new EventsDataSource(this);

        bundle = getIntent().getExtras();


        inputTitle = (EditText) findViewById(R.id.edit_title);
        inputDescription = (EditText) findViewById(R.id.edit_description);
        inputCategory = (Spinner) findViewById(R.id.edit_category);
        categoryArrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, Category.values());
        inputCategory.setAdapter(categoryArrayAdapter);
        inputDate = (EditText) findViewById(R.id.edit_date);
        dateSetter = new DateSetter(this, inputDate);
        inputTime = (EditText) findViewById(R.id.edit_time);
        TimeSetter timeSetter = new TimeSetter(this, inputTime);
        imageView = (ImageView) this.findViewById(R.id.photo_taken);
        inputPlace = (EditText) findViewById(R.id.edit_place);
        placesSpinner = (Spinner) findViewById(R.id.spn_places);

        ArrayAdapter placesSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.empty_places, android.R.layout.simple_spinner_item);
        placesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placesSpinner.setAdapter(placesSpinnerAdapter);

        saveButton = (FloatingActionButton) findViewById(R.id.btn_save);
        saveButton.setOnClickListener(this);
        cancelButton = (FloatingActionButton) findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(this);
        placeButton = (ImageButton) findViewById(R.id.btn_take_place);
        placeButton.setOnClickListener(this);

        if (bundle != null) {
            editEvent();
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_save: {
                mydb.createEvent(
                        inputTitle.getText().toString(),
                        inputDescription.getText().toString(),
                        inputCategory.getSelectedItem().toString(),
                        DateTimeSetter.getDateFromDisplayString(inputDate.getText().toString(), inputTime.getText().toString()),
                        inputPlace.getText().toString(),
                        inputImage
                );

                Toast.makeText(getApplicationContext(), "Event saved!", Toast.LENGTH_SHORT).show();
                scheduleNotification(getNotification());
                if (locationManager != null) {
                    locationManager.removeUpdates(locationListener);
                }
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_cancel: {
                if (locationManager != null) {
                    locationManager.removeUpdates(locationListener);
                }
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_take_place: {
//                Intent intent = new Intent(CreateActivity.this, MapsActivity.class);
//                startActivity(intent);

                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                locationListener = new LocationListener();
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            myRequestCode);
                }

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                if (GoogleAsked) {
                    locationManager.removeUpdates(locationListener);
                }
                break;
            }
        }
    }

    private void editEvent() {
        id = bundle.getLong("id");
        sender = bundle.get("SENDER_CLASS_NAME").toString();
        Event currentEvent = mydb.getEventsById(id);
        inputTitle.setText(currentEvent.getTitle());
        inputDescription.setText(currentEvent.getDescription());

        inputPlace.setText(currentEvent.getPlace());
        Bitmap bitmap = BitmapFactory.decodeFile(currentEvent.getImage());
        imageView.setImageBitmap(bitmap);
    }

    public void takePhoto(View view) {
        Uri imageUri;
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePhotoIntent, myRequestCode);
            File photo = new File(getCacheDir().getPath(), "Pic_" + DateTimeSetter.setDateToSqlite(new Date()) + ".jpg");
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));

            imageUri = Uri.fromFile(photo);
            this.inputImage = imageUri.getPath();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == myRequestCode && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            SaveImage(imageBitmap, this.inputImage);
            this.imageView.setImageBitmap(imageBitmap);
        }
    }

    //todo: Review this!
    private void SaveImage(Bitmap bitmapData, String fullPath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fullPath);
            bitmapData.compress(Bitmap.CompressFormat.JPEG, 100, out);
            // (100) is the compression factor;
            // (100) - no compression, (0) - full compression, good value is around 80 - on lower value the picture get messy
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class LocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
//            inputPlace.setText("Lat: " + String.valueOf(location.getLatitude()) +
//                    " Long: " + String.valueOf(location.getLongitude()));

            placesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String pickedPlace = parent.getItemAtPosition(position).toString();
                    inputPlace.setText(pickedPlace);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            if (!GoogleAsked) {
                GetGoogleApiPlacesAsync(String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()),
                        String.valueOf(location.getAccuracy()));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private void GetGoogleApiPlacesAsync(String latitude, String longitude, String radiusInMeters) {
        if (radiusInMeters == null || radiusInMeters.isEmpty()) {
            radiusInMeters = DefaultRadiusInMeters;
        }

        String urlBase = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        String location = "location=" + latitude + "," + longitude;
        String radius = "&radius=" + radiusInMeters;
        String key = "&key=" + ApiKey;
        String fullURL = urlBase + location + radius + key;

        new DownloadFromGoogleApi().execute(fullURL);
    }

    private class DownloadFromGoogleApi extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);

                urlConnection = (HttpsURLConnection) url.openConnection();

                InputStream input = urlConnection.getInputStream();
                InputStreamReader inputReader = new InputStreamReader(input);

                int data = inputReader.read();
                while (data != -1) {
                    char current = (char) data;
                    data = inputReader.read();
                    response.append(current);
                }

                if(response.toString().contains("OVER_QUERY_LIMIT")) {
                    response = new StringBuilder();
                    response.append("{results:[{\"name\":\"Sofia\"},{\"name\":\"Zk. Mladost 1\"},{\"name\":\"bul. Aleksander Malinov\"}]}");
                }
            } catch (Exception e) {
                response = new StringBuilder();
                response.append("{results:[{\"name\":\"Sofia\"},{\"name\":\"Zk. Mladost 1\"},{\"name\":\"bul. Aleksander Malinov\"}]}");
            } finally {
                urlConnection.disconnect();
                GoogleAsked = true;
            }
            return response.toString();
        }

        protected void onPostExecute(String result) {
            ResultsContainer resultsContainer = ParseGoogleApiPlacesResult(result);

            if (resultsContainer.results.size() > 0) {
                String[] places = new String[resultsContainer.results.size()];
                for (int i = 0; i < resultsContainer.results.size(); i++) {
                    places[i] = resultsContainer.results.get(i).name;
                }

                ArrayAdapter newPlacesAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, places);
                newPlacesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                placesSpinner.setAdapter(newPlacesAdapter);
            }
        }
    }

    private ResultsContainer ParseGoogleApiPlacesResult(String googleApiPlacesJsonString) {
        List places = new ArrayList(20);
        Gson jsonObj = new Gson();
        ResultsContainer result = jsonObj.fromJson(googleApiPlacesJsonString, ResultsContainer.class);

        return result;
    }

    public void createNotification() {

        Notification.Builder builder =
                new Notification.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(inputTitle.getText().toString())
                        .setContentText(inputCategory.getSelectedItem().toString())
                        .setTicker("Alert New Event");

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, builder.build());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.PRIORITY_HIGH;

        long futureInMillis = SystemClock.elapsedRealtime() + 5000;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

//        notificationManager.notify(R.id.myDiary_notification, notification);
//        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC_WAKEUP, dateSetter.getChosenDate().getTime() - System.currentTimeMillis(), resultPendingIntent);
    }

    private void scheduleNotification(Notification notification) {

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);

        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = dateSetter.getChosenDate().getTime() - System.currentTimeMillis(); //SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification() {
        Notification.Builder builder =
                new Notification.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(inputTitle.getText().toString())
                        .setContentText(inputCategory.getSelectedItem().toString())
                        .setTicker("Alert New Event");
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);
//        notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setContentIntent(resultPendingIntent);

        stackBuilder.addNextIntent(resultIntent);
        return builder.build();
    }
}