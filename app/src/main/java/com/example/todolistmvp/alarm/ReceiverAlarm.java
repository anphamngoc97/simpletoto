package com.example.todolistmvp.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.example.todolistmvp.R;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

public class ReceiverAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Showlog.d("receive alarm");

        final int TIME_VIBRATE = 6;

        final String title = intent.getStringExtra(Constant.KEY_BROADCAST_TASK_TITLE);

        final int INDEX = intent.getIntExtra(Constant.KEY_BROADCAST_TASK_ID,0);

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
                .setVibrate(new long[]{TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE});

        notificationManager.notify(INDEX,notifiBuilder.build());

    }
}
