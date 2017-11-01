package com.grietenenknapen.sithandroid.ui;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class CallbackFragment<T> extends Fragment {
    protected T callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.callback = (T) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
    }
}
