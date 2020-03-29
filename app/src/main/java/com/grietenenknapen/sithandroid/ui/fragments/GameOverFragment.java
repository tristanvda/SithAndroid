package com.grietenenknapen.sithandroid.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameTeam;
import com.grietenenknapen.sithandroid.ui.CallbackFragment;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFlowActivity;
import com.grietenenknapen.sithandroid.ui.presenters.GameFlowPresenter;
import com.grietenenknapen.sithandroid.util.FontCache;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameOverFragment extends CallbackFragment<GameOverFragment.Callback> {
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

    @GameTeam.Team
    private int winningTeam;

    public static Bundle createArguments(final List<ActivePlayer> players, int winningTeam) {
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_PLAYERS, new ArrayList<>(players));
        bundle.putInt(WINNING_TEAM, winningTeam);

        return bundle;
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
        return inflater.inflate(R.layout.fragment_game_over, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        CharSequence winTeamText = getString(R.string.team_sith);

        switch (winningTeam) {
            case GameTeam.JEDI:
                winTeamText = getText(R.string.team_jedi_winning_text);
                break;
            case GameTeam.LOVERS:
                winTeamText = getText(R.string.team_lovers_winning_text);
                break;
            case GameTeam.SITH:
                winTeamText = getText(R.string.team_sith_winning_text);
                break;
            case GameTeam.GALEN_ERSO:
                winTeamText = getText(R.string.team_galen_erso_winning_text);
        }

        gameOverWinningTeamText.setText(winTeamText);
    }

    @OnClick(R.id.nextButton)
    protected void onNextButtonClicked() {
        if (callback != null) {
            callback.closeGame();
        }
    }

    public interface Callback extends GameFlowActivity {
        void closeGame();
    }
}
