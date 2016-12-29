package com.grietenenknapen.sithandroid.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public final class ActivityUtils {
    private static final float SNACK_BAR_PADDING_BOTTOM_DP = 50;

    private ActivityUtils() {
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity, View view) {
        if (view == null) {
            view = activity.getCurrentFocus();
            if (view == null) {
                return;
            }
        }
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public static void showSnackBar(RelativeLayout layout, String text) {
        final Snackbar snackbar = Snackbar
                .make(layout, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        final View snackBarView = snackbar.getView();
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();

        params.setMargins(params.leftMargin,
                params.topMargin,
                params.rightMargin,
                (int) (params.bottomMargin + ResourceUtils.getPxFromDp(layout.getContext(), SNACK_BAR_PADDING_BOTTOM_DP)));

        snackBarView.setLayoutParams(params);
        snackbar.show();
    }
}
