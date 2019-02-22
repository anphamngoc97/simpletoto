package com.example.todolistmvp.edittask;

import com.example.todolistmvp.room.model.Task;

import java.util.ArrayList;

public class EditTaskPresenterImpl implements EditTaskContract.Presenter<Task>,
        EditTaskContract.Iterator.OnFinishListener{

    EditTaskContract.Iterator iterator;
    EditTaskContract.View view;

    public EditTaskPresenterImpl(EditTaskContract.Iterator iterator, EditTaskContract.View view) {
        this.iterator = iterator;
        setView(view);
    }

    @Override
    public void setView(EditTaskContract.View view) {
        this.view = view;

    }

    @Override
    public void updateData(Task object) {

    }

    @Override
    public void removeData(Task object) {

    }


    @Override
    public void onRemoveSuccess() {

    }

    @Override
    public void onUpdateSuccess() {

    }

    @Override
    public void onFailture(Throwable throwable) {

    }
}
