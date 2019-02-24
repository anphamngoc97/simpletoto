package com.example.todolistmvp.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.todolistmvp.util.Showlog;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxSearch {
    public static Observable<String> from(EditText editText){

        PublishSubject<String> publishSubject = PublishSubject.create();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                publishSubject.onNext(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return publishSubject;
    }
}
