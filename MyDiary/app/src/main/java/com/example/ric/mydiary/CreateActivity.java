package com.example.ric.mydiary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ric.mydiary.Database.Category;
import com.example.ric.mydiary.Database.EventsDataSource;
import com.example.ric.mydiary.HelperClasses.DateSetter;
import com.example.ric.mydiary.HelperClasses.DateTimeSetter;
import com.example.ric.mydiary.HelperClasses.TimeSetter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputTitle;
    private EditText inputDescription;
    private Spinner inputCategory;
    private EditText inputDate;
    private EditText inputTime;
    private ImageView imageView;
    private EditText inputPlace;
    private String inputImage;
    private Button cancelButton;
    private Button saveButton;
    private ImageButton placeButton;
    int myRequestCode = 1234;
    private LocationManager locationManager;
    private android.location.LocationListener locationListener;

    EventsDataSource mydb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mydb = new EventsDataSource(this);

        inputTitle = (EditText) findViewById(R.id.edit_title);
        inputDescription = (EditText) findViewById(R.id.edit_description);
        inputCategory = (Spinner) findViewById(R.id.edit_category);
        inputCategory.setAdapter(new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, Category.values()));
        inputDate = (EditText) findViewById(R.id.edit_date);
        DateSetter dateSetter = new DateSetter(this, inputDate);
        inputTime = (EditText) findViewById(R.id.edit_time);
        TimeSetter timeSetter = new TimeSetter(this, inputTime);
        imageView = (ImageView) this.findViewById(R.id.photo_taken);
        inputPlace = (EditText) findViewById(R.id.edit_place);

        saveButton = (Button) findViewById(R.id.btn_save);
        saveButton.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(this);
        placeButton = (ImageButton) findViewById(R.id.btn_take_place);
        placeButton.setOnClickListener(this);
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

                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_cancel: {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_take_place: {
//                Intent intent = new Intent(CreateActivity.this, MapsActivity.class);
//                startActivity(intent);

                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                locationListener = new LocationListener();
                if (this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    this.requestPermissions(new String[]{
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            myRequestCode);
                }

                if (this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                locationManager.removeUpdates(locationListener);
                break;
            }
        }
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
            //todo: here goes google maps api thing
            inputPlace.setText("Lat: " + String.valueOf(location.getLatitude()) +
                    " Long: " + String.valueOf(location.getLongitude()));
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

}