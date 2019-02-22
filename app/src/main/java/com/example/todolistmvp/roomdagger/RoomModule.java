package com.example.todolistmvp.roomdagger;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.todolistmvp.room.ResponsitoryTask;
import com.example.todolistmvp.room.local.DaoTask;
import com.example.todolistmvp.room.local.DatabaseTask;
import com.example.todolistmvp.room.local.LocalDataSourceTask;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {AppModule.class})
public class RoomModule {


//    public RoomModule(Application application){
//        databaseTask = Room.databaseBuilder(
//                application.getApplicationContext(),
//                DatabaseTask.class,DatabaseTask.DB_NAME
//        )
//                .build();
//
//    }

    @Provides
    @Singleton
    public DatabaseTask provideDatabaseTask(Application application){
        DatabaseTask databaseTask = Room.databaseBuilder(
                application.getApplicationContext(),
                DatabaseTask.class,DatabaseTask.DB_NAME
        )
                .build();

        return databaseTask;
    }


//    @Provides
//    @Singleton
//    public DatabaseTask provideDatabaseTask(){
//        return databaseTask;
//    }


    @Provides
    @Singleton
    public DaoTask provideDaoTask(DatabaseTask databaseTask){
        return databaseTask.daoTask();
    }

    @Provides
    @Singleton
    public LocalDataSourceTask provideLocalDataSourceTask(DaoTask daoTask){
        return new LocalDataSourceTask(daoTask);
    }
    @Provides
    @Singleton
    ResponsitoryTask provideResponsitoryTask(LocalDataSourceTask localDataSourceTask){
        return new ResponsitoryTask(localDataSourceTask);
    }

}
