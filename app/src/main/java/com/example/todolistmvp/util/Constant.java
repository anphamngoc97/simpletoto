package com.example.todolistmvp.util;

import android.content.Context;

import com.example.todolistmvp.R;

public class Constant {
    public static final String DATE_FORMAT = "dd/MM/yyyy:HH:mm";
    public static final boolean REMOVE = true;
    public static final boolean EDIT = !REMOVE;


    public enum ChildConstantNumber{
        REQUEST_CODE_EDIT_TASK,
        REQUEST_CODE_ADD_TASK,
        REQUEST_CODE_ADD_DETAIL_TASK,
        REQUEST_CODE_SEARCH;

        public int getValue(){
            return this.ordinal();
        }
    }
    public enum ChildConstantString{
        KEY_BROADCAST_TASK_TITLE,
        KEY_BROADCAST_TASK_ID,
        KEY_EXTRA_EDIT_TASK_OBJECT,
        KEY_EXTRA_EDIT_TASK_POSITION,
        KEY_EXTRA_ADD_TASK,
        KEY_EXTRA_IS_REMOVE,
        KEY_EXTRA_PREVIOUS_CLASS,
        KEY_EXTRA_TASK_DETAIL,
        KEY_EXTRA_TASK_PRIORITY,
        KEY_EXTRA_TASK_CATEGORY;

        public String getValue(){
            return this.name();
        }

    }
    public enum ChildConstantDetailTaskPriority{
        HIGH,
        MEDIUM,
        LOW,
        NONE;

        public String getValue(){
            return this.name();
        }
        public String getValueAsLanguage(Context context){
            switch (this){
                case LOW:{
                    return context.getResources().getString(R.string.priorityLow);
                }
                case MEDIUM:{
                    return context.getResources().getString(R.string.priorityMedium);
                }
                case HIGH:{
                    return context.getResources().getString(R.string.priorityHigh);
                }
                case NONE:{
                    return context.getResources().getString(R.string.priorityNone);
                }
                default:{
                    return context.getResources().getString(R.string.priorityNone);
                }
            }
        }
    }
    public enum ChildConstantDetailTaskcategory{
        NONE,
        PERSONAL,
        BUSINESS;

        public String getValue(){
            return this.name();
        }
        public String getValueAsLanguage(Context context){
            switch (this){
                case BUSINESS:{
                    return context.getResources().getString(R.string.categoryBusiness);
                }
                case PERSONAL:{
                    return context.getResources().getString(R.string.categoryPersonal);
                }
                default:{
                    return context.getResources().getString(R.string.priorityNone);
                }
            }
        }

    }

}
