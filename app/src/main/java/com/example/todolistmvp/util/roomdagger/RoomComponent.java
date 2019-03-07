package com.example.todolistmvp.util.roomdagger;

import android.app.Application;


import com.example.todolistmvp.MainActivity;
import com.example.todolistmvp.addtask.AddTaskActivity;
import com.example.todolistmvp.edittask.EditTaskActivity;
import com.example.todolistmvp.maintask.MainTaskActivity;
import com.example.todolistmvp.search.SearchActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class,RoomModule.class})
@Singleton
public interface RoomComponent {
    Application getApplication();
    void inject(MainActivity mainActivity);
    void inject(MainTaskActivity mainTaskActivity);
    void inject(AddTaskActivity addTaskActivity);
    void inject(EditTaskActivity editTaskActivity);
    void inject(SearchActivity searchActivity);
}
