package com.grietenenknapen.sithandroid.ui.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class PresenterFragment<P extends Presenter<U>, U extends PresenterView,
        A extends PresenterActivity>
        extends Fragment {

    private PresenterCache presenterCache;
    private boolean isDestroyedBySystem;
    private P presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            presenterCache = ((PresenterActivity) context).getPresenterCache();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must be a PresenterActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = presenterCache.getPresenter(getPresenterTag(),
                getPresenterFactory());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bindView(getPresenterView());
        isDestroyedBySystem = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isDestroyedBySystem = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isDestroyedBySystem) {
            presenterCache.removePresenter(getPresenterTag());
        }
    }

    protected abstract String getPresenterTag();

    protected abstract PresenterFactory<P> getPresenterFactory();

    protected abstract U getPresenterView();


}

