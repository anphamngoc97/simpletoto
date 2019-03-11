package com.example.todolistmvp.addtask;

import android.content.Intent;

import com.example.todolistmvp.BaseIterator;
import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;
import com.example.todolistmvp.util.room.model.Task;

public interface AddTaskContract {
    interface View extends BaseView {
        void insertComplete(Task task);
        void showDateDialog();
        void showTimeDialog();
        void onAddTaskClick(Task task);
        void showErrorInput();
        void showErrorReminder();
        void navigateDetailTask();
    }
    interface Presenter extends BasePresenter<View>{
        void insertData(Task object);
        void dateClick();
        void timeClick();
        void addTaskClick(String title,boolean isRemind);
        void detailTaskClick();
        void onCompletePickDate(String date,int year,int month,int day);
        void onCompletePickTime(String time,int hour,int minute);
        void onReceiveResult(Intent data);
    }
    interface Iterator extends BaseIterator{
        interface OnFinishListener extends BaseIterator.OnFinishListener{
            void onInsertSuccess(Task object);
        }
        void insertData(Task object, OnFinishListener onFinishListener);
    }
}
