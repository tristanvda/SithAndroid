package com.grietenenknapen.sithandroid.ui.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class PresenterActivity<P extends Presenter<U>, U extends PresenterView> extends AppCompatActivity {
    private PresenterCache presenterCache;
    private boolean isDestroyedBySystem;
    private P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Object retainObject = getLastCustomNonConfigurationInstance();

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
        presenter.bindView(getPresenterView());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenterCache;
    }

    public final PresenterCache getPresenterCache(){
        return presenterCache;
    }

    protected abstract String getPresenterTag();

    protected abstract PresenterFactory<P> getPresenterFactory();

    protected abstract U getPresenterView();

}
