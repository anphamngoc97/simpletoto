package com.example.todolistmvp.maintask;

import com.example.todolistmvp.BaseIterator;
import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;
import com.example.todolistmvp.room.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface MainTaskContract {
    interface View extends BaseView{
        void refreshData(List<Task> tasks);
    }
    interface Presenter extends BasePresenter<View>{
        void loadData();
    }
    interface Iterator extends BaseIterator{
        interface OnFinishListener extends BaseIterator.OnFinishListener{
            void onGetSuccess(List<Task> res);
        }
        void getData(OnFinishListener onFinishListener);
    }
}
