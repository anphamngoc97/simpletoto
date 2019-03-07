package com.example.todolistmvp.detailtask;

public class DetailTaskPresenterImpl implements DetailTaskContract.Presenter {
    DetailTaskContract.View view;

    public DetailTaskPresenterImpl(DetailTaskContract.View view) {
        setView(view);
    }

    @Override
    public void setView(DetailTaskContract.View view) {
        this.view = view;
    }
}
