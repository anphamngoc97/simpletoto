package com.example.todolistmvp.maintask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todolistmvp.R;
import com.example.todolistmvp.addtask.AddTaskActivity;
import com.example.todolistmvp.edittask.EditTaskActivity;
import com.example.todolistmvp.util.helper.SimplerTouchHelperCallback;
import com.example.todolistmvp.util.room.ResponsitoryTask;
import com.example.todolistmvp.util.room.model.Task;
import com.example.todolistmvp.util.roomdagger.AppModule;
import com.example.todolistmvp.util.roomdagger.DaggerRoomComponent;
import com.example.todolistmvp.util.roomdagger.RoomComponent;
import com.example.todolistmvp.search.SearchActivity;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainTaskActivity extends AppCompatActivity implements MainTaskContract.View {

    @BindView(R.id.recycleTask)
    RecyclerView recycleTask;
    @BindView(R.id.floatBtnAdd)
    FloatingActionButton floatBtnAdd;
    @BindView(R.id.btnSearch)
    ImageButton btnSearch;
    @BindView(R.id.txtvContentMain)
    TextView txtvContentMain;

//    List<Task> mTasks = new ArrayList<>();
    TaskAdapter mTaskAdapter;

    @Inject
    ResponsitoryTask mResponsitoryTask;

    MainTaskContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);
        ButterKnife.bind(this);

        init();
        setUpRecyclerView();
        onClick();

        loadData();
    }

    private void init() {
        RoomComponent component = DaggerRoomComponent.builder().appModule(new AppModule(getApplication()))
                .build();
        component.inject(this);
        mPresenter = new MainTaskPresenterImpl(this, new MainTaskIteratorImpl(mResponsitoryTask));


    }

    private void setUpRecyclerView(){
        mTaskAdapter = new TaskAdapter(getApplicationContext());
        recycleTask.setAdapter(mTaskAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recycleTask.getContext());
        recycleTask.setLayoutManager(layoutManager);

        SimplerTouchHelperCallback simplerTouchHelperCallback =
                new SimplerTouchHelperCallback(mTaskAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simplerTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recycleTask);

        mTaskAdapter.setOnItemClick(new TaskAdapter.OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(int position,Task task) {
                mPresenter.onClickItem(position,task);
            }

            @Override
            public void onCheckBoxChange(int position,Task task,boolean checked) {
                mPresenter.updateCompleteData(position,task,checked);
            }
        });
    }

    private void loadData() {
        mPresenter.loadData();
    }

    private void onClick() {
        floatBtnAdd.setOnClickListener(v -> {
            mPresenter.floatBtnAddClick();
        });
        btnSearch.setOnClickListener(v -> {
            mPresenter.searchClick();
        });
    }

    @Override
    public void navigateAddTask() {
        startActivityForResult(new Intent(this, AddTaskActivity.class),
                Constant.ChildConstantNumber.REQUEST_CODE_ADD_TASK.getValue());

        overridePendingTransition(R.anim.anim_float_button, R.anim.anim_out);

    }

    @Override
    public void navigateSearch() {
        startActivityForResult(new Intent(this, SearchActivity.class),
                Constant.ChildConstantNumber.REQUEST_CODE_SEARCH.getValue());

    }

    @Override
    public void refreshData(List<Task> tasks) {
        mTaskAdapter.insert(tasks);
        mTaskAdapter.notifyDataSetChanged();

        Showlog.d("refresh data: " + tasks.size());
        showMainContent(tasks.size());
    }

    @Override
    public void onUpdateSuccess(int position,Task task) {
        mTaskAdapter.update(position,task);
    }

    @Override
    public void onInsertSuccess(Task task) {
        mTaskAdapter.insert(task);
    }

    @Override
    public void onRemoveSuccess(int position) {
        mTaskAdapter.remove(position);
        showMainContent(mTaskAdapter.getItemCount());
    }

    private void showMainContent(int sizeOfList) {

        if (sizeOfList != 0 && txtvContentMain.getVisibility() == View.VISIBLE) {
            txtvContentMain.setVisibility(View.INVISIBLE);
        } else if (sizeOfList == 0 && txtvContentMain.getVisibility() == View.INVISIBLE) {
            txtvContentMain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void navigateEditTask(int position,Task task) {

        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_OBJECT.getValue(),
                task);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_POSITION.getValue(),
                position);


        startActivityForResult(intent, Constant.ChildConstantNumber.REQUEST_CODE_EDIT_TASK.getValue());

    }

    //todo fix mvc->mvp
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Constant.ChildConstantNumber.REQUEST_CODE_ADD_TASK.getValue()) {
                Task task = (Task) data
                        .getSerializableExtra(Constant.ChildConstantString.KEY_EXTRA_ADD_TASK.getValue());
                if (task != null) {

                    mPresenter.onInsertData(task);
//                    this.mTasks.add(task);
//                    mTaskAdapter.notifyDataSetChanged();
//
//                    showMainContent(this.mTasks.size());
                }
            }
            if (requestCode == Constant.ChildConstantNumber.REQUEST_CODE_EDIT_TASK.getValue()) {

                boolean isRemove = data.
                        getBooleanExtra(Constant.ChildConstantString.KEY_EXTRA_IS_REMOVE.getValue(),
                                true);

                int position = data
                        .getIntExtra(Constant.ChildConstantString
                                        .KEY_EXTRA_EDIT_TASK_POSITION.getValue(),
                                0);


                if (isRemove) {
                    mPresenter.onRemoveData(position);
//                    mTasks.remove(position);
//                    mTaskAdapter.notifyItemRemoved(position);
//
//                    showMainContent(mTasks.size());

                } else {
                    Task task = (Task) data.getSerializableExtra(
                            Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_OBJECT.getValue());
                    mPresenter.updateData(position,task);

//                    mTasks.set(position, task);
//                    mTaskAdapter.notifyItemChanged(position);
                }
            }
            if (requestCode == Constant.ChildConstantNumber.REQUEST_CODE_SEARCH.getValue()) {
                mPresenter.loadData();
            }
        }
    }
}
