package com.grietenenknapen.sithandroid.ui;


public abstract class Presenter<T extends PresenterView> {
    private T view;

    public void bindView(T view) {
        this.view = view;
        onViewBound();
    }

    public void unbindView() {
        this.view = null;
    }

    protected abstract void onViewBound();

    protected T getView() {
        return view;
    }
}
