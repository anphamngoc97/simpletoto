package com.example.todolistmvp.detailtask;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    String categorySelected = "";
    String prioritySelected = "";
    Class prevIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        ButterKnife.bind(this);

        getData();
        init();
        onClick();
    }

    private void getData() {
        Intent intent = getIntent();
        prevIntent = (Class) intent
                .getSerializableExtra(Constant.ChildConstantString.KEY_EXTRA_PREVIOUS_CLASS.getValue());
        Showlog.d(prevIntent.getCanonicalName());
    }

    private void init() {
        presenter = new DetailTaskPresenterImpl(this);

    }

    private void onClick() {
        btnCategory.setOnClickListener(v -> {
            PopupMenu menuCategory = new PopupMenu(this, btnCategory);
            menuCategory.inflate(R.menu.menu_category);
            menuCategory.setOnMenuItemClickListener(this);
            menuCategory.show();
        });
        btnPriority.setOnClickListener(v -> {
            PopupMenu menuPrority = new PopupMenu(this, btnPriority);
            menuPrority.inflate(R.menu.menu_priority);
            menuPrority.setOnMenuItemClickListener(this);
            menuPrority.show();
        });

        btnSave.setOnClickListener(v -> {
            Intent intent = new Intent();
            String detail = editDetaiTask.getText().toString();

            Showlog.d("detail: " + detail.length());

            intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_TASK_DETAIL.getValue(), detail);
            intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_TASK_CATEGORY.getValue(),
                    categorySelected);
            intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_TASK_PRIORITY.getValue(),
                    prioritySelected);

            setResult(Activity.RESULT_OK, intent);
            finish();
        });

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCategoryBusiness: {
                btnCategory.setText(getResources().getString(R.string.categoryBusiness));
                categorySelected = Constant.ChildConstantDetailTaskcategory.BUSINESS.getValue();
                break;
            }
            case R.id.menuCategoryPersonal: {
                btnCategory.setText(getResources().getString(R.string.categoryPersonal));
                categorySelected = Constant.ChildConstantDetailTaskcategory.PERSONAL.getValue();
                break;
            }
            case R.id.menuCategoryNone: {
                btnCategory.setText(getResources().getString(R.string.categoryNone));
                categorySelected = Constant.ChildConstantDetailTaskcategory.NONE.getValue();
                break;
            }
            case R.id.menuPriorityHigh: {
                btnPriority.setText(getResources().getString(R.string.priorityHigh));
                prioritySelected = Constant.ChildConstantDetailTaskPriority.HIGH.getValue();
                break;
            }
            case R.id.menuPriorityMedium: {
                btnPriority.setText(getResources().getString(R.string.priorityMedium));
                prioritySelected = Constant.ChildConstantDetailTaskPriority.MEDIUM.getValue();
                break;
            }
            case R.id.menuPriorityLow: {
                btnPriority.setText(getResources().getString(R.string.priorityLow));
                prioritySelected = Constant.ChildConstantDetailTaskPriority.LOW.getValue();
                break;
            }
            case R.id.menuPriorityNone: {
                btnPriority.setText(getResources().getString(R.string.priorityNone));
                prioritySelected = Constant.ChildConstantDetailTaskPriority.NONE.getValue();
                break;
            }
            default: {
                return false;
            }

        }

        return true;
    }
}
