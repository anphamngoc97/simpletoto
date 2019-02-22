package com.example.todolistmvp.maintask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.todolistmvp.R;
import com.example.todolistmvp.addtask.AddTaskActivity;
import com.example.todolistmvp.room.ResponsitoryTask;
import com.example.todolistmvp.room.model.Task;
import com.example.todolistmvp.roomdagger.AppModule;
import com.example.todolistmvp.roomdagger.DaggerRoomComponent;
import com.example.todolistmvp.roomdagger.RoomComponent;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainTaskActivity extends AppCompatActivity implements MainTaskContract.View{

    @BindView(R.id.recycleTask)
    RecyclerView recycleTask;
    @BindView(R.id.floatBtnAdd)
    FloatingActionButton floatBtnAdd;

    List<Task> tasks = new ArrayList<>();
    TaskAdapter taskAdapter;

    @Inject
    ResponsitoryTask responsitoryTask;

    MainTaskContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);
        ButterKnife.bind(this);

        init();
        onClick();

        loadData();
    }

    private void init(){
        RoomComponent component = DaggerRoomComponent.builder().appModule(new AppModule(getApplication()))
                .build();
        component.inject(this);
        presenter = new MainTaskPresenterImpl(this,new MainTaskIteratorImpl(responsitoryTask));

        taskAdapter = new TaskAdapter(tasks,presenter,recycleTask);
        recycleTask.setAdapter(taskAdapter);

    }
    private void loadData(){
        presenter.loadData();
    }
    private void onClick(){
        floatBtnAdd.setOnClickListener(v->{
            startActivityForResult(new Intent(this,AddTaskActivity.class),
                    Constant.REQUEST_CODE_ADD_TASK);
        });
    }

    @Override
    public void refreshData(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        taskAdapter.notifyDataSetChanged();
        Showlog.d("load complete: " + tasks.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constant.REQUEST_CODE_ADD_TASK && resultCode == Activity.RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.KEY_REQUEST_ADD_TASK);
            if(task!=null){
                this.tasks.add(task);
                taskAdapter.notifyDataSetChanged();
            }
        }
    }
}
