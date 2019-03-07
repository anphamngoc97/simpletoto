package com.example.todolistmvp.edittask;

import com.example.todolistmvp.util.room.ResponsitoryTask;
import com.example.todolistmvp.util.room.model.Task;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class EditTaskIteratorImpl implements EditTaskContract.Iterator {
    ResponsitoryTask responsitoryTask;

    public EditTaskIteratorImpl(ResponsitoryTask responsitoryTask) {
        this.responsitoryTask = responsitoryTask;
    }

    @Override
    public void updateData(Task object, OnFinishListener onFinishListener) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                responsitoryTask.editTask(object.id,object.title,object.dateAlarm,object.isComplete,object.isAlarm);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        onFinishListener.onUpdateSuccess();

                    }

                    @Override
                    public void onError(Throwable e) {
                        onFinishListener.onFailture(e);

                    }
                });
    }

    @Override
    public void removeData(Task object, OnFinishListener onFinishListener) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                responsitoryTask.removeTask(object);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        onFinishListener.onRemoveSuccess();

                    }

                    @Override
                    public void onError(Throwable e) {
                        onFinishListener.onFailture(e);

                    }
                });
    }
}
