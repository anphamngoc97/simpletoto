package com.example.todolistmvp.maintask;

import com.example.todolistmvp.BaseIterator;
import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;
import com.example.todolistmvp.util.room.model.Task;

import java.util.List;

public interface MainTaskContract {
    interface View extends BaseView {
        void refreshData(List<Task> tasks);

        void refreshEditText();

        void onUpdateSuccess(int position,Task task);

        void onInsertSuccess(Task task);

        void onRemoveSuccess(int position);

        void navigateEditTask(int position,Task task);

        void navigateAddTask();

        void navigateSearch();
    }

    interface Presenter extends BasePresenter<View> {
        void loadData();

        void onClickItem(int position,Task object);

        void onClickAdd(String title);

        void updateCompleteData(int position,Task object,boolean checked);

        void updateData(int position,Task object);

        void onInsertData(Task object);

        void onRemoveData(int position);

        void searchClick();

        void floatBtnAddClick();

    }

    interface Iterator extends BaseIterator {
        interface OnFinishListener extends BaseIterator.OnFinishListener {
            void onGetSuccess(List<Task> res);

            void onUpdateSuccess(int position,Task task);

            void onInsertSuccess(Task object);
        }

        void insertData(Task object, OnFinishListener onFinishListener);


        void updateData(int position,Task object, OnFinishListener onFinishListener);

        void getData(OnFinishListener onFinishListener);
    }
}
