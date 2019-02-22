package com.example.todolistmvp.edittask;

import com.example.todolistmvp.BaseIterator;
import com.example.todolistmvp.BasePresenter;
import com.example.todolistmvp.BaseView;
import com.example.todolistmvp.room.model.Task;

public interface EditTaskContract {
    interface View extends BaseView{

    }
    interface Presenter<sT> extends BasePresenter<View>{
        void updateData(sT object);

        void removeData(sT object);
    }
    interface Iterator<sT> extends BaseIterator{
        interface OnFinishListener extends BaseIterator.OnFinishListener{
            void onRemoveSuccess();

            void onUpdateSuccess();

        }
        void updateData(sT object, OnFinishListener onFinishListener);

        void removeData(sT object, OnFinishListener onFinishListener);
    }
}
