package ru.nove.model.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class DateUtil {
    public static final String PATTERN = "dd.MM hh:mm";

    public static long now(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static String formatTime(long now){
        return new SimpleDateFormat(PATTERN).format(now);
    }
}
