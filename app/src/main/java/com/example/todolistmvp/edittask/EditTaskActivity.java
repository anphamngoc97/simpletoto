package com.example.todolistmvp.edittask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.todolistmvp.BaseActivity;
import com.example.todolistmvp.R;
import com.example.todolistmvp.alarm.AlarmUtil;
import com.example.todolistmvp.maintask.MainTaskActivity;
import com.example.todolistmvp.room.ResponsitoryTask;
import com.example.todolistmvp.room.model.Task;
import com.example.todolistmvp.roomdagger.AppModule;
import com.example.todolistmvp.roomdagger.DaggerRoomComponent;
import com.example.todolistmvp.roomdagger.RoomComponent;
import com.example.todolistmvp.util.CommonFuntion;
import com.example.todolistmvp.util.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTaskActivity extends BaseActivity implements EditTaskContract.View{

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

    int BACKGROUND_INPUT_INVALID;
    int BACKGROUND_INPUT_VALID;

    @Inject
    ResponsitoryTask responsitoryTask;
    EditTaskContract.Presenter presenter;

    Task mTask;
    int mPosition;

    String mDate ="";
    String mTime ="";
    int mYear=-1,mMonth=-1,mDay=-1,mMinute=-1,mHour=-1;

    boolean isKeyboardShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);

        getData();
        init();
        setupView();
        updateView();
    }

    private void getData(){
        Intent intent = getIntent();
        mTask = (Task) intent
                .getSerializableExtra(Constant.ChildConstantString.KEY_SEND_EXTRA_EDIT_TASK_OBJECT.getValue());
        mPosition = intent
                .getIntExtra(Constant.ChildConstantString.KEY_SEND_EXTRA_EDIT_TASK_POSITION.getValue(),0);
        Calendar calendar = CommonFuntion.getDateFromString(mTask.dateAlarm);
        if(calendar!=null){
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DATE);
            mHour = calendar.get(Calendar.HOUR);
            mMinute = calendar.get(Calendar.MINUTE);
        }

    }
    private void init() {
        BACKGROUND_INPUT_INVALID = getResources().getColor(R.color.inputInvalid);
        BACKGROUND_INPUT_VALID = getResources().getColor(R.color.inputValid);

        RoomComponent roomComponent = DaggerRoomComponent.builder()
                .appModule(new AppModule(getApplication()))
                .build();

        roomComponent.inject(this);
        presenter = new EditTaskPresenterImpl(this,new EditTaskIteratorImpl(responsitoryTask));

    }
    private void updateView(){

        editTitle.setText(mTask.title);
        switchReminder.setChecked(mTask.isAlarm);
        Calendar calendar = CommonFuntion.getDateFromString(mTask.dateAlarm);

        if(calendar!=null && mTask.isAlarm) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            btnDate.setText(simpleDateFormat.format(calendar.getTime()));

            simpleDateFormat.applyPattern("hh:mm");
            btnTime.setText(simpleDateFormat.format(calendar.getTime()));

        }

    }
    private void setupView() {
        layoutTitle.setBoxStrokeColor(BACKGROUND_INPUT_VALID);

        new Handler().post(() -> {
            editTitle.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Service.INPUT_METHOD_SERVICE);

            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        });

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

                if(actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Service.INPUT_METHOD_SERVICE);

                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                    return true;
                }

                return false;
            }
        });

        btnBack.setOnClickListener(v -> {
            if(isKeyboardShowing){
                hideSoftKeyboard();
            }else {
                finish();
            }
        });
        btnRemove.setOnClickListener(v->{
            confirmRemove();
        });

        btnUpdate.setOnClickListener(v -> onClickBtnUpdate());
        btnUpdateShowKeyBoard.setOnClickListener(v->onClickBtnUpdate());

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                groupTime.setVisibility(View.VISIBLE);
            } else {
                groupTime.setVisibility(View.INVISIBLE);
            }
        });

        groupSwitchReminder.setOnClickListener(v->{
            switchReminder.setChecked(!switchReminder.isChecked());
        });
        btnDate.setOnClickListener(v -> {
            showDatePicker();
        });
        btnTime.setOnClickListener(v -> {
            showTimePicker();
        });
    }

    private void onClickBtnUpdate(){
        {

            String title = editTitle.getText().toString().trim();
            if (title.length() == 0) {
                layoutTitle.setBoxStrokeColor(BACKGROUND_INPUT_INVALID);
                editTitle.setError("Input is empty");

            }else{

                String timeString =null;
                if (!switchReminder.isChecked() || (switchReminder.isChecked() && mYear * mMonth * mDay * mDay * mMinute >=0)) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(mYear, mMonth, mDay, mHour, mMinute);


                    timeString = simpleDateFormat.format(calendar.getTime());

                    if(!switchReminder.isChecked()){
                        timeString = null;
                    }

                    mTask.isAlarm = switchReminder.isChecked();
                    mTask.dateAlarm = timeString;
                    mTask.title = title;
                    presenter.updateData(mTask);


                }else{
                    Snackbar.make(parentLayout,"Timehavenotset",Snackbar.LENGTH_SHORT).show();
                }

            }
        }
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) ->
                {
                    mDate = dayOfMonth + "/" + (month+1) + "/" + year;
                    btnDate.setText(mDate);
                    mYear = year;
                    mMonth = month;
                    mDay = dayOfMonth;

                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();

    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    mTime = hourOfDay + ":" + minute;
                    btnTime.setText(mTime);

                    mHour = hourOfDay;
                    mMinute = minute;
                },
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    private void confirmRemove(){

        AlertDialog.Builder alertDBuilder = new AlertDialog.Builder(this);
        alertDBuilder.setTitle(getString(R.string.title_confirm_dialog));
        alertDBuilder.setMessage(getString(R.string.content_confirm_remove_dialog));
        alertDBuilder.setNegativeButton(getString(R.string.confirm_no), (dialog, which) -> {

        });
        alertDBuilder.setPositiveButton(getString(R.string.confirm_yes), (dialog, which) -> {
            presenter.removeData(mTask);
        });
        alertDBuilder.create().show();
    }
    @Override
    public void onRemoveSuccess() {
        AlarmUtil.cancelAlarm(getApplicationContext(),mTask);

        Intent intent = new Intent(this, MainTaskActivity.class);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_IS_REMOVE.getValue(),Constant.REMOVE);
        intent.putExtra(Constant.ChildConstantString.KEY_SEND_EXTRA_EDIT_TASK_POSITION.getValue(),mPosition);

        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onUpdateSuccess() {
        AlarmUtil.cancelAlarm(getApplicationContext(),mTask);
        AlarmUtil.addAlarm(getApplicationContext(),mTask);

        Intent intent = new Intent(this,MainTaskActivity.class);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_IS_REMOVE.getValue(),Constant.EDIT);
        intent.putExtra(Constant.ChildConstantString.KEY_SEND_EXTRA_EDIT_TASK_POSITION.getValue(),mPosition);
        intent.putExtra(Constant.ChildConstantString.KEY_SEND_EXTRA_EDIT_TASK_OBJECT.getValue(),mTask);

        setResult(Activity.RESULT_OK,intent);
        finish();

    }

    @Override
    public void onShowKeyBoard(int keyboardHeight) {
        super.onShowKeyBoard(keyboardHeight);
        btnUpdateShowKeyBoard.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.INVISIBLE);
        isKeyboardShowing = true;
    }

    @Override
    public void onHideKeyboard() {
        super.onHideKeyboard();
        btnUpdateShowKeyBoard.setVisibility(View.INVISIBLE);
        btnUpdate.setVisibility(View.VISIBLE);
        isKeyboardShowing = false;
    }
}
