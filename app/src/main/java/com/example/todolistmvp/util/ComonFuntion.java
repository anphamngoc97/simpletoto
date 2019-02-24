package com.example.todolistmvp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ComonFuntion {
    public static Calendar getDateFromString(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
        Calendar date = Calendar.getInstance();
        try {

            date.setTime(simpleDateFormat.parse(s));

            Showlog.d("h: " + date.get(Calendar.HOUR) + " minu: " + date.get(Calendar.MINUTE) +
                    " d: " + date.get(Calendar.DATE) + " m: " + date.get(Calendar.MONTH) + " y: " + date.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException nullPointerException) {
            return null;

        }
        return date;
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
