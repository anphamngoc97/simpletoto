package com.example.todolistmvp.detailtask;

import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;

public interface DetailTaskContract {
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View>{

    }
}
