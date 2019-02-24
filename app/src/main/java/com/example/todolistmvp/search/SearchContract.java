package com.example.todolistmvp.search;

import com.example.todolistmvp.BaseIterator;
import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;
import com.example.todolistmvp.room.model.Task;

import java.util.List;

public interface SearchContract {
    interface View extends BaseView{
        void refreshData(List<Task> tasks);
        void navigateEditTask(int position);
    }
    interface Presenter extends BasePresenter<View>{
        void loadData();
        void onClickItem(int position);
    }
    interface Iterator extends BaseIterator{
        interface OnFinishListener extends BaseIterator.OnFinishListener{
            void onGetSuccess(List<Task> res);
        }
        void getData(OnFinishListener onFinishListener);
    }
}
