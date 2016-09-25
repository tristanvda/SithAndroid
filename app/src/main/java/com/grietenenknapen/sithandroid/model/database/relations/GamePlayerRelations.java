package com.grietenenknapen.sithandroid.model.database.relations;

import java.util.Arrays;

public class GamePlayerRelations {
    public static final String PLAYED_GAME_ID = "playedGameId";
    public static final String GAME_PLAYER_ID = "gamePlayerId";
    public static final String SITH_CARD_IDS = "sithCardIds";

    private long _id;
    private long playedGameId;
    private long gamePlayerId;
    private String sithCardIds;

    private GamePlayerRelations(Builder builder) {
        _id = builder._id;
        playedGameId = builder.playedGameId;
        gamePlayerId = builder.gamePlayerId;
        sithCardIds = Arrays.toString(builder.sithCardIds);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(GamePlayerRelations copy) {
        Builder builder = new Builder();
        builder._id = copy._id;
        builder.playedGameId = copy.playedGameId;
        builder.gamePlayerId = copy.gamePlayerId;
        builder.sithCardIds = copy.getSithCardIds();
        return builder;
    }

    public long getId() {
        return _id;
    }

    public long getPlayedGameId() {
        return playedGameId;
    }

    public long getGamePlayerId() {
        return gamePlayerId;
    }

    public long[] getSithCardIds() {
        String[] s = sithCardIds.split(",");
        long[] ids = new long[s.length];
        for (int curr = 0; curr < s.length; curr++)
            ids[curr] = Long.parseLong(s[curr]);
        return ids;
    }


    /**
     * {@code GamePlayerRelations} builder static inner class.
     */
    public static final class Builder {
        private long _id;
        private long playedGameId;
        private long gamePlayerId;
        private long[] sithCardIds;

        private Builder() {
        }

        /**
         * Sets the {@code _id} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code _id} to set
         * @return a reference to this Builder
         */
        public Builder _id(long val) {
            _id = val;
            return this;
        }

        /**
         * Sets the {@code playedGameId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code playedGameId} to set
         * @return a reference to this Builder
         */
        public Builder playedGameId(long val) {
            playedGameId = val;
            return this;
        }

        /**
         * Sets the {@code gamePlayerId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code gamePlayerId} to set
         * @return a reference to this Builder
         */
        public Builder gamePlayerId(long val) {
            gamePlayerId = val;
            return this;
        }

        /**
         * Sets the {@code sithCardIds} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code sithCardIds} to set
         * @return a reference to this Builder
         */
        public Builder sithCardIds(long[] val) {
            sithCardIds = val;
            return this;
        }

        /**
         * Returns a {@code GamePlayerRelations} built from the parameters previously set.
         *
         * @return a {@code GamePlayerRelations} built with parameters of this {@code GamePlayerRelations.Builder}
         */
        public GamePlayerRelations build() {
            return new GamePlayerRelations(this);
        }
    }
}
