package com.grietenenknapen.sithandroid.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class PresenterFragment<P extends Presenter<U>, U extends PresenterView>
        extends Fragment {

    private PresenterCache presenterCache;
    private boolean isDestroyedBySystem;
    private P presenter;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            presenterCache = ((PresenterActivity) getContext()).getPresenterCache();
        } catch (ClassCastException e) {
            throw new ClassCastException(getContext().toString() + " must be a PresenterActivity");
        }

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
            presenter.onPresenterDestroy();
            presenterCache.removePresenter(getPresenterTag());
        }
    }

    protected P getPresenter() {
        return presenter;
    }

    protected abstract String getPresenterTag();

    protected abstract PresenterFactory<P> getPresenterFactory();

    protected abstract U getPresenterView();


}

