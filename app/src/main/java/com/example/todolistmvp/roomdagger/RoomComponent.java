package com.example.todolistmvp.roomdagger;

import android.app.Application;


import com.example.todolistmvp.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class,RoomModule.class})
@Singleton
public interface RoomComponent {
    Application getApplication();
    void inject(MainActivity mainActivity);
}
