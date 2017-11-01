package com.grietenenknapen.sithandroid.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.ui.views.SithCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRoleFragment extends Fragment {

    public static final String KEY_PLAYER = "key_player";

    @BindView(R.id.roleTitle)
    TextView roleTitle;
    @BindView(R.id.roleMessage)
    TextView roleMessage;
    @BindView(R.id.sithCard)
    SithCardView sithCardView;

    public static Bundle createArguments(final ActivePlayer activePlayer) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PLAYER, activePlayer);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_role, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
    }

    private void initLayout() {
        if (getArguments().containsKey(KEY_PLAYER)) {
            ActivePlayer player = getArguments().getParcelable(KEY_PLAYER);

            if (player == null) {
                return;
            }

            roleTitle.setText(getString(R.string.your_role, player.getName()));
            //TODO: role message
            sithCardView.setSithCard(player.getSithCard());
            roleMessage.setText("This is the description of your role. It's not implemented yet, so stop crying!");
        }
    }

}