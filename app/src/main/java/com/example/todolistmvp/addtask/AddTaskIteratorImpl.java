package com.example.todolistmvp.addtask;

import com.example.todolistmvp.room.ResponsitoryTask;
import com.example.todolistmvp.room.model.Task;
import com.example.todolistmvp.util.Iterator;

public class AddTaskIteratorImpl implements AddTaskContract.Iterator {
    ResponsitoryTask mResponsitoryTask;


    public AddTaskIteratorImpl(ResponsitoryTask responsitoryTask) {
        this.mResponsitoryTask = responsitoryTask;
    }


    @Override
    public void insertData(Task object, OnFinishListener onFinishListener) {
        Iterator.insertTask(object, mResponsitoryTask,onFinishListener);
    }


}
