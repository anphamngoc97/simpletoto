package com.example.todolistmvp.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todolistmvp.R;
import com.example.todolistmvp.edittask.EditTaskActivity;
import com.example.todolistmvp.util.room.ResponsitoryTask;
import com.example.todolistmvp.util.room.model.Task;
import com.example.todolistmvp.util.roomdagger.AppModule;
import com.example.todolistmvp.util.roomdagger.DaggerRoomComponent;
import com.example.todolistmvp.util.roomdagger.RoomComponent;
import com.example.todolistmvp.util.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements SearchContract.View {

    @BindView(R.id.recycleTask)
    RecyclerView recycleTask;
    @BindView(R.id.btnClear)
    ImageButton btnClear;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.editSearch)
    EditText editSearch;
    @BindView(R.id.txtvContentSearch)
    TextView txtvTitleSearch;

    //List<Task> mTasks = new ArrayList<>();
    SearchAdapter mTaskAdapter;

    @Inject
    ResponsitoryTask mResponsitoryTask;

    SearchContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        init();
        setUpRecyclerView();
        onClick();
        loadData();
        setUpSearch();

    }

    private void init() {
        RoomComponent component = DaggerRoomComponent.builder().appModule(new AppModule(getApplication()))
                .build();
        component.inject(this);
        mPresenter = new SearchPresenterImpl(this, new SearchIteratorImpl(mResponsitoryTask));

    }

    public void setUpRecyclerView(){
        mTaskAdapter = new SearchAdapter(this);
        mTaskAdapter.setOnItemClick((position, task) -> mPresenter.onClickItem(position,task));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recycleTask.getContext());
        recycleTask.setLayoutManager(layoutManager);
        recycleTask.setAdapter(mTaskAdapter);
    }
    private void onClick() {
        btnBack.setOnClickListener(v->{
            mPresenter.backClick();
        });
        btnClear.setOnClickListener(v->{
            mPresenter.clearClick();
        });
    }

    @Override
    public void onBack() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onClear() {
        editSearch.setText("");
    }

    @Override
    public void onFilter(String s) {
        if(txtvTitleSearch.getVisibility() == View.VISIBLE){
            txtvTitleSearch.setVisibility(View.INVISIBLE);
        }
        mTaskAdapter.getFilter().filter(s);
    }

    @Override
    public void onNonFilter() {
        if(txtvTitleSearch.getVisibility() == View.INVISIBLE){
            txtvTitleSearch.setVisibility(View.VISIBLE);
        }
        mTaskAdapter.getFilter().filter("");
    }

    private void setUpSearch(){

        RxSearch.from(editSearch)
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged((s, s2) -> s.compareToIgnoreCase(s2)==0)
                .switchMap((Function<String, ObservableSource<?>>) s -> Observable.just(s))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        mPresenter.search(o.toString());

                        /*
                        mTaskAdapter.getFilter().filter(o.toString());
                        if(o.toString().trim().length()==0){
                            txtvTitleSearch.setVisibility(View.VISIBLE);
                        }else if(txtvTitleSearch.getVisibility() == View.VISIBLE){
                            txtvTitleSearch.setVisibility(View.INVISIBLE);
                        }
                        */
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadData() {
        mPresenter.loadData();
    }


    @Override
    public void refreshData(List<Task> tasks) {
        mTaskAdapter.addData(tasks);
    }

    @Override
    public void notifyEditTask(Task task) {
        mTaskAdapter.changeData(task);
    }

    @Override
    public void notifyRemoveTask(int position) {
        mTaskAdapter.remove(position);
    }

    @Override
    public void navigateEditTask(int position,Task task) {
        editSearch.setText("");

        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_OBJECT.getValue(),
                task);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_POSITION.getValue(),
                position);

        startActivityForResult(intent, Constant.ChildConstantNumber.REQUEST_CODE_EDIT_TASK.getValue());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Constant.ChildConstantNumber.REQUEST_CODE_EDIT_TASK.getValue()) {

                boolean isRemove = data.
                        getBooleanExtra(Constant.ChildConstantString.KEY_EXTRA_IS_REMOVE.getValue(),
                                true);

                int position = data
                        .getIntExtra(Constant.ChildConstantString
                                        .KEY_EXTRA_EDIT_TASK_POSITION.getValue(),
                                0);

                if (isRemove) {
                    //mTasks.remove(position);
                    //mTaskAdapter.notifyItemRemoved(position);
                    mPresenter.removeTask(position);

                } else {
                    Task task = (Task) data.getSerializableExtra(
                            Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_OBJECT.getValue());
                    //mTasks.set(position, task);
                    //mTaskAdapter.notifyItemChanged(position);

                    mPresenter.editTask(task);
                }
            }
        }
    }

}
