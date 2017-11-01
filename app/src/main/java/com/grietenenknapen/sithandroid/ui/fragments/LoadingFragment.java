package com.grietenenknapen.sithandroid.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.grietenenknapen.sithandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingFragment extends Fragment {

    private static final String KEY_TITLE = "key_title";

    @BindView(R.id.loadingTitle)
    TextView loadingTitle;

    public static Bundle createArguments(final String title) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
    }

    private void initLayout() {
        if (getArguments() != null && getArguments().containsKey(KEY_TITLE)) {
            loadingTitle.setText(getArguments().getString(KEY_TITLE));
        }
    }

}
