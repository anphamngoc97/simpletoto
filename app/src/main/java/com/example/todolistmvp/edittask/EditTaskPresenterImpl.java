package com.example.todolistmvp.edittask;

import com.example.todolistmvp.room.model.Task;
import com.example.todolistmvp.util.Showlog;

public class EditTaskPresenterImpl implements EditTaskContract.Presenter,
        EditTaskContract.Iterator.OnFinishListener{

    EditTaskContract.Iterator iterator;
    EditTaskContract.View view;

    public EditTaskPresenterImpl(EditTaskContract.View view,EditTaskContract.Iterator iterator) {
        this.iterator = iterator;
        setView(view);
    }

    @Override
    public void setView(EditTaskContract.View view) {
        this.view = view;

    }

    @Override
    public void updateData(Task object) {
        iterator.updateData(object,this);
    }

    @Override
    public void removeData(Task object) {
        iterator.removeData(object,this);

    }


    @Override
    public void onRemoveSuccess() {
        view.onRemoveSuccess();

    }

    @Override
    public void onUpdateSuccess() {
        view.onUpdateSuccess();

    }

    @Override
    public void onFailture(Throwable throwable) {
        Showlog.d("edittask presenter error: " + throwable.getMessage());
    }
}
