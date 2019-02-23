package com.example.todolistmvp.edittask;

import com.example.todolistmvp.BaseIterator;
import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;
import com.example.todolistmvp.room.model.Task;

public interface EditTaskContract {
    interface View extends BaseView{
        void onRemoveSuccess();

        void onUpdateSuccess();

    }
    interface Presenter extends BasePresenter<View>{
        void updateData(Task object);

        void removeData(Task object);
    }
    interface Iterator extends BaseIterator{
        interface OnFinishListener extends BaseIterator.OnFinishListener{
            void onRemoveSuccess();

            void onUpdateSuccess();

        }
        void updateData(Task object, OnFinishListener onFinishListener);

        void removeData(Task object, OnFinishListener onFinishListener);
    }
}
