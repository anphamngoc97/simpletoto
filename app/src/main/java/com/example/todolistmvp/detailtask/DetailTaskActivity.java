package com.example.todolistmvp.detailtask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.todolistmvp.R;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailTaskActivity extends AppCompatActivity implements DetailTaskContract.View, PopupMenu.OnMenuItemClickListener {

    DetailTaskContract.Presenter presenter;
    @BindView(R.id.editDetaiTask)
    EditText editDetaiTask;
    @BindView(R.id.btnPriority)
    Button btnPriority;
    @BindView(R.id.btnCategory)
    Button btnCategory;
    @BindView(R.id.btnSave)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        ButterKnife.bind(this);

        init();
        onClick();
    }

    private void init() {
        presenter = new DetailTaskPresenterImpl(getIntent(),this);
    }

    private void onClick() {
        btnCategory.setOnClickListener(v -> {
            presenter.categoryClick();
        });
        btnPriority.setOnClickListener(v -> {
            presenter.priorityClick();
        });

        btnSave.setOnClickListener(v -> {
            presenter.saveClick();
        });

    }

    @Override
    public void showCateory() {
        PopupMenu menuCategory = new PopupMenu(this, btnCategory);
        menuCategory.inflate(R.menu.menu_category);
        menuCategory.setOnMenuItemClickListener(this);
        menuCategory.show();
    }

    @Override
    public void showPriority() {
        PopupMenu menuPriority = new PopupMenu(this, btnPriority);
        menuPriority.inflate(R.menu.menu_priority);
        menuPriority.setOnMenuItemClickListener(this);
        menuPriority.show();
    }

    @Override
    public void onSelectCategory(String name) {
        btnCategory.setText(name);
    }

    @Override
    public void onSelectPriority(String name) {
        btnPriority.setText(name);
    }

    @Override
    public void onSave(String category,String priority) {
        Intent intent = new Intent();
        String detail = editDetaiTask.getText().toString();

        Showlog.d("detail: " + detail.length());

        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_TASK_DETAIL.getValue(), detail);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_TASK_CATEGORY.getValue(),
                category);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_TASK_PRIORITY.getValue(),
                priority);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        presenter.onMenuSelect(this,item.getItemId());
        return true;
    }
}
