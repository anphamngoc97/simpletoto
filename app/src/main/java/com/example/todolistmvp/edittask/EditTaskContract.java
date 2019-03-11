package com.example.todolistmvp.edittask;

import android.content.Intent;

import com.example.todolistmvp.BaseIterator;
import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;
import com.example.todolistmvp.util.room.model.Task;

public interface EditTaskContract {
    interface View extends BaseView{
        void initView(String title,boolean isAlarm,String date,String time);
        void onRemoveSuccess(int position);

        void onUpdateSuccess(int position,Task task);

        void showDateDialog();
        void showTimeDialog();
        void onUpdateTaskClick(Task task);
        void onRemoveClick();
        void showErrorInput();
        void showErrorReminder();
        void navigateDetailTask();


    }
    interface Presenter extends BasePresenter<View>{
        void updateData(Task object);

        void removeData();

        void dateClick();
        void timeClick();
        void addTaskClick(String title,boolean isRemind);
        void removeClick();
        void detailTaskClick();
        void onCompletePickDate(String date,int year,int month,int day);
        void onCompletePickTime(String time,int hour,int minute);
        void onReceiveResult(Intent data);
    }
    interface Iterator extends BaseIterator{
        interface OnFinishListener extends BaseIterator.OnFinishListener{
            void onRemoveSuccess();

            void onUpdateSuccess();

        }
        void updateData(Task object, OnFinishListener onFinishListener);

        void removeData(Task object, OnFinishListener onFinishListener);
    }
}
