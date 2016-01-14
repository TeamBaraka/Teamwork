package com.example.ric.mydiary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.ric.mydiary.Database.Category;
import com.example.ric.mydiary.Database.EventsDataSource;
import com.example.ric.mydiary.HelperClasses.DateSetter;
import com.example.ric.mydiary.HelperClasses.DateTimeSetter;
import com.example.ric.mydiary.HelperClasses.TimeSetter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputTitle;
    private EditText inputDescription;
    private Spinner inputCategory;
    private EditText inputDate;
    private EditText inputTime;
    private ImageView imageView;
    private EditText inputPlace;
    private Button cancelButton;
    private Button saveButton;
    private ImageButton placeButton;
    int myRequestCode = 1234;
    EventsDataSource mydb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mydb = new EventsDataSource(this);
        //mydb.open();

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
                        DateTimeSetter.getDateFromDisplayString(inputDate.getText().toString(),inputTime.getText().toString()),
                        inputPlace.getText().toString(),
                        inputPlace.getText().toString()
                );
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
                Intent intent = new Intent(CreateActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    public void takePhoto(View view) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePhotoIntent, myRequestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == myRequestCode && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            this.imageView.setImageBitmap(imageBitmap);
        }
    }
}