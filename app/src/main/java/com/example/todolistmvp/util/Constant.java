package com.example.todolistmvp.util;

public class Constant {
    public static final String DATE_FORMAT = "dd/mm/yyyy:hh:mm";
    public static final boolean REMOVE = true;
    public static final boolean EDIT = !REMOVE;


    public enum ChildConstantNumber{
        REQUEST_CODE_EDIT_TASK,
        REQUEST_CODE_ADD_TASK;

        public int getValue(){
            return this.ordinal();
        }
    }
    public enum ChildConstantString{
        KEY_BROADCAST_TASK_TITLE,
        KEY_BROADCAST_TASK_ID,
        KEY_SEND_EXTRA_EDIT_TASK_OBJECT,
        KEY_SEND_EXTRA_EDIT_TASK_POSITION,
        KEY_EXTRA_ADD_TASK,
        KEY_EXTRA_IS_REMOVE;

        public String getValue(){
            return this.name();
        }

    }

}
