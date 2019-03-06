package com.example.todolistmvp.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.example.todolistmvp.R;
import com.example.todolistmvp.maintask.MainTaskActivity;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

import java.util.Calendar;

public class ReceiverAlarm extends BroadcastReceiver {
    private  static Notification.Builder notifiBuilder= null;
    private static Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
    private static final int NOTIFICATION_ID = 0;

    public static final String KEY_BUNDLE = "KEY_BUNDLE";
    public static final String KEY_BUNDLE_CONTENT = "KEY_BUNDLE_CONTENT";
    public static final String KEY_BUNDLE_AMOUNT_CONTENT = "KEY_BUNDLE_AMOUNT_CONTENT";

    @Override
    public void onReceive(Context context, Intent intent) {

        createBuilder(context);


        final String titleTask = intent
                .getStringExtra(Constant.ChildConstantString.KEY_BROADCAST_TASK_TITLE.getValue());

        final int INDEX = intent
                .getIntExtra(Constant.ChildConstantString.KEY_BROADCAST_TASK_ID.getValue(),0);

        Intent mainIntent = new Intent(context, MainTaskActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);



        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);


        Bundle bundle = notifiBuilder.getExtras();

        String curContent = bundle.getString(KEY_BUNDLE_CONTENT,"");
        String contentNotify;
        int amount = bundle.getInt(KEY_BUNDLE_AMOUNT_CONTENT,0);


        bigTextStyle.setBigContentTitle("You have "+(amount+1) +" task");
        if(amount==0){
            contentNotify = "> "+titleTask;
        }else {
            contentNotify = curContent + "\n> " + titleTask;
        }

        bigTextStyle.bigText(contentNotify);
        notifiBuilder.setStyle(bigTextStyle);

        bundle.putString(KEY_BUNDLE_CONTENT,contentNotify);
        bundle.putInt(KEY_BUNDLE_AMOUNT_CONTENT,amount+1);


        Showlog.d("content notify: "+amount + contentNotify);
        notifiBuilder.setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID,notifiBuilder.build());


    }
    public static void createBuilder(Context context){
        final int TIME_VIBRATE = 4;
        Intent mainIntent = new Intent(context, MainTaskActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);


        PendingIntent mainPendingIntent = PendingIntent.getActivities(context,
                1,new Intent[]{mainIntent},0);

        if (notifiBuilder==null){
            notifiBuilder = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.title_notification))
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setVibrate(new long[]{TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE})
                    .setContentIntent(mainPendingIntent);
            notifiBuilder.setDeleteIntent(createOnDismissedIntent(context,NOTIFICATION_ID));

        }
    }
    public static void removeNotification(){
        notifiBuilder = null;
    }
    private static PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, ReceiverDismissNotification.class);

        intent.putExtra(KEY_BUNDLE,notifiBuilder.getExtras());

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }
}
