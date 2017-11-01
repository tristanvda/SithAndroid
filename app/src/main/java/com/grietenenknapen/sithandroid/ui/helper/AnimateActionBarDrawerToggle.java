package com.grietenenknapen.sithandroid.ui.helper;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;

public class AnimateActionBarDrawerToggle implements DrawerLayout.DrawerListener {
    private static final float FINAL_SCALE = 0.65F;

    private final ActionBarDrawerToggle internalToggle;
    private ViewGroup contentFrame;
    private View leftDrawer;
    private Toolbar toolbar;
    private TextView sliderTitle;

    public AnimateActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                        @StringRes int openDrawerContentDescRes,
                                        @StringRes int closeDrawerContentDescRes) {

        internalToggle = new ActionBarDrawerToggle(activity, drawerLayout, null, openDrawerContentDescRes, closeDrawerContentDescRes);
        init(drawerLayout, null);
    }

    public AnimateActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                        Toolbar toolbar, @StringRes int openDrawerContentDescRes,
                                        @StringRes int closeDrawerContentDescRes) {

        internalToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, openDrawerContentDescRes,
                closeDrawerContentDescRes);
        init(drawerLayout, toolbar);
    }

    private void init(DrawerLayout layout, Toolbar toolbar) {
        this.toolbar = toolbar;
        contentFrame = (ViewGroup) layout.findViewById(R.id.content_frame);
        leftDrawer = layout.findViewById(R.id.left_drawer);
    }

    @Override
    public void onDrawerSlide(final View drawerView, final float slideOffset) {
        internalToggle.onDrawerSlide(drawerView, slideOffset);
        final float moveFactor = (leftDrawer.getWidth() * slideOffset);
        final float scaleFactor = 1 - (slideOffset * (1 - FINAL_SCALE));
        contentFrame.setTranslationX(moveFactor);
        contentFrame.setScaleX(scaleFactor);
        contentFrame.setScaleY(scaleFactor);

        if (sliderTitle != null) {
            sliderTitle.setAlpha(slideOffset);
        }
    }

    @Override
    public void onDrawerOpened(final View drawerView) {
        internalToggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(final View drawerView) {
        internalToggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerStateChanged(final int newState) {
        internalToggle.onDrawerStateChanged(newState);
    }

    public void enableSliderTitleAnimation(@IdRes final int idRes) {
        if (toolbar != null) {
            sliderTitle = (TextView) toolbar.findViewById(idRes);
            sliderTitle.setVisibility(View.VISIBLE);
            sliderTitle.setAlpha(0);
        } else {
            throw new IllegalStateException("SliderTitleAnimation cannot be enabled when a toolbar isn't provided");
        }
    }

    public void syncState() {
        internalToggle.syncState();
    }

    public void setHomeAsUpIndicator(Drawable indicator) {
        internalToggle.setHomeAsUpIndicator(indicator);
    }

    public void setHomeAsUpIndicator(int resId) {
        internalToggle.setHomeAsUpIndicator(resId);
    }

    public boolean isDrawerIndicatorEnabled() {
        return internalToggle.isDrawerIndicatorEnabled();
    }

    public void setDrawerIndicatorEnabled(boolean enable) {
        internalToggle.setDrawerIndicatorEnabled(enable);
    }

    @NonNull
    public DrawerArrowDrawable getDrawerArrowDrawable() {
        return internalToggle.getDrawerArrowDrawable();
    }

    public void setDrawerArrowDrawable(@NonNull DrawerArrowDrawable drawable) {
        internalToggle.setDrawerArrowDrawable(drawable);
    }

    public void setDrawerSlideAnimationEnabled(boolean enabled) {
        internalToggle.setDrawerSlideAnimationEnabled(enabled);
    }

    public boolean isDrawerSlideAnimationEnabled() {
        return internalToggle.isDrawerSlideAnimationEnabled();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        internalToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return internalToggle.onOptionsItemSelected(item);
    }

}
