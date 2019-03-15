package com.example.todolistmvp;

import android.app.Service;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.example.todolistmvp.util.Showlog;

public class BaseActivity extends AppCompatActivity {

    ViewGroup parentLayout;
    boolean isKeyboardShowing = false;

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    Rect r = new Rect();
                    parentLayout.getWindowVisibleDisplayFrame(r);
                    int screenHeight = parentLayout.getRootView().getHeight();

                    // r.bottom is the position above soft keypad or device button.
                    // if keypad is shown, the r.bottom is smaller than that before.
                    int keypadHeight = screenHeight - r.bottom;

                    //            Showlog.d( "keypadHeight = " + keypadHeight);

                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        // keyboard is opened
                        ///                   Showlog.d("keyboard show");
                        onShowKeyBoard(keypadHeight);

                    } else {
                        // keyboard is closed
                        //              Showlog.d("keyboard hide");
                        onHideKeyboard();
                    }
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentLayout = findViewById(android.R.id.content);
    }

    @Override
    protected void onStart() {
        super.onStart();
        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        Showlog.d("start add listener");
    }

    public void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Service.INPUT_METHOD_SERVICE);

        if (!isKeyboardShowing) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        isKeyboardShowing = true;
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Service.INPUT_METHOD_SERVICE);


        if (isKeyboardShowing) {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
//        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        isKeyboardShowing = false;
    }

    public void onShowKeyBoard(int keyboardHeight) {
        isKeyboardShowing = true;
    }

    public void onHideKeyboard() {
        isKeyboardShowing = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard();
        parentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        Showlog.d("pause remove listener");
    }

}
