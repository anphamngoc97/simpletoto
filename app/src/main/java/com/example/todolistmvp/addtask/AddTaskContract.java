package com.example.todolistmvp.addtask;

import com.example.todolistmvp.BaseIterator;
import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;
import com.example.todolistmvp.room.model.Task;

public interface AddTaskContract {
    interface View extends BaseView {
        void insertComplete();
    }
    interface Presenter extends BasePresenter<View>{
        void insertData(Task object);
    }
    interface Iterator extends BaseIterator{
        interface OnFinishListener extends BaseIterator.OnFinishListener{
            void onInsertSuccess(Task object);
        }
        void insertData(Task object, OnFinishListener onFinishListener);
    }
}
