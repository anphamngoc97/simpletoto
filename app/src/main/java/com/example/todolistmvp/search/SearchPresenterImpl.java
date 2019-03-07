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
    public void onClickItem(int position) {
        view.navigateEditTask(position);
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
