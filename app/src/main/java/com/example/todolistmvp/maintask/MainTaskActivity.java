package com.example.todolistmvp.maintask;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.todolistmvp.R;
import com.example.todolistmvp.room.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainTaskActivity extends AppCompatActivity implements MainTaskContract.View{

    @BindView(R.id.recycleTask)
    RecyclerView recycleTask;
    @BindView(R.id.floatBtnAdd)
    FloatingActionButton floatBtnAdd;

    List<Task> tasks = new ArrayList<>();
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);
        ButterKnife.bind(this);
    }

    @Override
    public void refreshData(List<Task> tasks) {
        tasks.clear();
        this.tasks.addAll(tasks);
        taskAdapter.notifyDataSetChanged();
    }
}
