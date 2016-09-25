package com.grietenenknapen.sithandroid.model.database.relations;


public class PlayerFavourite {
    public static final String PLAYER_ID = "playerId";
    public static final String FAVOURITE_ID = "favouriteId";

    private long _id;
    private long playerId;
    private long favouriteId;

    private PlayerFavourite(Builder builder) {
        _id = builder._id;
        playerId = builder.playerId;
        favouriteId = builder.favouriteId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(PlayerFavourite copy) {
        Builder builder = new Builder();
        builder._id = copy._id;
        builder.playerId = copy.playerId;
        builder.favouriteId = copy.favouriteId;
        return builder;
    }

    public long get_id() {
        return _id;
    }

    public long getPlayerId() {
        return playerId;
    }

    public long getFavouriteId() {
        return favouriteId;
    }

    /**
     * {@code PlayerFavourite} builder static inner class.
     */
    public static final class Builder {
        private long _id;
        private long playerId;
        private long favouriteId;

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
         * Sets the {@code playerId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code playerId} to set
         * @return a reference to this Builder
         */
        public Builder playerId(long val) {
            playerId = val;
            return this;
        }

        /**
         * Sets the {@code favouriteId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code favouriteId} to set
         * @return a reference to this Builder
         */
        public Builder favouriteId(long val) {
            favouriteId = val;
            return this;
        }

        /**
         * Returns a {@code PlayerFavourite} built from the parameters previously set.
         *
         * @return a {@code PlayerFavourite} built with parameters of this {@code PlayerFavourite.Builder}
         */
        public PlayerFavourite build() {
            return new PlayerFavourite(this);
        }
    }
}
