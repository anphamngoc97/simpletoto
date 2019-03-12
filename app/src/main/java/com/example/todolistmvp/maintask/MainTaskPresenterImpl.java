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
    public void searchClick() {
        view.navigateSearch();
    }

    @Override
    public void floatBtnAddClick() {
        view.navigateAddTask();
    }

    @Override
    public void onClickAdd(String title) {
        if(title.trim().length()>0){
            Task task = new Task.Builder().createTask(title);
            iterator.insertData(task,this);
        }
    }

    @Override
    public void onClickItem(int position,Task task) {
        view.navigateEditTask(position,task);
    }

    @Override
    public void updateCompleteData(int position, Task object, boolean checked) {
        object.isComplete = checked;
        iterator.updateData(position,object,this);
    }

    @Override
    public void updateData(int position,Task object) {
        iterator.updateData(position,object,this);
    }

    @Override
    public void onInsertData(Task object) {
        view.onInsertSuccess(object);
    }

    @Override
    public void onRemoveData(int position) {
        view.onRemoveSuccess(position);
    }

    @Override
    public void onInsertSuccess(Task object) {
        view.refreshEditText();
        view.onInsertSuccess(object);
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
    public void onUpdateSuccess(int position,Task task) {
        view.onUpdateSuccess(position,task);
    }

    @Override
    public void onFailture(Throwable throwable) {
        Showlog.d("maintask mPresenter fail: " + throwable.getMessage());
    }
}
