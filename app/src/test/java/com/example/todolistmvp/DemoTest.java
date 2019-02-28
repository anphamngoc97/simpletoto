package com.example.todolistmvp;


import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.widget.Switch;

import com.example.todolistmvp.addtask.AddTaskActivity;
import com.example.todolistmvp.maintask.MainTaskActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

@RunWith(RobolectricTestRunner.class)
public class DemoTest {
    MainActivity mainActivity;
    MainTaskActivity mainTaskActivity;
    AddTaskActivity addTaskActivity;

    @Before
    public void setup(){
        mainActivity = Robolectric.buildActivity(MainActivity.class)
                .create().get();
        mainTaskActivity = Robolectric.buildActivity(MainTaskActivity.class)
                .create().get();
        addTaskActivity = Robolectric.buildActivity(AddTaskActivity.class)
                .create().get();


    }
    @Test
    public void checkCurrentActivity(){
        Intent expectActivity = new Intent(mainActivity,
                MainTaskActivity.class);

        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        Intent mainTaskActivity = shadowActivity.getNextStartedActivity();

        Assert.assertTrue(expectActivity.filterEquals(mainTaskActivity));
    }
    @Test
    public void checkAddTask(){
        Intent expectActivity = new Intent(mainTaskActivity, AddTaskActivity.class);

        mainTaskActivity.findViewById(R.id.floatBtnAdd).callOnClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(mainTaskActivity);

        Intent actualActivity = shadowActivity.getNextStartedActivity();
        Assert.assertTrue(expectActivity.filterEquals(actualActivity));
    }
    @Test
    public void checkApplyAddTask(){
        Intent expectActivity = new Intent(addTaskActivity, MainTaskActivity.class);

        addTaskActivity.findViewById(R.id.btnUpdate).callOnClick();

        TextInputEditText editTitle = addTaskActivity.findViewById(R.id.editTitle);
        Switch switchReminder = addTaskActivity.findViewById(R.id.switchReminder);
        String title = editTitle.getText().toString();

        Assert.assertEquals(title,"");

        ShadowActivity shadowActivity = Shadows.shadowOf(addTaskActivity);

        Intent actualActivity = shadowActivity.getNextStartedActivity();
        System.out.println("package: " + expectActivity.getScheme());
        Assert.assertTrue(expectActivity.filterEquals(actualActivity));

    }
}

