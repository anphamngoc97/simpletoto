package com.example.todolistmvp.search;

import com.example.todolistmvp.room.ResponsitoryTask;
import com.example.todolistmvp.util.Iterator;

public class SearchIteratorImpl implements SearchContract.Iterator{
    ResponsitoryTask responsitoryTask;

    public SearchIteratorImpl(ResponsitoryTask responsitoryTask) {
        this.responsitoryTask = responsitoryTask;
    }


    public void getData(OnFinishListener onFinishListener) {
        Iterator.getAllTaskBySearch(responsitoryTask,onFinishListener);
    }
}
