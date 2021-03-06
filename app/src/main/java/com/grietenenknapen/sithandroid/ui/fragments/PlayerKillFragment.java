package com.grietenenknapen.sithandroid.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;

import java.util.ArrayList;

public class PlayerKillFragment extends PlayerSelectFragment {
    private static final String PRESENTER_TAG = "player_kill_presenter";

    public static Bundle createArguments(final ArrayList<Player> players) {
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_PLAYERS, players);
        bundle.putInt(KEY_SELECTION_MAX, 2);
        return bundle;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        titleTextView.setText(getString(R.string.select_players_kill));
        nextButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_done));
    }

    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callback.setGameStatus(GameFlowPresenter.STATUS_KILL_PLAYERS);
    }
}
