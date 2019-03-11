package com.example.todolistmvp.search;

import com.example.todolistmvp.util.room.model.Task;
import com.example.todolistmvp.util.Showlog;

import java.util.List;

public class SearchPresenterImpl implements SearchContract.Presenter,
        SearchContract.Iterator.OnFinishListener {

    SearchContract.View view;
    SearchContract.Iterator iterator;

    public SearchPresenterImpl(SearchContract.View view, SearchContract.Iterator iterator) {
        this.view = view;
        this.iterator = iterator;
    }


    @Override
    public void setView(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void loadData() {
        iterator.getData(this);
    }

    @Override
    public void backClick() {
        view.onBack();
    }

    @Override
    public void clearClick() {
        view.onClear();
    }

    @Override
    public void search(String s) {
        if(s.trim().length()>0){
            view.onFilter(s);
        }else{
            view.onNonFilter();
        }
    }

    @Override
    public void onClickItem(int position,Task task) {
        view.navigateEditTask(position, task);
    }

    @Override
    public void editTask(Task task) {
        view.notifyEditTask(task);
    }

    @Override
    public void removeTask(int position) {
        view.notifyRemoveTask(position);
    }

    @Override
    public void onGetSuccess(List<Task> res) {
        view.refreshData(res);
    }

    @Override
    public void onFailture(Throwable throwable) {
        Showlog.d("maintask mPresenter fail: " + throwable.getMessage());
    }
}
