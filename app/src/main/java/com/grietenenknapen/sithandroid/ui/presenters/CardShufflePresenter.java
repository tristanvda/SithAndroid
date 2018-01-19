package com.grietenenknapen.sithandroid.ui.presenters;

import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameSide;
import com.grietenenknapen.sithandroid.model.game.GameTeam;
import com.grietenenknapen.sithandroid.service.ServiceCallBack;
import com.grietenenknapen.sithandroid.service.SithCardService;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;
import com.grietenenknapen.sithandroid.util.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CardShufflePresenter extends Presenter<CardShufflePresenter.View> {
    private final SithCardService sithCardService;
    private final List<Player> players;
    private List<SithCard> sithCards;
    private List<ActivePlayer> activePlayers;

    public CardShufflePresenter(SithCardService sithCardService, List<Player> players) {
        this.sithCardService = sithCardService;
        this.players = players;
    }

    @Override
    protected void onViewBound() {
        if (activePlayers == null) {
            if (sithCards == null) {
                sithCardService.retrieveAllSithCards(new ServiceCallBack<List<SithCard>>() {
                    @Override
                    public void onSuccess(List<SithCard> returnData) {
                        sithCards = returnData;
                        shuffleCards();
                        getView().showActivePlayers(activePlayers);
                    }

                    @Override
                    public void onError(int messageResId) {

                    }
                });
            } else {
                shuffleCards();
                getView().showActivePlayers(activePlayers);
            }
        } else {
            getView().showActivePlayers(activePlayers);
        }
    }

    private void shuffleCards() {
        long seed = System.nanoTime();
        final List<SithCard> shuffledCards = new ArrayList<>(sithCards);

        Collections.shuffle(shuffledCards, new Random(seed));
        filterSithCards(shuffledCards, players.size(), 0.4f);

        activePlayers = new ArrayList<>();

        int sithCount = 0;

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            SithCard card = shuffledCards.get(i);

            final int gameSide = GameSide.getSideFromCardType(card.getCardType());

            if (gameSide == GameSide.SITH) {
                sithCount++;
            }

            ActivePlayer activePlayer = ActivePlayer.newBuilder()
                    .player_id(player.getId())
                    .alive(true)
                    .name(player.getName())
                    .side(gameSide)
                    .team(GameTeam.getInitialTeamFromCardType(card.getCardType()))
                    .sithCard(card)
                    .telephoneNumber(player.getTelephoneNumber())
                    .build();

            activePlayers.add(activePlayer);
        }

        if (sithCount == 0) {
            replaceCardWithSithCard(shuffledCards);
        }
    }

    private void replaceCardWithSithCard(final List<SithCard> shuffledCards) {
        for (int i = players.size(); i < shuffledCards.size(); i++) {
            SithCard card = shuffledCards.get(i);

            if (GameSide.getSideFromCardType(card.getCardType()) == GameSide.SITH) {
                final int replacePosition = MathUtils.generateRandomInteger(0, activePlayers.size() - 1);
                final ActivePlayer activePlayer = activePlayers.get(replacePosition);
                activePlayer.setSithCard(card);
                activePlayer.setSide(GameSide.getSideFromCardType(card.getCardType()));
                activePlayer.setTeam(GameTeam.getInitialTeamFromCardType(card.getCardType()));
                break;
            }
        }
    }

    private void filterSithCards(final List<SithCard> cards, final int amountPlayers, final float maxPercentageSith) {
        int countSith = 0;

        final int maxSithCardCount = (int) (amountPlayers * maxPercentageSith);
        Iterator<SithCard> iterator = cards.iterator();

        while (iterator.hasNext()) {
            final SithCard card = iterator.next();
            if (GameSide.getSideFromCardType(card.getCardType()) == GameSide.SITH) {
                countSith++;
                if (countSith > maxSithCardCount && cards.size() > amountPlayers) {
                    iterator.remove();
                }
            }
        }
    }

    public void onNextClicked() {
        getView().onCardShuffled(activePlayers);
    }

    public interface View extends PresenterView {
        void showActivePlayers(List<ActivePlayer> activePlayers);

        void onCardShuffled(List<ActivePlayer> activePlayers);
    }
}
