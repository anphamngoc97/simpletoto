package com.example.todolistmvp.util;


import com.example.todolistmvp.addtask.AddTaskContract;
import com.example.todolistmvp.edittask.EditTaskContract;
import com.example.todolistmvp.maintask.MainTaskContract;
import com.example.todolistmvp.util.room.ResponsitoryTask;
import com.example.todolistmvp.util.room.model.Task;
import com.example.todolistmvp.search.SearchContract;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class Iterator {
    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void getAllTask(ResponsitoryTask responsitoryTask,
                                  MainTaskContract.Iterator.OnFinishListener onFinishListener){


         responsitoryTask.getAllTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                 .subscribeWith(new DisposableSubscriber<List<Task>>() {
                     @Override
                     public void onNext(List<Task> tasks) {
                         onFinishListener.onGetSuccess(tasks);
                         dispose();

                     }

                     @Override
                     public void onError(Throwable t) {
                         onFinishListener.onFailture(t);
                         dispose();

                     }

                     @Override
                     public void onComplete() {
                         Showlog.d("dispose");
                         dispose();
                     }
                 });
    }
    public static void getAllTaskBySearch(ResponsitoryTask responsitoryTask,
                                  SearchContract.Iterator.OnFinishListener onFinishListener){


        Disposable disposable = responsitoryTask.getAllTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Task>>() {
                    @Override
                    public void accept(List<Task> tasks) throws Exception {
                        onFinishListener.onGetSuccess(tasks);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onFinishListener.onFailture(throwable );
                    }
                });

        compositeDisposable.add(disposable);

    }
    public static void insertTask(Task task,ResponsitoryTask responsitoryTask,
                                  AddTaskContract.Iterator.OnFinishListener onFinishListener){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                responsitoryTask.insertTask(task);
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
                        onFinishListener.onInsertSuccess(task);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onFinishListener.onFailture(e);

                    }
                });
    }
    public static void updateTask(Task task, ResponsitoryTask responsitoryTask,
                                  EditTaskContract.Iterator.OnFinishListener onFinishListener){

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                responsitoryTask.editTask(
                        task.id,task.title,task.dateAlarm,task.isComplete,task.isAlarm,
                        task.subTask,task.typeList,task.tag);
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
    public static void removeTask(Task task, ResponsitoryTask responsitoryTask,
                                  EditTaskContract.Iterator.OnFinishListener onFinishListener){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                responsitoryTask.removeTask(task);
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
