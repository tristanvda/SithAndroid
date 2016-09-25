package com.grietenenknapen.sithandroid.model.database;


import com.grietenenknapen.sithandroid.model.game.GameCardType;

import nl.qbusict.cupboard.annotation.Ignore;

public class Favourite {
    public static final String TIMES_USED = "timesUsed";

    private Long _id;
    private Integer timesUsed;

    @Ignore
    private SithCard sithCard;

    private Favourite(Builder builder) {
        _id = builder._id;
        setTimesUsed(builder.timesUsed);
        setSithCard(builder.sithCard);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Favourite copy) {
        Builder builder = new Builder();
        builder._id = copy._id;
        builder.timesUsed = copy.timesUsed;
        builder.sithCard = copy.sithCard;
        return builder;
    }

    public long getId() {
        return _id;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public SithCard getSithCard() {
        return sithCard;
    }

    public void setSithCard(SithCard sithCard) {
        this.sithCard = sithCard;
    }


    /**
     * {@code Favourite} builder static inner class.
     */
    public static final class Builder {
        private long _id;
        private int timesUsed;
        private SithCard sithCard;

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
         * Sets the {@code timesUsed} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code timesUsed} to set
         * @return a reference to this Builder
         */
        public Builder timesUsed(int val) {
            timesUsed = val;
            return this;
        }

        /**
         * Sets the {@code sithCard} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code sithCard} to set
         * @return a reference to this Builder
         */
        public Builder sithCard(SithCard val) {
            sithCard = val;
            return this;
        }

        /**
         * Returns a {@code Favourite} built from the parameters previously set.
         *
         * @return a {@code Favourite} built with parameters of this {@code Favourite.Builder}
         */
        public Favourite build() {
            return new Favourite(this);
        }
    }
}
