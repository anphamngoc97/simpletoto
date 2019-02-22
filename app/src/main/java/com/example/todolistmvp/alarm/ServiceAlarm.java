package com.example.todolistmvp.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ServiceAlarm extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
