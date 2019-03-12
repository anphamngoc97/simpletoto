package com.example.todolistmvp.maintask;

import com.example.todolistmvp.util.room.ResponsitoryTask;
import com.example.todolistmvp.util.room.model.Task;
import com.example.todolistmvp.util.Iterator;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class MainTaskIteratorImpl implements MainTaskContract.Iterator {
    ResponsitoryTask mResponsitoryTask;

    public MainTaskIteratorImpl(ResponsitoryTask responsitoryTask) {
        this.mResponsitoryTask = responsitoryTask;
    }

    @Override
    public void updateData(int position,Task object, OnFinishListener onFinishListener) {

            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    mResponsitoryTask.editTask(object.id,object.title,object.dateAlarm,
                            object.isComplete,object.isAlarm, object.subTask,object.typeList,object.tag);
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
                            onFinishListener.onUpdateSuccess(position,object);
                        }

                        @Override
                        public void onError(Throwable e) {
                            onFinishListener.onFailture(e);

                        }
                    });

    }

    @Override
    public void insertData(Task object, OnFinishListener onFinishListener) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mResponsitoryTask.insertTask(object);
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
                        onFinishListener.onInsertSuccess(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onFinishListener.onFailture(e);

                    }
                });
    }

    @Override
    public void getData(OnFinishListener onFinishListener) {
        Iterator.getAllTask(mResponsitoryTask,onFinishListener);
    }
}
