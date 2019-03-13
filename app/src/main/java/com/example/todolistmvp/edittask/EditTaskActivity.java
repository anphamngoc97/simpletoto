package com.example.todolistmvp.edittask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.todolistmvp.BaseActivity;
import com.example.todolistmvp.R;
import com.example.todolistmvp.detailtask.DetailTaskActivity;
import com.example.todolistmvp.maintask.MainTaskActivity;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;
import com.example.todolistmvp.util.room.ResponsitoryTask;
import com.example.todolistmvp.util.room.model.Task;
import com.example.todolistmvp.util.roomdagger.AppModule;
import com.example.todolistmvp.util.roomdagger.DaggerRoomComponent;
import com.example.todolistmvp.util.roomdagger.RoomComponent;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTaskActivity extends BaseActivity implements EditTaskContract.View {

    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.btnSearch)
    ImageButton btnRemove;
    @BindView(R.id.inputLayoutTitle)
    TextInputLayout layoutTitle;
    @BindView(R.id.editTitle)
    TextInputEditText editTitle;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    @BindView(R.id.btnUpdateShowKeyBoard)
    Button btnUpdateShowKeyBoard;
    @BindView(R.id.switchReminder)
    Switch switchReminder;
    @BindView(R.id.groupSwitch)
    ViewGroup groupSwitchReminder;
    @BindView(R.id.groupTime)
    ViewGroup groupTime;
    @BindView(R.id.btnDate)
    Button btnDate;
    @BindView(R.id.btnTime)
    Button btnTime;
    @BindView(R.id.parentLayout)
    ViewGroup parentLayout;
    @BindView(R.id.btnAddDetail)
    ImageButton btnAddDetail;


    int BACKGROUND_INPUT_INVALID;
    int BACKGROUND_INPUT_VALID;

    @Inject
    ResponsitoryTask responsitoryTask;
    EditTaskContract.Presenter presenter;

    boolean isKeyboardShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);

        init();
        setupView();
    }


    private void init() {
        BACKGROUND_INPUT_INVALID = getResources().getColor(R.color.colorInputInvalid);
        BACKGROUND_INPUT_VALID = getResources().getColor(R.color.colorInputValid);

        RoomComponent roomComponent = DaggerRoomComponent.builder()
                .appModule(new AppModule(getApplication()))
                .build();

        roomComponent.inject(this);
        presenter = new EditTaskPresenterImpl(getApplicationContext(),getIntent(),
                this, new EditTaskIteratorImpl(responsitoryTask));

    }

    @Override
    public void initView(String title,boolean isAlarm,String date,String time) {

        editTitle.setText(title);
        switchReminder.setChecked(isAlarm);
        if(date!=null && date.length()>0){
            btnDate.setText(date);
        }
        if(time!=null && time.length()>0){
            btnTime.setText(time);
        }
    }

    private void setupView() {
        btnAddDetail.setOnClickListener(v->{
            presenter.detailTaskClick();
        });
        layoutTitle.setBoxStrokeColor(BACKGROUND_INPUT_VALID);

        editTitle.requestFocus();
        showSoftKeyboard();
        Showlog.d("handler show soft keyboard");
//        new Handler().post(() -> {
//            editTitle.requestFocus();
//            showSoftKeyboard();
//            Showlog.d("handler show soft keyboard");
//        });

        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutTitle.getBoxStrokeColor() != BACKGROUND_INPUT_VALID) {
                    layoutTitle.setBoxStrokeColor(BACKGROUND_INPUT_VALID);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideSoftKeyboard();
                    return true;
                }

                return false;
            }
        });

        btnBack.setOnClickListener(v -> {
            if (isKeyboardShowing) {
                hideSoftKeyboard();
            } else {
                finish();
            }
        });
        btnRemove.setOnClickListener(v -> {
            presenter.removeClick();
        });

        btnUpdate.setOnClickListener(v -> onClickBtnUpdate());
        btnUpdateShowKeyBoard.setOnClickListener(v -> onClickBtnUpdate());

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                groupTime.setVisibility(View.VISIBLE);
            } else {
                groupTime.setVisibility(View.INVISIBLE);
            }
        });

        groupSwitchReminder.setOnClickListener(v -> {
            switchReminder.setChecked(!switchReminder.isChecked());
        });
        btnDate.setOnClickListener(v -> {
            presenter.dateClick();
        });
        btnTime.setOnClickListener(v -> {
            presenter.timeClick();
        });
    }

    @Override
    public void navigateDetailTask(Bundle bundle) {

        Intent intent = new Intent(this, DetailTaskActivity.class);

        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_PREVIOUS_CLASS.getValue(),
                this.getClass());
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_BUNDLE_DETAIL_TASK.getValue(),bundle);

        startActivityForResult(intent,
                Constant.ChildConstantNumber.REQUEST_CODE_ADD_DETAIL_TASK.getValue());
    }

    private void onClickBtnUpdate() {
        String title = editTitle.getText().toString().trim();
        boolean isCheck = switchReminder.isChecked();
        presenter.addTaskClick(title,isCheck);
    }

    @Override
    public void showDateDialog() {
        showDatePicker();
    }

    @Override
    public void showTimeDialog() {
        showTimePicker();
    }

    @Override
    public void onUpdateTaskClick(Task task) {
        presenter.updateData(task);
        Showlog.d("edit update: " + task.tag+"_"+task.typeList);
    }

    @Override
    public void onRemoveClick() {
        showConfirmRemove();
    }

    @Override
    public void onUpdateSuccess(int position,Task task) {

        Intent intent = new Intent(this, MainTaskActivity.class);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_IS_REMOVE.getValue(), Constant.EDIT);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_POSITION.getValue(), position);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_OBJECT.getValue(), task);

        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    @Override
    public void onRemoveSuccess(int position) {

        Intent intent = new Intent(this, MainTaskActivity.class);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_IS_REMOVE.getValue(), Constant.REMOVE);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_EDIT_TASK_POSITION.getValue(), position);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void showErrorInput() {
        layoutTitle.setBoxStrokeColor(BACKGROUND_INPUT_INVALID);
        editTitle.setError(getResources().getString(R.string.errorEmptyTitle));
    }

    @Override
    public void showErrorReminder() {
        Snackbar.make(parentLayout, getResources().getString(R.string.contentNotSetTime),
                Snackbar.LENGTH_SHORT).show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) ->
                {
                    String mDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    btnDate.setText(mDate);
//                    mYear = year;
//                    mMonth = month;
//                    mDay = dayOfMonth;

                    presenter.onCompletePickDate(mDate,year,month,dayOfMonth);

                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();

    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String mTime = hourOfDay + ":" + minute;
                    btnTime.setText(mTime);

                    //mHour = hourOfDay;
                    //mMinute = minute;

                    presenter.onCompletePickTime(mTime,hourOfDay,minute);
                },
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    private void showConfirmRemove() {

        AlertDialog.Builder alertDBuilder = new AlertDialog.Builder(this);
        alertDBuilder.setTitle(getString(R.string.title_confirm_dialog));
        alertDBuilder.setMessage(getString(R.string.content_confirm_remove_dialog));
        alertDBuilder.setNegativeButton(getString(R.string.confirm_no), (dialog, which) -> {

        });
        alertDBuilder.setPositiveButton(getString(R.string.confirm_yes), (dialog, which) -> {
            presenter.removeData();
        });
        alertDBuilder.create().show();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (editTitle.isFocused()) {
                Rect outRect = new Rect();
                editTitle.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    editTitle.setFocusableInTouchMode(false);
                    editTitle.clearFocus();
                    editTitle.setFocusableInTouchMode(true);
                    hideSoftKeyboard();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onShowKeyBoard(int keyboardHeight) {
        super.onShowKeyBoard(keyboardHeight);
        btnUpdateShowKeyBoard.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.INVISIBLE);
        isKeyboardShowing = true;
        Showlog.d("onshowwkeyboard");
    }

    @Override
    public void onHideKeyboard() {
        super.onHideKeyboard();
        btnUpdateShowKeyBoard.setVisibility(View.INVISIBLE);
        btnUpdate.setVisibility(View.VISIBLE);
        isKeyboardShowing = false;
        Showlog.d("onhide");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.ChildConstantNumber.REQUEST_CODE_ADD_DETAIL_TASK.getValue()) {
                presenter.onReceiveResult(data);
            }
        }
    }
}
