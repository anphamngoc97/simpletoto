package com.example.todolistmvp.util;

import android.content.Context;

import com.example.todolistmvp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonFuntion {
    public static Calendar getDateFromString(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
        Calendar date = Calendar.getInstance();
        try {

            date.setTime(simpleDateFormat.parse(s));

//            Showlog.d("get date from string  h:" + date.get(Calendar.HOUR_OF_DAY) + " minu:"
//                    + date.get(Calendar.MINUTE) +" d:" + date.get(Calendar.DATE) + " m:" +
//                    date.get(Calendar.MONTH) + " y:" + date.get(Calendar.YEAR));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException nullPointerException) {
            return null;

        }
        return date;
    }

    public static String getTimeRemaining(Context context, String dateAlarm) {
        Calendar calendarAlarm = getDateFromString(dateAlarm);
        Calendar current = Calendar.getInstance();

        if (calendarAlarm == null) {
            return "Remain: " + context.getResources().getString(R.string.contentRemainNotSet);
        }

        if (current.after(calendarAlarm) ) {
            return "Remain: "  + context.getResources().getString(R.string.contentRemainPast);
        }
        if(current.equals(calendarAlarm)){
            return "Remain: "+ context.getResources().getString(R.string.contentRemainNow);
        }
        long remainInMillis = calendarAlarm.getTimeInMillis() - current.getTimeInMillis();
        Calendar calendarRemain = Calendar.getInstance();
        calendarRemain.setTimeInMillis(remainInMillis);
        long day, hour, min, sec;
        sec = remainInMillis / 1000;
        day = sec / (24 * 60 * 60);
        sec %= (24 * 60 * 60);
        hour = sec / (60 * 60);
        sec %= (60 * 60);
        min = sec / 60;
        sec %= 60;

        //day = TimeUnit.MILLISECONDS.toDays(remainInMillis);
        //hour = TimeUnit.MILLISECONDS.toHours(remainInMillis) - TimeUnit.DAYS.toHours(day);
        //min = TimeUnit.MILLISECONDS.toMinutes(remainInMillis) - TimeUnit.HOURS.toMinutes();

        String dayString, hourString, minString;
        dayString = String.valueOf(day) + " day";
        hourString = String.valueOf(hour) + " hour";
        minString = String.valueOf(min) + " minute";

        if (day > 1) dayString += "s";
        if (hour > 1) hourString += "s";
        if (min > 1) minString += "s";

        return "Remain: " + dayString + " " + hourString + " " + minString;
    }

    public static Constant.ChildConstantDetailTaskPriority getPriority(String priority) {
        try {
            Constant.ChildConstantDetailTaskPriority valuePriority = Constant
                    .ChildConstantDetailTaskPriority.valueOf(priority);

            return valuePriority;
        } catch (IllegalArgumentException e) {
            //Showlog.d("error commonfunction get priority holder:" +priority+"_"+ e.getMessage());
            return null;
        }
    }
    public static Constant.ChildConstantDetailTaskCategory getCategory(String category) {
        try {
            Constant.ChildConstantDetailTaskCategory valueCategory = Constant
                    .ChildConstantDetailTaskCategory.valueOf(category);

            return valueCategory;
        } catch (IllegalArgumentException e) {
            //Showlog.d(" error commonfunction get category holder: " +category+"_"+ e.getMessage());
            return null;
        }
    }



    //kmp algorithm
    private static int[] kMap(String s) {
        s = s.replace(" ", "");
        s = s.toLowerCase();

        int j = 0;
        int[] k = new int[s.length()];
        for (int i = 1; i < s.length(); i++) {
            while (j > 0 && s.charAt(i) != s.charAt(j)) {
                j = k[j - 1];
            }
            if (s.charAt(j) == s.charAt(j)) j++;
            k[i] = j;

        }
        return k;
    }

    public static boolean isMatch(String s, String pattern) {
        s = s.toLowerCase();
        pattern = pattern.toLowerCase();

        int[] k = kMap(pattern);
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            while (j > 0 && s.charAt(i) != pattern.charAt(j)) {
                j = k[j - 1];
            }
            if (s.charAt(i) == pattern.charAt(j)) j++;
            if (j == pattern.length()) {
                return true;
            }

        }
        return false;

    }


}
