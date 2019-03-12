package com.example.todolistmvp.edittask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.todolistmvp.util.CommonFuntion;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;
import com.example.todolistmvp.util.alarm.AlarmUtil;
import com.example.todolistmvp.util.room.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditTaskPresenterImpl implements EditTaskContract.Presenter,
        EditTaskContract.Iterator.OnFinishListener {

    EditTaskContract.Iterator iterator;
    EditTaskContract.View view;
    Context context;

    Task mTask;
    int mPosition;

    String mDate = "";
    String mTime = "";
    int mYear = -1, mMonth = -1, mDay = -1, mMinute = -1, mHour = -1;

    public EditTaskPresenterImpl(Context context, Intent data
            , EditTaskContract.View view, EditTaskContract.Iterator iterator) {
        this.iterator = iterator;
        this.context = context;
        setView(view);
        receiveData(data);
    }

    @Override
    public void setView(EditTaskContract.View view) {
        this.view = view;
    }

    public void receiveData(Intent data) {

        mTask = (Task) data
                .getSerializableExtra(Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_OBJECT.getValue());
        mPosition = data
                .getIntExtra(Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_POSITION.getValue(), 0);
        Calendar calendar = CommonFuntion.getDateFromString(mTask.dateAlarm);
        if (calendar != null) {
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DATE);
            mHour = calendar.get(Calendar.HOUR);
            mMinute = calendar.get(Calendar.MINUTE);
        }

        String title;
        String time = null, date = null;
        boolean isAlarm;

        title = mTask.title;
        isAlarm = mTask.isAlarm;

        if (calendar != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            date = simpleDateFormat.format(calendar.getTime());

            simpleDateFormat.applyPattern("hh:mm");
            time = simpleDateFormat.format(calendar.getTime());

        }

        view.initView(title, isAlarm, date, time);

    }

    @Override
    public void detailTaskClick() {

        Bundle  bundle = new Bundle();
        bundle.putString(Constant.ChildConstantString.KEY_EXTRA_TASK_DETAIL.getValue(),
                mTask.subTask);
        bundle.putString(Constant.ChildConstantString.KEY_EXTRA_TASK_CATEGORY.getValue(),
                mTask.typeList);
        bundle.putString(Constant.ChildConstantString.KEY_EXTRA_TASK_PRIORITY.getValue(),
                mTask.tag);

        Showlog.d("before navigate detail task: " + mTask.typeList+"_"+mTask.tag);
        view.navigateDetailTask(bundle);
    }

    @Override
    public void updateData(Task object) {
        iterator.updateData(object, this);
    }

    @Override
    public void removeData() {
        iterator.removeData(mTask, this);
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
    public void removeClick() {
        view.onRemoveClick();
    }

    @Override
    public void addTaskClick(String title, boolean isRemind) {

        if (title.length() == 0) {
            view.showErrorInput();
        } else {

            String timeString = null;
            if (!isRemind ||
                    (isRemind && mYear * mMonth * mDay * mDay * mMinute >= 0)) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.set(mYear, mMonth, mDay, mHour, mMinute);


                timeString = simpleDateFormat.format(calendar.getTime());

                if (!isRemind) {
                    timeString = null;
                }

                mTask.isAlarm = isRemind;
                mTask.dateAlarm = timeString;
                mTask.title = title;

                view.onUpdateTaskClick(mTask);
            } else {
                view.showErrorReminder();
            }

        }
    }

    @Override
    public void onCompletePickDate(String date, int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    @Override
    public void onCompletePickTime(String time, int hour, int minute) {
        mHour = hour;
        mMinute = minute;
    }

    @Override
    public void onReceiveResult(Intent data) {
        String detail = data
                .getStringExtra(Constant.ChildConstantString.KEY_EXTRA_TASK_DETAIL.getValue());
        String category = data
                .getStringExtra(Constant.ChildConstantString.KEY_EXTRA_TASK_CATEGORY.getValue());
        String priority = data
                .getStringExtra(Constant.ChildConstantString.KEY_EXTRA_TASK_PRIORITY.getValue());

        mTask.subTask = detail;
        mTask.typeList = category;
        mTask.tag = priority;
    }

    @Override
    public void onRemoveSuccess() {
        AlarmUtil.cancelAlarm(context, mTask);
        view.onRemoveSuccess(mPosition);
    }

    @Override
    public void onUpdateSuccess() {
        AlarmUtil.cancelAlarm(context, mTask);
        AlarmUtil.addAlarm(context, mTask);
        view.onUpdateSuccess(mPosition, mTask);
    }

    @Override
    public void onFailture(Throwable throwable) {
        Showlog.d("edittask presenter error: " + throwable.getMessage());
    }
}
