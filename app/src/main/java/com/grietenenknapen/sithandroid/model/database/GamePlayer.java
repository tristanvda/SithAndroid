package com.grietenenknapen.sithandroid.model.database;


import com.grietenenknapen.sithandroid.model.game.GameSide;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.annotation.Ignore;

public class GamePlayer {
    public static final String KEY_SIDE = "side";

    private Long _id;
    @GameSide.Side
    private Integer side;
    @Ignore
    private transient List<SithCard> sithCards;
    @Ignore
    private transient Player player;

    public GamePlayer(){
    }

    private GamePlayer(Builder builder) {
        _id = builder._id;
        setSide(builder.side);
        setSithCards(builder.sithCards);
        setPlayer(builder.player);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(GamePlayer copy) {
        Builder builder = new Builder();
        builder._id = copy._id;
        builder.side = copy.side;
        builder.sithCards = copy.sithCards;
        builder.player = copy.player;
        return builder;
    }

    public Long getId() {
        return _id;
    }

    @GameSide.Side
    public int getSide() {
        return side;
    }

    public void setSide(@GameSide.Side int side) {
        this.side = side;
    }

    public List<SithCard> getSithCards() {
        return sithCards;
    }

    public void setSithCards(List<SithCard> sithCards) {
        this.sithCards = sithCards;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long[] getSithCardIds() {
        long[] ids = new long[sithCards.size()];
        for (int i = 0; i < sithCards.size(); i++) {
            ids[i] = sithCards.get(i).getId();
        }
        return ids;
    }

    /**
     * {@code GamePlayer} builder static inner class.
     */
    public static final class Builder {
        private Long _id;
        @GameSide.Side
        private int side;
        private List<SithCard> sithCards = new ArrayList<>();
        private Player player;

        private Builder() {
        }

        /**
         * Sets the {@code _id} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code _id} to set
         * @return a reference to this Builder
         */
        public Builder _id(Long val) {
            _id = val;
            return this;
        }

        /**
         * Sets the {@code side} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code side} to set
         * @return a reference to this Builder
         */
        public Builder side(@GameSide.Side
                            int val) {
            side = val;
            return this;
        }

        /**
         * Sets the {@code sithCards} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code sithCards} to set
         * @return a reference to this Builder
         */
        public Builder sithCards(List<SithCard> val) {
            sithCards = val;
            return this;
        }

        /**
         * Sets the {@code player} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code player} to set
         * @return a reference to this Builder
         */
        public Builder player(Player val) {
            player = val;
            return this;
        }

        /**
         * Returns a {@code GamePlayer} built from the parameters previously set.
         *
         * @return a {@code GamePlayer} built with parameters of this {@code GamePlayer.Builder}
         */
        public GamePlayer build() {
            return new GamePlayer(this);
        }
    }
}
