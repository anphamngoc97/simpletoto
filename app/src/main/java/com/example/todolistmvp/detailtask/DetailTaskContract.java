package com.example.todolistmvp.detailtask;

import android.content.Context;

import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;

public interface DetailTaskContract {
    interface View extends BaseView{
        void showCateory();
        void showPriority();
        void onSave(String category,String priority);
        void onSelectCategory(String name);
        void onSelectPriority(String name);
    }
    interface Presenter extends BasePresenter<View>{
        void categoryClick();
        void priorityClick();
        void saveClick();
        void onMenuSelect(Context context, int id);
    }
}
