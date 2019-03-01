package com.example.todolistmvp.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.todolistmvp.util.Showlog;

public class ReceiverDismissNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras().getBundle(ReceiverAlarm.KEY_BUNDLE);
        if(bundle!=null) {
            Showlog.d("receive event dismiss notification");

            bundle.putString(ReceiverAlarm.KEY_BUNDLE_CONTENT, "");
            bundle.putInt(ReceiverAlarm.KEY_BUNDLE_AMOUNT_CONTENT, 0);
            ReceiverAlarm.removeNotification();

        }
    }
}
