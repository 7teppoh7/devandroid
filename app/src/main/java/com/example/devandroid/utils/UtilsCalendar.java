package com.example.devandroid.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UtilsCalendar {
    public static final Calendar calendar = Calendar.getInstance();
    public static final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
    public static final SimpleDateFormat formatter = new SimpleDateFormat("d MMM (EEEE) ''yy в HH:mm", Locale.forLanguageTag("ru"));
    public static final SimpleDateFormat newsDateFormatter = new SimpleDateFormat("d MMMM yyyy г.", Locale.forLanguageTag("ru"));

    public static String getTestExample(){
        return "2020-10-10 10:10:10.000";
    }

    public static String formatForNews(String date) {
        try {
            Date dateFromEvent = parser.parse(date);
            return newsDateFormatter.format(dateFromEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "10 мая 2021 г.";
    }

}
