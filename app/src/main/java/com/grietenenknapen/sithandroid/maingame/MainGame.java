package com.grietenenknapen.sithandroid.maingame;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.Pair;

import com.grietenenknapen.sithandroid.game.Game;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.game.ActivePlayer;
import com.grietenenknapen.sithandroid.model.game.GameCardType;
import com.grietenenknapen.sithandroid.model.game.GameSide;

import java.util.ArrayList;
import java.util.List;

public class MainGame implements Game, Parcelable {
    private int currentNight = 0;
    private int currentUseCaseStep = 0;
    private int currentTurn = 0;
    private long gameStartTimeStamp;
    private List<ActivePlayer> activePlayers;
    private List<ActivePlayer> deathList;
    private Pair<ActivePlayer, ActivePlayer> lovers;
    private boolean bobaMedPackUsed = false;
    private boolean bobaRocketUsed = false;
    private boolean rocketAlreadySelected = false;

    @DayCycle.Cycle
    private int dayCycle;

    public MainGame(List<ActivePlayer> activePlayers) {
        this.activePlayers = activePlayers;
        dayCycle = DayCycle.CYCLE_DAY;
        deathList = new ArrayList<>();
    }

    public FlowDetails getFlowDetails() {
        return new FlowDetails(currentNight, currentUseCaseStep, currentTurn);
    }

    public List<ActivePlayer> getActivePlayers() {
        return activePlayers;
    }

    public boolean isRocketAlreadySelected() {
        return rocketAlreadySelected;
    }

    public void setRocketAlreadySelected(boolean rocketAlreadySelected) {
        this.rocketAlreadySelected = rocketAlreadySelected;
    }

    public boolean isBobaRocketUsed() {
        return bobaRocketUsed;
    }

    public void setBobaRocketUsed(boolean bobaRocketUsed) {
        this.bobaRocketUsed = bobaRocketUsed;
    }

    public boolean isBobaMedPackUsed() {
        return bobaMedPackUsed;
    }

    public void setBobaMedPackUsed(boolean bobaMedPackUsed) {
        this.bobaMedPackUsed = bobaMedPackUsed;
    }

    public Pair<ActivePlayer, ActivePlayer> getLovers() {
        return lovers;
    }

    public List<ActivePlayer> getDeathList() {
        return deathList;
    }

    public void setLovers(Pair<ActivePlayer, ActivePlayer> lovers) {
        this.lovers = lovers;
    }

    @Override
    public int getCurrentStep() {
        return currentUseCaseStep;
    }

    @Override
    public int getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public int getCurrentRound() {
        return currentNight;
    }

    @Override
    public boolean isRoundActive() {
        return dayCycle == DayCycle.CYCLE_NIGHT;
    }

    @Override
    public void setupNewRound() {
        currentTurn = 1;
        currentUseCaseStep = 1;
        nextRound();
        dayCycle = DayCycle.CYCLE_NIGHT;
        deathList = new ArrayList<>();
    }

    @Override
    public void finishRound() {
        currentUseCaseStep = 0;
        currentTurn = 0;
        dayCycle = DayCycle.CYCLE_DAY;

        updateDeathListWithLovers();

        for (ActivePlayer deathPlayer : deathList) {
            deathPlayer.setAlive(false);
        }
    }

    public void killPlayers(List<Player> players) {
        for (Player player : players) {
            ActivePlayer activePlayer = getActivePlayer(player.getId());
            if (activePlayer != null) {
                activePlayer.setAlive(false);
            }
        }
    }

    private void updateDeathListWithLovers() {
        ActivePlayer loverPlayer = null;
        if (lovers == null) {
            return;
        }

        for (ActivePlayer deathPlayer : deathList) {

            if (deathPlayer.getPlayerId() == lovers.first.getPlayerId()) {
                loverPlayer = lovers.second;
            }

            if (deathPlayer.getPlayerId() == lovers.second.getPlayerId()) {
                loverPlayer = lovers.first;
            }
        }

        if (loverPlayer != null && !deathList.contains(loverPlayer)) {
            deathList.add(loverPlayer);
        }
    }

    @Override
    public void nextRound() {
        currentNight++;
    }

    @Override
    public void nextStep() {
        currentUseCaseStep++;
    }

