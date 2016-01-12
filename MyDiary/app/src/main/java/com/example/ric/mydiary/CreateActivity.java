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
import com.example.ric.mydiary.HelperClasses.DateSetter;
import com.example.ric.mydiary.HelperClasses.TimeSetter;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputDate;
    private EditText inputTime;
    private Spinner inputCategory;
    private ImageView imageView;
    private Button cancelButton;
    private Button saveButton;
    private ImageButton placeButton;
    int myRequestCode = 1234;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        inputCategory=(Spinner)findViewById(R.id.edit_category);
        inputCategory.setAdapter(new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, Category.values()));
        inputDate = (EditText) findViewById(R.id.edit_date);
        DateSetter dateSetter = new DateSetter(this,inputDate);
        inputTime = (EditText) findViewById(R.id.edit_time);
        TimeSetter timeSetter = new TimeSetter(this,inputTime);
        imageView = (ImageView) this.findViewById(R.id.photo_taken);
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

