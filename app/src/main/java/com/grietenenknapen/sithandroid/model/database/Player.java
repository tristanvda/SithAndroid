package com.grietenenknapen.sithandroid.model.database;

import java.util.List;

import nl.qbusict.cupboard.annotation.Ignore;

public class Player {
    public static final String KEY_NAME = "name";
    public static final String KEY_TELEPHONE_NUMBER = "telephoneNumber";
    public static final String KEY_WINS = "wins";
    public static final String KEY_LOSSES = "losses";

    private Long _id;
    private String name;
    private String telephoneNumber;
    private Integer wins;
    private Integer losses;
    @Ignore
    private List<Favourite> favourites;

    private Player(Builder builder) {
        _id = builder._id;
        setName(builder.name);
        setTelephoneNumber(builder.telephoneNumber);
        setWins(builder.wins);
        setLosses(builder.losses);
        setFavourites(builder.favourites);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Player copy) {
        Builder builder = new Builder();
        builder._id = copy._id;
        builder.name = copy.name;
        builder.telephoneNumber = copy.telephoneNumber;
        builder.wins = copy.wins;
        builder.losses = copy.losses;
        builder.favourites = copy.favourites;
        return builder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public long getId() {
        return _id;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public List<Favourite> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Favourite> favourites) {
        this.favourites = favourites;
    }


    /**
     * {@code Player} builder static inner class.
     */
    public static final class Builder {
        private long _id;
        private String name;
        private String telephoneNumber;
        private int wins;
        private int losses;
        private List<Favourite> favourites;

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
         * Sets the {@code name} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code name} to set
         * @return a reference to this Builder
         */
        public Builder name(String val) {
            name = val;
            return this;
        }

        /**
         * Sets the {@code telephoneNumber} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code telephoneNumber} to set
         * @return a reference to this Builder
         */
        public Builder telephoneNumber(String val) {
            telephoneNumber = val;
            return this;
        }

        /**
         * Sets the {@code wins} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code wins} to set
         * @return a reference to this Builder
         */
        public Builder wins(int val) {
            wins = val;
            return this;
        }

        /**
         * Sets the {@code losses} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code losses} to set
         * @return a reference to this Builder
         */
        public Builder losses(int val) {
            losses = val;
            return this;
        }

        /**
         * Sets the {@code favourites} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code favourites} to set
         * @return a reference to this Builder
         */
        public Builder favourites(List<Favourite> val) {
            favourites = val;
            return this;
        }

        /**
         * Returns a {@code Player} built from the parameters previously set.
         *
         * @return a {@code Player} built with parameters of this {@code Player.Builder}
         */
        public Player build() {
            return new Player(this);
        }
    }
}
