package com.example.todolistmvp.maintask;

import com.example.todolistmvp.util.room.model.Task;
import com.example.todolistmvp.util.CommonFuntion;
import com.example.todolistmvp.util.Showlog;

import java.util.Calendar;
import java.util.List;

public class MainTaskPresenterImpl implements MainTaskContract.Presenter,
        MainTaskContract.Iterator.OnFinishListener {

    MainTaskContract.View view;
    MainTaskContract.Iterator iterator;

    public MainTaskPresenterImpl(MainTaskContract.View view, MainTaskContract.Iterator iterator) {
        setView(view);
        this.iterator = iterator;
    }

    @Override
    public void setView(MainTaskContract.View view) {
        this.view = view;
    }

    @Override
    public void loadData() {
        iterator.getData(this);
    }

    @Override
    public void onClickItem(int position) {
        view.navigateEditTask(position);
    }

    @Override
    public void updateData(Task object) {
        iterator.updateData(object,this);
    }

    @Override
    public void onGetSuccess(List<Task> res) {
        //remove past alrm and write to db
        Calendar calendarCurrent = Calendar.getInstance();
        for(Task task:res){
            Calendar calendarAlarm = CommonFuntion.getDateFromString(task.dateAlarm);
            if(calendarCurrent.equals(calendarAlarm) || calendarCurrent.after(calendarAlarm)){
                task.isAlarm = false;
            }

        }
        view.refreshData(res);
    }

    @Override
    public void onUpdateSuccess() {

    }

    @Override
    public void onFailture(Throwable throwable) {
        Showlog.d("maintask mPresenter fail: " + throwable.getMessage());
    }
}
