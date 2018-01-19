package com.grietenenknapen.sithandroid.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.ui.views.SithCardView;
import com.grietenenknapen.sithandroid.util.FontCache;

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
            final Typeface starWars = FontCache.get("fonts/Starjedi.ttf", getContext());
            roleMessage.setTypeface(starWars);

            ActivePlayer player = getArguments().getParcelable(KEY_PLAYER);

            if (player == null) {
                return;
            }

            roleTitle.setText(getString(R.string.your_role, player.getName()));
            sithCardView.setSithCard(player.getSithCard());

            switch (player.getSithCard().getCardType()) {
                case GameCardType.BB8:
                    roleMessage.setText(getString(R.string.bb8_description));
                    break;
                case GameCardType.BOBA_FETT:
                    roleMessage.setText(getString(R.string.boba_fett_description));
                    break;
                case GameCardType.CHEWBACCA:
                    roleMessage.setText(getString(R.string.chewbacca_description));
                    break;
                case GameCardType.GENERAL_GRIEVOUS:
                    roleMessage.setText(getString(R.string.general_grievous_description));
                    break;
                case GameCardType.HAN_SOLO:
                    roleMessage.setText(getString(R.string.han_solo_description));
                    break;
                case GameCardType.JEDI:
                    roleMessage.setText(getString(R.string.jedi_description));
                    break;
                case GameCardType.KYLO_REN:
                    roleMessage.setText(getString(R.string.kylo_ren_description));
                    break;
                case GameCardType.MAZ_KANATA:
                    roleMessage.setText(getString(R.string.maz_kanata_description));
                    break;
                case GameCardType.PEEPING_FINN:
                    roleMessage.setText(getString(R.string.finn_description));
                    break;
                case GameCardType.SITH:
                    roleMessage.setText(getString(R.string.sith_description));
                    break;
                default:
                    roleMessage.setText(getString(R.string.no_description));
            }
        }
    }
}