package com.example.todolistmvp.edittask;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.example.todolistmvp.maintask.MainTaskActivity;
import com.example.todolistmvp.room.ResponsitoryTask;
import com.example.todolistmvp.room.model.Task;
import com.example.todolistmvp.roomdagger.AppModule;
import com.example.todolistmvp.roomdagger.DaggerRoomComponent;
import com.example.todolistmvp.roomdagger.RoomComponent;
import com.example.todolistmvp.util.ComonFuntion;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTaskActivity extends AppCompatActivity implements EditTaskContract.View{

    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.btnRemove)
    ImageButton btnRemove;
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
    EditTaskContract.Presenter presenter;

    Task mTask;
    int mPosition;

    String mDate ="";
    String mTime ="";
    int mYear=0,mMonth=0,mDay=0,mMinute=0,mHour=0;


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

    }
    private void init() {

        RoomComponent roomComponent = DaggerRoomComponent.builder()
                .appModule(new AppModule(getApplication()))
                .build();

        roomComponent.inject(this);
        presenter = new EditTaskPresenterImpl(this,new EditTaskIteratorImpl(responsitoryTask));

    }
    private void updateView(){
        editTitle.setText(mTask.title);
        switchReminder.setChecked(mTask.isAlarm);
        Calendar calendar = ComonFuntion.getDateFromString(mTask.dateAlarm);

        if(calendar!=null && mTask.isAlarm) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
            btnDate.setText(simpleDateFormat.format(calendar.getTime()));

            simpleDateFormat.applyPattern("hh:mm");
            btnTime.setText(simpleDateFormat.format(calendar.getTime()));

        }

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
        btnRemove.setOnClickListener(v->{
            confirmRemove();
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

                //todo update task
//                mTask = new Task.Builder().setIsAlarm(switchReminder.isChecked())
//                        .setDateAlarm(timeString)
//                        .createTask(title);
                mTask.isAlarm = switchReminder.isChecked();
                mTask.dateAlarm = timeString;
                mTask.title = title;
                presenter.updateData(mTask);

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

    private void confirmRemove(){

        AlertDialog.Builder alertDBuilder = new AlertDialog.Builder(this);
        alertDBuilder.setTitle(getString(R.string.titleComfirmRDialog));
        alertDBuilder.setMessage(getString(R.string.contentConfirmRemoveDialog));
        alertDBuilder.setNegativeButton(getString(R.string.confirmNo), (dialog, which) -> {

        });
        alertDBuilder.setPositiveButton(getString(R.string.confirmYes), (dialog, which) -> {
            presenter.removeData(mTask);
        });
        alertDBuilder.create().show();
    }
    @Override
    public void onRemoveSuccess() {
        AlarmUtil.cancelAlarm(this,mTask);


        Intent intent = new Intent(this, MainTaskActivity.class);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_IS_REMOVE.getValue(),Constant.REMOVE);
        intent.putExtra(Constant.ChildConstantString.KEY_SEND_EXTRA_EDIT_TASK_POSITION.getValue(),mPosition);

        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onUpdateSuccess() {
        AlarmUtil.addAlarm(this,mTask);

        Intent intent = new Intent(this,MainTaskActivity.class);
        intent.putExtra(Constant.ChildConstantString.KEY_EXTRA_IS_REMOVE.getValue(),Constant.EDIT);
        intent.putExtra(Constant.ChildConstantString.KEY_SEND_EXTRA_EDIT_TASK_POSITION.getValue(),mPosition);
        intent.putExtra(Constant.ChildConstantString.KEY_SEND_EXTRA_EDIT_TASK_OBJECT.getValue(),mTask);

        setResult(Activity.RESULT_OK,intent);
        finish();

    }
}
