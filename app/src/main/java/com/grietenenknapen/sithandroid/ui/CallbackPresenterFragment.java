package com.grietenenknapen.sithandroid.ui;


import android.content.Context;

public abstract class CallbackPresenterFragment<P extends Presenter<U>, U extends PresenterView, T> extends PresenterFragment<P, U> {
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
