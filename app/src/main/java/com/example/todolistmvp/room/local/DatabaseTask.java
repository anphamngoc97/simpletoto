package com.example.todolistmvp.room.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.todolistmvp.room.model.Task;

@Database(entities = {Task.class},version = 1)
public abstract class DatabaseTask extends RoomDatabase{
    private static final int VERSION_DB = 1;
    public static DatabaseTask sTaskDatabase;

    public static final String DB_NAME = "Task_DB";

    public abstract DaoTask daoTask();

    public static DatabaseTask getInstance(Context context) {
        if(sTaskDatabase == null){
            sTaskDatabase = Room.databaseBuilder(context, DatabaseTask.class,DB_NAME)
            .fallbackToDestructiveMigration()
            .build();
        }

        return sTaskDatabase;
    }

}
