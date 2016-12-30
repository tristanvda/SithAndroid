package com.grietenenknapen.sithandroid.ui.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameTeam;
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
    private static final String WINNING_TEAM = "key:winning_team";


    @BindView(R.id.gameOverText)
    TextView gameOverText;
    @BindView(R.id.gameOverWinners)
    TextView gameOverWinners;
    @BindView(R.id.nextButton)
    ImageButton nextButton;
    @BindView(R.id.gameOverWinningTeam)
    TextView gameOverWinningTeamText;

    private GameOverFragment.Callback callback;

    @GameTeam.Team
    private int winningTeam;

    public static Bundle createArguments(final ArrayList<ActivePlayer> players, int winningTeam) {
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_PLAYERS, players);
        bundle.putInt(WINNING_TEAM, winningTeam);

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

    @SuppressWarnings("ResourceType")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winningTeam = getArguments().getInt(WINNING_TEAM);
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
        displayText(getArguments().<ActivePlayer>getParcelableArrayList(KEY_PLAYERS));
    }

    private void displayText(List<ActivePlayer> players) {
        StringBuilder stringBuilder = new StringBuilder();

        for (ActivePlayer player : players) {
            if (player.getTeam() == winningTeam)
            stringBuilder.append(player.getName());
            stringBuilder.append("\n");
        }

        gameOverWinners.setText(stringBuilder.toString());

        String winTeamText = getString(R.string.team_sith);

        switch (winningTeam) {
            case GameTeam.JEDI:
                winTeamText = getString(R.string.team_jedi);
                break;
            case GameTeam.LOVERS:
                winTeamText = getString(R.string.team_lovers);
                break;
            case GameTeam.SITH:
                winTeamText = getString(R.string.team_sith);
                break;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(getString(R.string.winning_team_1));
        builder.append(" ");
        final int start = builder.length();
        builder.append(winTeamText);
        builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, start + winTeamText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" ");

        builder.append(getString(R.string.winning_team_2));

        gameOverWinningTeamText.setText(builder);
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
