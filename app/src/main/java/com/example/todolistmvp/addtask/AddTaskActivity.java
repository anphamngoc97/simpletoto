package com.example.todolistmvp.addtask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.todolistmvp.R;

public class AddTaskActivity extends AppCompatActivity implements AddTaskContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    @Override
    public void insertComplete() {

    }
}
