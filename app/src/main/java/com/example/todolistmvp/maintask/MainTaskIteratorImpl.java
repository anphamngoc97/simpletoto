package com.example.todolistmvp.maintask;

import com.example.todolistmvp.room.ResponsitoryTask;
import com.example.todolistmvp.util.Iterator;

public class MainTaskIteratorImpl implements MainTaskContract.Iterator {
    ResponsitoryTask responsitoryTask;

    public MainTaskIteratorImpl(ResponsitoryTask responsitoryTask) {
        this.responsitoryTask = responsitoryTask;
    }

    @Override
    public void getData(OnFinishListener onFinishListener) {
        Iterator.getTaskAllTask(responsitoryTask,onFinishListener);
    }
}
