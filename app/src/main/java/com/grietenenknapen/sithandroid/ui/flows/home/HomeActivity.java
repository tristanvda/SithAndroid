package com.grietenenknapen.sithandroid.ui.flows.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.ui.presenter.PresenterActivity;
import com.grietenenknapen.sithandroid.ui.presenter.PresenterFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends PresenterActivity<HomePresenter, HomePresenter.View> implements HomePresenter.View {
    private static final String PRESENTER_TAG = "home_presenter";

    @BindView(R.id.menu_start)
    TextView menuStart;
    @BindView(R.id.menu_resume)
    TextView menuResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initLayout();
    }

    private void initLayout() {
        Typeface starJedi = Typeface.createFromAsset(getAssets(),
                "fonts/Starjedi.ttf");
        menuStart.setTypeface(starJedi);
        menuResume.setTypeface(starJedi);
    }

    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<HomePresenter> getPresenterFactory() {
        return new HomePresenterFactory();
    }

    @Override
    protected HomePresenter.View getPresenterView() {
        return this;
    }

    private static class HomePresenterFactory implements PresenterFactory<HomePresenter> {

        @Override
        public HomePresenter createPresenter() {
            return new HomePresenter();
        }
    }
}
