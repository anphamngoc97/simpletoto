package com.example.todolistmvp.util.room;


import com.example.todolistmvp.util.room.model.Task;

import java.util.List;

import io.reactivex.Flowable;

public class ResponsitoryTask implements DataSourceTask {
    private static ResponsitoryTask sResponsitoryTask;

    public static ResponsitoryTask getInstance(DataSourceTask dataSourceTask) {
        if (sResponsitoryTask == null) {
            sResponsitoryTask = new ResponsitoryTask(dataSourceTask);
        }
        return sResponsitoryTask;
    }

    DataSourceTask dataSourceTask;

    public ResponsitoryTask(DataSourceTask dataSourceTask) {
        this.dataSourceTask = dataSourceTask;
    }


    @Override
    public Flowable<List<Task>> getAllTask() {
        return dataSourceTask.getAllTask();
    }

    @Override
    public void insertTask(Task... task) {
        dataSourceTask.insertTask(task);
    }

    @Override
    public void editTask(int id, String title, String dateAlarm, boolean isComplete, boolean isAlarm,
                         String detail,String category,String priority) {
        dataSourceTask.editTask(id, title, dateAlarm, isComplete, isAlarm,detail,category,priority);
    }

    @Override
    public void removeTask(Task... tasks) {
        dataSourceTask.removeTask(tasks);
    }
}
