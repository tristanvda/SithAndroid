package com.grietenenknapen.sithandroid.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.application.Settings;
import com.grietenenknapen.sithandroid.ui.presenters.HomePresenter;
import com.grietenenknapen.sithandroid.ui.PresenterActivity;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.util.FontCache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends PresenterActivity<HomePresenter, HomePresenter.View> implements HomePresenter.View {
    private static final String PRESENTER_TAG = "home_presenter";

    private static final int SEND_SMS_PERMISSION = 1;

    @BindView(R.id.menuStart)
    TextView menuStart;
    @BindView(R.id.menuResume)
    TextView menuResume;
    @BindView(R.id.fab)
    FloatingActionMenu fab;

    private boolean permissionGiven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initLayout();
    }

    private void initLayout() {
        Typeface starWars = FontCache.get("fonts/Starjedi.ttf", this);
        menuStart.setTypeface(starWars);
        menuResume.setTypeface(starWars);
    }

    @OnClick(R.id.menuStart)
    public void onMenuStartClicked() {
        if (checkSmsPermissionsOk()) {
            getPresenter().onMenuStartClicked();
        } else {
            getPresenter().setMenuStartSelected(true);
        }
    }

    private boolean checkSmsPermissionsOk() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION);

            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.menuResume)
    public void onMenuResumeClicked() {
        if (checkSmsPermissionsOk()) {
            getPresenter().onMenuResumeClicked();
        } else {
            getPresenter().setMenuResumeSelected(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGiven = true;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.close(false);
        if (permissionGiven) {
            getPresenter().handleStartOrResumeGameClicked();
            permissionGiven = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (fab.isOpened()) {
            fab.close(true);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.menuPlayers)
    public void onMenuPlayersClicked() {
        getPresenter().onPlayersClicked();
    }

    @OnClick(R.id.menuSettings)
    public void onMenuSettingsClicked() {
        getPresenter().onSettingsClicked();
    }

    @Override
    public void goToPlayersScreen() {
        startActivity(new Intent(this, PlayerActivity.class));
    }

    @Override
    public void goToGameFlowScreen() {
        startActivity(new Intent(this, GameFlowActivity.class));
    }

    @Override
    public void updateStartResumeScreen() {
        if (Settings.isMainGameSaved(this) && Settings.getSavedMainGame(this) != null) {
            showResumeButton();
        } else {
            hideResumeButton();
        }
    }

    @Override
    public void deleteSavedGame() {
        Settings.setSavedMainGame(this, null);
        Settings.setMainGameSaved(this, false);
    }

    @Override
    public void goToSettingsScreen() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void showResumeButton() {
        menuResume.setVisibility(View.VISIBLE);
    }

    private void hideResumeButton() {
        menuResume.setVisibility(View.GONE);
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
