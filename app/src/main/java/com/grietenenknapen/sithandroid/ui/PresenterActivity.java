package com.grietenenknapen.sithandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class PresenterActivity<P extends Presenter<U>, U extends PresenterView> extends AppCompatActivity {
    private PresenterCache presenterCache;
    protected P presenter;
    private boolean isDestroyedBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Object retainObject = getLastCustomNonConfigurationInstance();

        if (retainObject != null) {
            presenterCache = (PresenterCache) retainObject;
        } else {
            presenterCache = new PresenterCache();
        }

        presenter = presenterCache.getPresenter(getPresenterTag(),
                getPresenterFactory());
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(getPresenterView());
        isDestroyedBySystem = false;
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        isDestroyedBySystem = true;
        return presenterCache;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isDestroyedBySystem) {
            presenter.onPresenterDestroy();
        }
    }

    protected P getPresenter() {
        return presenter;
    }

    public final PresenterCache getPresenterCache() {
        return presenterCache;
    }

    protected abstract String getPresenterTag();

    protected abstract PresenterFactory<P> getPresenterFactory();

    protected abstract U getPresenterView();

}
