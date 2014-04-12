package com.example.Lastfm.helpers;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ShutUpAndSkate on 30.03.14.
 */
public class CalendarHelper {
    public CalendarHelper() {
    }

    public static String prettifyTrackDate(Long l) {
        Long then = l*1000,
             now = System.currentTimeMillis(),
             diff = now - then,
             diffInMins = diff/(1000*60),
             diffInHours = diffInMins/60;

        Calendar c1 = Calendar.getInstance(),
                 c2 = Calendar.getInstance();

        c1.add(Calendar.DAY_OF_YEAR, -1);   // yesterday
        c2.setTime(new Date(then));         // date of track listening

        Boolean yesterday = c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);

        if(diffInMins <= 0) {
            return "Now";
        }
        if(diffInMins < 60 && diffInMins > 0) {
            return diffInMins.toString() + " min ago";
        }

        if (yesterday) {
            return "Yesterday";
        } else {
            if(diffInHours >= 1 && diffInHours < 24) {
                return diffInHours.toString() + " hours ago";
            } else {
                return new SimpleDateFormat("d MMM").format(new Date(then));
            }
        }
    }


}
