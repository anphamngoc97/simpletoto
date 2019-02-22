package com.example.todolistmvp.room;


import com.example.todolistmvp.room.model.Task;

import java.util.List;

import io.reactivex.Flowable;

public interface DataSourceTask {

    Flowable<List<Task>> getAllTask();

    void insertTask(Task... task);

    void editTask(int id, String title, String dateAlarm,boolean isComplete, boolean isAlarm);

    void removeTask(Task... tasks);

}
