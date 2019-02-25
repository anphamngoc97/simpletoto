package com.example.todolistmvp.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.example.todolistmvp.R;
import com.example.todolistmvp.maintask.MainTaskActivity;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

import java.util.Calendar;

public class ReceiverAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        Showlog.d("receive: " + calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONTH)
                +calendar.get(Calendar.DATE)+"  "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));

        final int TIME_VIBRATE = 4;

        final String title = intent
                .getStringExtra(Constant.ChildConstantString.KEY_BROADCAST_TASK_TITLE.getValue());

        final int INDEX = intent
                .getIntExtra(Constant.ChildConstantString.KEY_BROADCAST_TASK_ID.getValue(),0);

        Intent mainIntent = new Intent(context, MainTaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent mainPendingIntent = PendingIntent.getActivities(context,
                1,new Intent[]{mainIntent},0);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder notifiBuilder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Task")
                .setContentText(title)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setVibrate(new long[]{TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE})
                .setContentIntent(mainPendingIntent);

        notificationManager.notify(INDEX,notifiBuilder.build());

    }
}
