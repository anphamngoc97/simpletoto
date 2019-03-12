package com.example.todolistmvp.util.room.local;


import com.example.todolistmvp.util.room.DataSourceTask;
import com.example.todolistmvp.util.room.model.Task;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class LocalDataSourceTask implements DataSourceTask {
    private static LocalDataSourceTask sLocalDataSourceTask;

    public static LocalDataSourceTask getInstance(DaoTask daoTask) {
        if(sLocalDataSourceTask == null){
            sLocalDataSourceTask = new LocalDataSourceTask(daoTask);
        }
        return sLocalDataSourceTask;
    }

    DaoTask daoTask;

    @Inject
    public LocalDataSourceTask(DaoTask daoTask) {
        this.daoTask = daoTask;
    }

    @Override
    public Flowable<List<Task>> getAllTask() {
        return daoTask.getAllTask();
    }

    @Override
    public void insertTask(Task... task) {
        daoTask.insertTask(task);
    }

    @Override
    public void editTask(int id, String title, String dateAlarm,boolean isComplete, boolean isAlarm,
                         String detail,String category,String priority) {
        daoTask.editTask(id,title,dateAlarm,isComplete,isAlarm,detail,category,priority);
    }

    @Override
    public void removeTask(Task... tasks) {
        daoTask.removeTask(tasks);
    }
}
