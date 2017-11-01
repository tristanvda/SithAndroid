package com.grietenenknapen.sithandroid.ui;


public abstract class Presenter<T extends PresenterView> {
    private T view;

    public final void bindView(T view) {
        this.view = view;
        onViewBound();
    }

    public final void unbindView() {
        this.view = null;
        onViewUnBound();
    }

    protected abstract void onViewBound();

    protected void onViewUnBound() {
    }

    protected void onPresenterDestroy() {
    }

    protected T getView() {
        return view;
    }
}
