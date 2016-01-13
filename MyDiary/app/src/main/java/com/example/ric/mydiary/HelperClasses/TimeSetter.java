package com.example.ric.mydiary.HelperClasses;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeSetter implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private EditText editText;
    private Context context;
    Calendar myCalendar = Calendar.getInstance();

    public TimeSetter(Context context, EditText editText) {
        this.editText = editText;
        this.editText.setOnClickListener(this);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
            int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = myCalendar.get(Calendar.MINUTE);
            new TimePickerDialog(context, this, hour, minute, true).show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //this.editText.setText( hourOfDay + ":" + minute);
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        this.editText.setText(sdf.format(myCalendar.getTime()));
    }

    public Date getChosenTime() {
        return myCalendar.getTime();
    }
}
