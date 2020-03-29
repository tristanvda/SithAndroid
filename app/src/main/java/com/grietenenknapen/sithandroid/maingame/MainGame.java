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
import com.grietenenknapen.sithandroid.model.game.GameTeam;
import com.grietenenknapen.sithandroid.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class MainGame implements Game, Parcelable {
    private int currentNight = 0;
    private int currentUseCaseStep = 0;
    private int currentTurn = 0;
    private long gameStartTimeStamp;
    private List<ActivePlayer> activePlayers;
    private List<Long> deathList;
    private List<Long> switchRolesList;
    private Pair<Long, Long> lovers;
    private boolean bobaMedPackUsed;
    private boolean bobaRocketUsed;
    private boolean rocketAlreadySelected;
    private boolean gameOver;

    @DayCycle.Cycle
    private int dayCycle;

    @GameTeam.Team
    private int winningTeam = GameTeam.SITH;

    public MainGame(List<ActivePlayer> activePlayers) {
        this.activePlayers = activePlayers;
        gameStartTimeStamp = System.currentTimeMillis();
        dayCycle = DayCycle.CYCLE_DAY;
        deathList = new ArrayList<>();
        switchRolesList = new ArrayList<>();
    }

    public FlowDetails getFlowDetails() {
        return new FlowDetails(currentNight, currentUseCaseStep, currentTurn);
    }

    @Override
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
        return new Pair<>(getActivePlayer(lovers.first), getActivePlayer(lovers.second));
    }

    public List<ActivePlayer> getDeathList() {
        final List<ActivePlayer> activeDeathList = new ArrayList<>();

        for (Long deathPlayerId : this.deathList) {
            activeDeathList.add(getActivePlayer(deathPlayerId));
        }
        return activeDeathList;
    }

    public List<ActivePlayer> getSwitchRolesList() {
        final List<ActivePlayer> activeSwitchRoleList = new ArrayList<>();

        for (Long switchRolePlayerId : this.switchRolesList) {
            activeSwitchRoleList.add(getActivePlayer(switchRolePlayerId));
        }
        return activeSwitchRoleList;
    }



    public void setLovers(Pair<ActivePlayer, ActivePlayer> lovers) {
        this.lovers = new Pair<>(lovers.first.getPlayerId(), lovers.second.getPlayerId());
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
        rocketAlreadySelected = false;
        nextRound();
        dayCycle = DayCycle.CYCLE_NIGHT;
        deathList = new ArrayList<>();
        switchRolesList = new ArrayList<>();
    }

    @Override
    public void finishRound() {
        currentUseCaseStep = 0;
        currentTurn = 0;
        rocketAlreadySelected = false;
        dayCycle = DayCycle.CYCLE_DAY;

        updateDeathListWithLovers();
        switchRoles(switchRolesList);

        for (Long deathPlayerId : deathList) {
            getActivePlayer(deathPlayerId).setAlive(false);
        }
        gameOver = checkGameOver();
    }

    public void killPlayers(List<Player> players) {
        for (Player player : players) {
            ActivePlayer activePlayer = getActivePlayer(player.getId());
            if (activePlayer != null) {
                activePlayer.setAlive(false);
            }
        }
        gameOver = checkGameOver();
    }

    private void switchRoles(List<Long> playerIds) {
        final int direction = MathUtils.generateRandomInteger(0, 1); //shift left, shift right

        final List<ActivePlayer> players = new ArrayList<>();

        for (Long id : playerIds) {
            final ActivePlayer activePlayer = getActivePlayer(id);
            if (activePlayer != null) {
                players.add(activePlayer);
            }
        }

        //TODO: use seperate list for the roles in original position

        for (int i = 0; i < players.size(); i++) {
            final ActivePlayer player = players.get(i);
            if (i == players.size() - 1 && direction == 0) {
                player.setSithCard(players.get(0).getSithCard());
            } else if (i == 0 && direction == 1) {
                player.setSithCard(players.get(players.size() - 1).getSithCard());
            } else {
                player.setSithCard(players.get(direction == 0 ? i + 1 : i - 1).getSithCard());
            }
        }
    }

    private void updateDeathListWithLovers() {
        Long loverPlayerId = null;
        if (lovers == null) {
            return;
        }

        for (Long deathPlayerId : deathList) {

            if (deathPlayerId.equals(lovers.first)) {
                loverPlayerId = lovers.second;
            }

            if (deathPlayerId.equals(lovers.second)) {
                loverPlayerId = lovers.first;
            }
        }

        if (loverPlayerId != null && !deathList.contains(loverPlayerId)) {
            deathList.add(loverPlayerId);
        }
    }

    @Override
    public void nextRound() {
        currentNight++;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
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
            if (playerId == deathList.get(i)) {
                toDelete = i;
            }
        }

        if (toDelete >= 0) {
            deathList.remove(toDelete);
        }
    }

    public void setSwitchRolesList(List<Long> ids) {
        switchRolesList.clear();
        switchRolesList.addAll(ids);
    }

    public void addToDeathList(long playerId) {
        deathList.add(playerId);
    }

    @Override
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

    public List<ActivePlayer> findPlayersByTeam(@GameTeam.Team int team) {
        final List<ActivePlayer> teamPlayers = new ArrayList<>();

        for (ActivePlayer activePlayer : activePlayers) {
            if (activePlayer.getTeam() == team) {
                teamPlayers.add(activePlayer);
            }
        }
        return teamPlayers;
    }

    //TODO: unused, maybe remove?
    public List<ActivePlayer> findPlayersBySide(@GameSide.Side int side) {
        final List<ActivePlayer> sidePlayers = new ArrayList<>();

        for (ActivePlayer activePlayer : activePlayers) {
            if (activePlayer.getSide() == side) {
                sidePlayers.add(activePlayer);
            }
        }
        return sidePlayers;
    }

    public ActivePlayer getCurrentKilledPlayer() {
        if (deathList.size() > 0) {
            return getActivePlayer(deathList.get(0));
        } else {
            return null;
        }
    }

    public boolean isLover(ActivePlayer activePlayer) {
        return lovers != null
                && (lovers.first == activePlayer.getPlayerId()
                || lovers.second == activePlayer.getPlayerId());
    }

    private boolean checkGameOver() {
        ActivePlayer firstAlivePlayer = null;
        boolean gameOver = true;

        List<ActivePlayer> galenErsoList = findPlayersByTeam(GameTeam.GALEN_ERSO);

        if (!galenErsoList.isEmpty()
                && !galenErsoList.get(0).isAlive()
                && getCurrentRound() == 1
                && !isRoundActive()) {

            firstAlivePlayer = galenErsoList.get(0);
        } else {
            for (ActivePlayer activePlayer : activePlayers) {
                if (!activePlayer.isAlive()) {
                    continue;
                }

                if (firstAlivePlayer == null) {
                    firstAlivePlayer = activePlayer;
                } else if (firstAlivePlayer.getTeam() != activePlayer.getTeam()) {
                    gameOver = false;
                    break;
                }
            }
        }

        if (gameOver && firstAlivePlayer != null) {
            setWinningTeam(firstAlivePlayer.getTeam());
        }

        return gameOver;
    }

    @GameTeam.Team
    public int getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(@GameTeam.Team int winningTeam) {
        this.winningTeam = winningTeam;
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
        dest.writeList(this.deathList);
        dest.writeList(this.switchRolesList);
        dest.writeLong(lovers != null ? lovers.first : -1);
        dest.writeLong(lovers != null ? lovers.second : -1);
        dest.writeByte(this.bobaMedPackUsed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.bobaRocketUsed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.rocketAlreadySelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.dayCycle);
        dest.writeInt(this.winningTeam);
    }

    @SuppressWarnings("ResourceType")
    protected MainGame(Parcel in) {
        this.currentNight = in.readInt();
        this.currentUseCaseStep = in.readInt();
        this.currentTurn = in.readInt();
        this.gameStartTimeStamp = in.readLong();
        this.activePlayers = in.createTypedArrayList(ActivePlayer.CREATOR);
        this.deathList = new ArrayList<>();
        in.readList(this.deathList, Long.class.getClassLoader());
        this.switchRolesList = new ArrayList<>();
        in.readList(this.switchRolesList, Long.class.getClassLoader());
        final long lover1 = in.readLong();
        final long lover2 = in.readLong();
        if (lover1 != -1 && lover2 != -1) {
            this.lovers = new Pair<>(lover1, lover2);
        }
        this.bobaMedPackUsed = in.readByte() != 0;
        this.bobaRocketUsed = in.readByte() != 0;
        this.rocketAlreadySelected = in.readByte() != 0;
        this.dayCycle = in.readInt();
        this.winningTeam = in.readInt();
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
