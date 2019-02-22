package com.example.todolistmvp.addtask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.todolistmvp.R;
import com.example.todolistmvp.alarm.AlarmUtil;
import com.example.todolistmvp.room.ResponsitoryTask;
import com.example.todolistmvp.room.model.Task;
import com.example.todolistmvp.roomdagger.AppModule;
import com.example.todolistmvp.roomdagger.DaggerRoomComponent;
import com.example.todolistmvp.roomdagger.RoomComponent;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTaskActivity extends AppCompatActivity implements AddTaskContract.View {

    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.inputLayoutTitle)
    TextInputLayout layoutTitle;
    @BindView(R.id.editTitle)
    TextInputEditText editTitle;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.switchReminder)
    Switch switchReminder;
    @BindView(R.id.groupTime)
    ViewGroup groupTime;
    @BindView(R.id.btnDate)
    Button btnDate;
    @BindView(R.id.btnTime)
    Button btnTime;
    @BindView(R.id.parentLayout)
            View parentLayout;

    int BACKGROUND_INPUT_INVALID = Color.parseColor("#F53D25");
    int BACKGROUND_INPUT_VALID = Color.parseColor("#5EEE1B");

    @Inject
    ResponsitoryTask responsitoryTask;
    AddTaskContract.Presenter presenter;

    Task mTask;

    String mDate ="";
    String mTime ="";
    int mYear=0,mMonth=0,mDay=0,mMinute=0,mHour=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        init();
        setupView();
    }

    private void init() {

        RoomComponent roomComponent = DaggerRoomComponent.builder()
                .appModule(new AppModule(getApplication()))
                .build();

        roomComponent.inject(this);
        presenter = new AddTaskPresenterImpl(this,new AddTaskIteratorImpl(responsitoryTask));

    }

    private void setupView() {
        layoutTitle.setBoxStrokeColor(BACKGROUND_INPUT_VALID);
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

        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnAdd.setOnClickListener(v -> {

            String title = editTitle.getText().toString().trim();
            if (title.length() == 0) {
                layoutTitle.setBoxStrokeColor(BACKGROUND_INPUT_INVALID);
                editTitle.setError("Input is empty");

            }else{


                String timeString =null;
                if( switchReminder.isChecked() && mYear*mMonth*mDay*mDay*mMinute!=0) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(mYear, mMonth, mDay, mHour, mMinute);
                    timeString = simpleDateFormat.format(calendar.getTime());

                }else{
                    Snackbar.make(parentLayout,"Timehavenotset",Snackbar.LENGTH_SHORT).show();
                }
                mTask = new Task.Builder().setIsAlarm(switchReminder.isChecked())
                        .setDateAlarm(timeString)
                        .createTask(title);
                presenter.insertData(mTask);

            }
        });
        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                groupTime.setVisibility(View.VISIBLE);
            } else {
                groupTime.setVisibility(View.INVISIBLE);
            }
        });

        btnDate.setOnClickListener(v -> {
            showDatePicker();
        });
        btnTime.setOnClickListener(v -> {
            showTimePicker();
        });
    }

    @Override
    public void insertComplete() {
        Showlog.d("insert complete");
        AlarmUtil.addAlarm(getApplicationContext(),mTask);
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_REQUEST_ADD_TASK,mTask);
        setResult(Activity.RESULT_OK,intent);

        finish();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) ->
                {
                    Showlog.d(year + "_" + month + "_" + dayOfMonth);
                    mDate = dayOfMonth + "/" + month + "/" + year;
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
                    Showlog.d(hourOfDay + "_" + minute);
                    mTime = hourOfDay + ":" + minute;
                    btnTime.setText(mTime);

                    mHour = hourOfDay;
                    mMinute = minute;
                },
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

}
