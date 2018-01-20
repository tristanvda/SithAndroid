package com.grietenenknapen.sithandroid.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.ui.CallbackFragment;
import com.grietenenknapen.sithandroid.util.FontCache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClientErrorFragment extends CallbackFragment<ClientErrorFragment.Callback> {

    private static final String KEY_TITLE = "key_title";
    private static final String KEY_MESSAGE = "key_message";

    @BindView(R.id.errorTitle)
    TextView errorTitle;
    @BindView(R.id.errorMessage)
    TextView errorMessage;

    public static Bundle createArguments(final String message, final String title) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_MESSAGE, message);
        return bundle;
    }

    public static Bundle createArguments(final String message) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, message);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_error, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
    }

    private void initLayout() {
        if (getArguments() == null) {
            return;
        }

        Typeface starWars = FontCache.get("fonts/Starjedi.ttf", getContext());
        errorMessage.setTypeface(starWars);

        if (getArguments().containsKey(KEY_TITLE)) {
            errorTitle.setText(getArguments().getString(KEY_TITLE));
        }

        if (getArguments().containsKey(KEY_MESSAGE)) {
            errorMessage.setText(getArguments().getString(KEY_MESSAGE));
        }
    }

    @OnClick(R.id.refreshButton)
    public void onRefreshButtonClicked() {
        if (callback != null) {
            callback.onRefreshClicked();
        }
    }

    @OnClick(R.id.closeButton)
    public void onCloseButtonClicked() {
        if (callback != null) {
            callback.onCloseClicked();
        }
    }

    public interface Callback {

        void onRefreshClicked();

        void onCloseClicked();

    }
}