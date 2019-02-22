package com.example.todolistmvp;

public interface BasePresenter<T> {
    void setView(T view);
}
