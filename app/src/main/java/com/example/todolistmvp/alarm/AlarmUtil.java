package com.example.todolistmvp.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.todolistmvp.room.model.Task;
import com.example.todolistmvp.util.CommonFuntion;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

import java.util.Calendar;
import java.util.StringTokenizer;

public class AlarmUtil {


    public static void addAlarm(Context context, Task task) {

        if(task.dateAlarm==null || task.dateAlarm.length()==0) return;

        if(!task.isAlarm){
            cancelAlarm(context,task);
            return;
        }


        final int INDEX = task.id;


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReceiverAlarm.class);
        intent.putExtra(Constant.ChildConstantString.KEY_BROADCAST_TASK_TITLE.getValue(),task.title);
        intent.putExtra(Constant.ChildConstantString.KEY_BROADCAST_TASK_ID.getValue(),task.id);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, INDEX, intent,0);


        Calendar calendar = CommonFuntion.getDateFromString(task.dateAlarm);

        if(calendar==null) return;

        Showlog.d("alarm utiltest: " + Calendar.getInstance().get(Calendar.MINUTE));
        Showlog.d("alarm util: "+calendar.get(Calendar.MINUTE));

        //todo test
        calendar.add(Calendar.MINUTE,1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager
                    .setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }else{
            alarmManager
                    .set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }


 //       alarmManager.setExact(AlarmManager.RTC_WAKEUP, );

//        for (int i = 0; i < 10; i++) {
//            intent.putExtra(Constant.KEY_TYPE, INDEX);
//            PendingIntent pendingIntent =
//                    PendingIntent.getService(context, INDEX, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            INDEX++;
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MINUTE, INDEX);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager
//                        .setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            } else {
//                alarmManager
//                        .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            }
//    }
    }

    public static void cancelAlarm(Context context, Task task){

        if(task.dateAlarm==null || task.dateAlarm.length()==0) return;

        StringTokenizer st = new StringTokenizer(task.dateAlarm, " ");
        String date, time;
        int TIME_VIBRATE;

        if (st.hasMoreTokens()) date = st.nextToken();
        if (st.hasMoreTokens()) time = st.nextToken();
        final int INDEX = task.id;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReceiverAlarm.class);
        intent.putExtra(Constant.ChildConstantString.KEY_BROADCAST_TASK_TITLE.getValue(),task.title);
        intent.putExtra(Constant.ChildConstantString.KEY_BROADCAST_TASK_ID.getValue(),task.id);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, INDEX, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(pendingIntent);

    }
}