package com.example.todolistmvp.addtask;

import com.example.todolistmvp.util.room.model.Task;

public class AddTaskPresenterImpl implements AddTaskContract.Presenter,
        AddTaskContract.Iterator.OnFinishListener{


    AddTaskContract.View view;
    AddTaskContract.Iterator iterator;

    public AddTaskPresenterImpl(AddTaskContract.View view, AddTaskContract.Iterator iterator) {
        setView(view);
        this.iterator = iterator;
    }

    @Override
    public void setView(AddTaskContract.View view) {
        this.view = view;

    }


    @Override
    public void onFailture(Throwable throwable) {

    }

    @Override
    public void onInsertSuccess(Task object) {
        view.insertComplete();
    }

    @Override
    public void insertData(Task object) {
        iterator.insertData(object,this);
    }
}
