package com.example.todolistmvp.room.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.todolistmvp.room.model.Task;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface DaoTask {


    @Query("SELECT * FROM Task")
    Flowable<List<Task>> getAllTask();

    @Insert
    void insertTask(Task... task);

    @Query("UPDATE Task SET title=:title,dateAlarm=:dateAlarm," +
            "isComplete=:isComplete, isAlarm=:isAlarm WHERE id=:id")
    void editTask(int id, String title, String dateAlarm,boolean isComplete, boolean isAlarm);

    @Delete
    void removeTask(Task... tasks);

}
