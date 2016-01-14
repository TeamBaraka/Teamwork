package com.example.ric.mydiary.HelperClasses;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateSetter implements View.OnClickListener,DatePickerDialog.OnDateSetListener {
    private EditText editText;
    private Context context;
    Calendar myCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));

    public DateSetter(Context context,EditText editText) {
        this.editText = editText;
        this.editText.setOnClickListener(this);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        new DatePickerDialog(context, this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDisplay();
    }

    private void updateDisplay() {

        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        this.editText.setText(sdf.format(myCalendar.getTime()));
    }

    public Date getChosenDate() {
        return myCalendar.getTime();
    }
}