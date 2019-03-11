package com.example.todolistmvp.search;

import com.example.todolistmvp.BaseIterator;
import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;
import com.example.todolistmvp.util.room.model.Task;

import java.util.List;

public interface SearchContract {
    interface View extends BaseView{
        void refreshData(List<Task> tasks);
        void navigateEditTask(int position,Task task);
        void notifyEditTask(Task task);
        void notifyRemoveTask(int position);
        void onBack();
        void onClear();
        void onFilter(String s);
        void onNonFilter();
    }
    interface Presenter extends BasePresenter<View>{
        void loadData();
        void onClickItem(int position,Task task);
        void editTask(Task task);
        void removeTask(int position);
        void backClick();
        void clearClick();
        void search(String s);
    }
    interface Iterator extends BaseIterator{
        interface OnFinishListener extends BaseIterator.OnFinishListener{
            void onGetSuccess(List<Task> res);
        }
        void getData(OnFinishListener onFinishListener);
    }
}
