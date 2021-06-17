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

    public static String getTestExample() throws ParseException {
        return "2000-10-10 10:10:10.000";
    }
}
