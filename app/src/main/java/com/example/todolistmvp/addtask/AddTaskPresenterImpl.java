package com.example.todolistmvp.addtask;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;

import com.example.todolistmvp.R;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.alarm.AlarmUtil;
import com.example.todolistmvp.util.room.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTaskPresenterImpl implements AddTaskContract.Presenter,
        AddTaskContract.Iterator.OnFinishListener{


    AddTaskContract.View view;
    AddTaskContract.Iterator iterator;
    Context context;

    Task mTask;

    String mDate = "";
    String mTime = "";
    int mYear = -1, mMonth = -1, mDay = -1, mMinute = -1, mHour = -1;

    String detail,priority,category;


    public AddTaskPresenterImpl(Context context,
                                AddTaskContract.View view, AddTaskContract.Iterator iterator) {
        setView(view);
        this.iterator = iterator;
        this.context = context;
    }

    @Override
    public void setView(AddTaskContract.View view) {
        this.view = view;
    }

    @Override
    public void detailTaskClick() {
        view.navigateDetailTask();
    }

    @Override
    public void dateClick() {
        view.showDateDialog();
    }

    @Override
    public void timeClick() {
        view.showTimeDialog();
    }

    @Override
    public void addTaskClick(String title, boolean isRemind) {
        if (title.length() == 0) {
            view.showErrorInput();
        } else {
            String timeString = null;
            if (!isRemind || (isRemind && mYear * mMonth * mDay * mDay * mMinute >= 0)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.set(mYear, mMonth, mDay, mHour, mMinute);
                timeString = simpleDateFormat.format(calendar.getTime());

                if (!isRemind) {
                    timeString = null;
                }

                mTask = new Task.Builder().setIsAlarm(isRemind)
                        .setDateAlarm(timeString)
                        .setSubTask(detail)
                        .setTag(category)
                        .setTypeList(priority)
                        .createTask(title);
                view.onAddTaskClick(mTask);
            } else {
                view.showErrorReminder();
            }

        }


    }

    @Override
    public void onCompletePickDate(String date,int year,int month,int day) {
         mYear = year;
         mMonth = month;
         mDay = day;
    }

    @Override
    public void onCompletePickTime(String time,int hour,int minute) {
        mHour = hour;
        mMinute = minute;
    }

    @Override
    public void onReceiveResult(Intent data) {

        detail = data.getStringExtra(
                Constant.ChildConstantString.KEY_EXTRA_TASK_DETAIL.getValue());
        category = data.getStringExtra(
                Constant.ChildConstantString.KEY_EXTRA_TASK_CATEGORY.getValue());
        priority = data.getStringExtra(
                Constant.ChildConstantString.KEY_EXTRA_TASK_PRIORITY.getValue());
    }

    @Override
    public void onFailture(Throwable throwable) {

    }

    @Override
    public void onInsertSuccess(Task object) {
        AlarmUtil.addAlarm(context, mTask);
        view.insertComplete(object);
    }

    @Override
    public void insertData(Task object) {
        iterator.insertData(object,this);
    }
}
