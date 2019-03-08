package com.example.todolistmvp.detailtask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.todolistmvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailTaskActivity extends AppCompatActivity implements DetailTaskContract.View{

    DetailTaskContract.Presenter presenter;
    @BindView(R.id.editDetaiTask)
    EditText editDetaiTask;
    @BindView(R.id.btnPriority)
    Button btnPriority;
    @BindView(R.id.btnCategory)
    Button btnCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        ButterKnife.bind(this);

        init();
        //onClick();
    }

    private void init(){
        presenter=new DetailTaskPresenterImpl(this);
        registerForContextMenu(btnCategory);
    }
    private void onClick(){
        btnCategory.setOnClickListener(v->{
            registerForContextMenu(btnCategory);
        });
        btnPriority.setOnClickListener(v->{
            registerForContextMenu(btnPriority);
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_category,menu);
//
//        switch (v.getId()){
//            case R.id.btnCategory:{
//                getMenuInflater().inflate(R.menu.menu_category,menu);
//                break;
//            }
//            case R.id.btnPriority:{
//                getMenuInflater().inflate(R.menu.menu_priority,menu);
//                break;
//            }
//        }
    }
}
