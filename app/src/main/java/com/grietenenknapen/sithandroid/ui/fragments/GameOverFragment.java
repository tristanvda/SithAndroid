package com.grietenenknapen.sithandroid.ui.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFragmentCallback;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.util.FontCache;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameOverFragment extends Fragment {
    private static final String KEY_PLAYERS = "key:players";

    @BindView(R.id.gameOverText)
    TextView gameOverText;
    @BindView(R.id.gameOverWinners)
    TextView gameOverWinners;
    @BindView(R.id.nextButton)
    ImageButton nextButton;

    private GameOverFragment.Callback callback;

    public static Bundle createArguments(final ArrayList<String> players) {
        final Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_PLAYERS, players);

        return bundle;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.callback = (Callback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_game_over, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
        callback.setGameStatus(GameFlowPresenter.STATUS_GAME_OVER);
    }

    private void initLayout() {
        Typeface starWars = FontCache.get("fonts/Starjedi.ttf", getContext());

        gameOverText.setTypeface(starWars);
        disPlayText(getArguments().getStringArrayList(KEY_PLAYERS));
    }

    private void disPlayText(List<String> players) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String str : players) {
            stringBuilder.append(str);
            stringBuilder.append("\n");
        }

        gameOverWinners.setText(stringBuilder.toString());
    }

    @OnClick(R.id.nextButton)
    protected void onNextButtonClicked() {
        if (callback != null) {
            callback.closeGame();
        }
    }

    public interface Callback extends GameFragmentCallback {
        void closeGame();
    }
}
