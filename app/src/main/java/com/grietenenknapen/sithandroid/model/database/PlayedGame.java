package com.grietenenknapen.sithandroid.model.database;


import java.util.List;

import nl.qbusict.cupboard.annotation.Ignore;

public class PlayedGame {
    public static final String START_TIME = "startTime";
    public static final String STOP_TIME = "stopTime";
    public static final String ROUNDS = "rounds";

    private Long _id;
    private Long startTime;
    private Long stopTime;
    private Integer rounds;
    @Ignore
    private List<GamePlayer> gamePlayers;

    private PlayedGame(Builder builder) {
        _id = builder._id;
        setStartTime(builder.startTime);
        setStopTime(builder.stopTime);
        setRounds(builder.rounds);
        setGamePlayers(builder.gamePlayers);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(PlayedGame copy) {
        Builder builder = new Builder();
        builder._id = copy._id;
        builder.startTime = copy.startTime;
        builder.stopTime = copy.stopTime;
        builder.rounds = copy.rounds;
        builder.gamePlayers = copy.gamePlayers;
        return builder;
    }

    public Long getId() {
        return _id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }


    /**
     * {@code PlayedGame} builder static inner class.
     */
    public static final class Builder {
        private long _id;
        private long startTime;
        private long stopTime;
        private int rounds;
        private List<GamePlayer> gamePlayers;

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
         * Sets the {@code startTime} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code startTime} to set
         * @return a reference to this Builder
         */
        public Builder startTime(long val) {
            startTime = val;
            return this;
        }

        /**
         * Sets the {@code stopTime} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code stopTime} to set
         * @return a reference to this Builder
         */
        public Builder stopTime(long val) {
            stopTime = val;
            return this;
        }

        /**
         * Sets the {@code rounds} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code rounds} to set
         * @return a reference to this Builder
         */
        public Builder rounds(int val) {
            rounds = val;
            return this;
        }

        /**
         * Sets the {@code gamePlayers} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code gamePlayers} to set
         * @return a reference to this Builder
         */
        public Builder gamePlayers(List<GamePlayer> val) {
            gamePlayers = val;
            return this;
        }

        /**
         * Returns a {@code PlayedGame} built from the parameters previously set.
         *
         * @return a {@code PlayedGame} built with parameters of this {@code PlayedGame.Builder}
         */
        public PlayedGame build() {
            return new PlayedGame(this);
        }
    }
}
