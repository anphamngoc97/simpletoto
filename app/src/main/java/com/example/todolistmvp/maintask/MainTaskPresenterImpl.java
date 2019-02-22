package com.example.todolistmvp.maintask;

import com.example.todolistmvp.room.model.Task;

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
    public void onGetSuccess(List<Task> res) {
        view.refreshData(res);
    }

    @Override
    public void onFailture(Throwable throwable) {

    }
}