    @Override
    public void nextTurn() {
        currentUseCaseStep = 1;
        currentTurn++;
    }

    public void deleteFromDeathList(long playerId) {

        int toDelete = -1;

        for (int i = 0; i < deathList.size(); i++) {
            if (playerId == deathList.get(i).getPlayerId()) {
                toDelete = i;
            }
        }

        if (toDelete >= 0) {
            deathList.remove(toDelete);
        }
    }

    public void addToDeathList(long playerId) {
        deathList.add(getActivePlayer(playerId));
    }

    public ActivePlayer getActivePlayer(final long id) {
        for (ActivePlayer activePlayer : activePlayers) {
            if (activePlayer.getPlayerId() == id) {
                return activePlayer;
            }
        }
        return null;
    }

    public List<ActivePlayer> getAlivePlayers() {
        final List<ActivePlayer> alivePlayers = new ArrayList<>();

        for (ActivePlayer activePlayer : activePlayers) {
            if (activePlayer.isAlive()) {
                alivePlayers.add(activePlayer);
            }
        }
        return alivePlayers;
    }

    public List<ActivePlayer> getKilledPlayers() {
        final List<ActivePlayer> killedPlayers = new ArrayList<>();

        for (ActivePlayer activePlayer : activePlayers) {
            if (!activePlayer.isAlive()) {
                killedPlayers.add(activePlayer);
            }
        }
        return killedPlayers;
    }

    public List<ActivePlayer> getAlivePlayersLightSide() {
        final List<ActivePlayer> alivePlayers = new ArrayList<>();

        for (ActivePlayer activePlayer : activePlayers) {
            if (activePlayer.isAlive() && activePlayer.getSide() != GameSide.SITH) {
                alivePlayers.add(activePlayer);
            }
        }
        return alivePlayers;
    }

    public List<ActivePlayer> findPlayersByType(@GameCardType.CardType int type) {
        final List<ActivePlayer> typePlayers = new ArrayList<>();

        for (ActivePlayer activePlayer : activePlayers) {
            if (activePlayer.getSithCard().getCardType() == type) {
                typePlayers.add(activePlayer);
            }
        }
        return typePlayers;
    }

    public ActivePlayer getCurrentKilledPlayer() {
        if (deathList.size() > 0) {
            return deathList.get(0);
        } else {
            return null;
        }
    }

    public boolean isGameOver() {
        int jediSideCount = 0;
        int sithSideCount = 0;
        int aliveCount = 0;

        for (ActivePlayer activePlayer : activePlayers) {
            if (!activePlayer.isAlive()) {
                continue;
            }

            aliveCount++;

            if (activePlayer.getSide() == GameSide.SITH) {
                sithSideCount++;
            } else {
                if (activePlayer.getSithCard().getCardType() == GameCardType.KYLO_REN && currentNight >= 2) {
                    sithSideCount++;
                } else {
                    jediSideCount++;
                }
            }
        }

        if (lovers != null
                && lovers.first.isAlive()
                && lovers.second.isAlive()
                && aliveCount == 2) {
            return true;
        } else {
            return jediSideCount == 0 || sithSideCount == 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.currentNight);
        dest.writeInt(this.currentUseCaseStep);
        dest.writeInt(this.currentTurn);
        dest.writeLong(this.gameStartTimeStamp);
        dest.writeTypedList(this.activePlayers);
        dest.writeTypedList(this.deathList);
        dest.writeInt(this.dayCycle);
    }

    @SuppressWarnings("ResourceType")
    protected MainGame(Parcel in) {
        this.currentNight = in.readInt();
        this.currentUseCaseStep = in.readInt();
        this.currentTurn = in.readInt();
        this.gameStartTimeStamp = in.readLong();
        this.activePlayers = in.createTypedArrayList(ActivePlayer.CREATOR);
        this.deathList = in.createTypedArrayList(ActivePlayer.CREATOR);
        this.dayCycle = in.readInt();
    }

    public static final Parcelable.Creator<MainGame> CREATOR = new Parcelable.Creator<MainGame>() {
        @Override
        public MainGame createFromParcel(Parcel source) {
            return new MainGame(source);
        }

        @Override
        public MainGame[] newArray(int size) {
            return new MainGame[size];
        }
    };
}
