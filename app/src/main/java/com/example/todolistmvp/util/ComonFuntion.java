package com.example.todolistmvp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ComonFuntion {
    public static Calendar getDateFromString(String s){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
        Calendar date = Calendar.getInstance();
        try {

            date.setTime(simpleDateFormat.parse(s));

            Showlog.d("h: " + date.get(Calendar.HOUR)+" minu: " + date.get(Calendar.MINUTE)+
                    " d: " +date.get(Calendar.DATE)+" m: " + date.get(Calendar.MONTH)+ " y: " +date.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }catch ( NullPointerException nullPointerException){
            return null;

        }
        return date;
    }
}
