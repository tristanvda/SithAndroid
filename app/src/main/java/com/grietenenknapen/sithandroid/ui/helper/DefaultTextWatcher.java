package com.grietenenknapen.sithandroid.ui.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class DefaultTextWatcher implements TextWatcher {
    private final View view;
    private final OnTextChangedListener listener;

    public DefaultTextWatcher(View view, OnTextChangedListener listener) {
        this.view = view;
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        listener.onTextChanged(view);
    }

    public interface OnTextChangedListener {
        void onTextChanged(View v);
    }
}