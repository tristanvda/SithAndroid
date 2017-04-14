package com.grietenenknapen.sithandroid.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.grietenenknapen.sithandroid.application.Settings;
import com.pddstudio.starview.StarView;

public class SithStarView extends StarView {

    private static final long ANIMATION_BATTERY_SAVING_DELAY = 2 * 1000;

    public SithStarView(Context context) {
        super(context);
    }

    public SithStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {

        if (isInEditMode()){
            return;
        }

        super.onVisibilityChanged(changedView, visibility);

        if (visibility == View.VISIBLE) {
            //Resume Star View
            if (Settings.isBatterySavingMode(getContext())) {
                onResume();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onPause();
                    }
                }, ANIMATION_BATTERY_SAVING_DELAY);
            }

        }
    }
}
