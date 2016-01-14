package com.example.ric.mydiary.HelperClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeSetter {
    static SimpleDateFormat sdfSqlite = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    static SimpleDateFormat sdfDisplayDatetime = new SimpleDateFormat(
            "dd.MM.yyyy HH:mm", Locale.getDefault());
    static String defaultTime = "00:00";

    public static String setDateToSqlite(Date date) {

        return sdfSqlite.format(date);
    }

    public static Date getDateFromSqlite(String date) {

        Date returnDate = new Date();
        try {
            returnDate = sdfSqlite.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public static String setDateToDisplayString(Date date) {

        return sdfDisplayDatetime.format(date);
    }

    public static Date getDateFromDisplayString(String date, String time) {
        if (time.isEmpty()) {
            time = defaultTime;
        }

        String datetime = date + " " + time;
        Date dateForDb = new Date();

        try {
            dateForDb = sdfDisplayDatetime.parse(datetime.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateForDb;
    }
}