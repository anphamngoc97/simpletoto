package com.example.todolistmvp.edittask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.todolistmvp.R;

public class EditTaskActivity extends AppCompatActivity implements EditTaskContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
    }
}
