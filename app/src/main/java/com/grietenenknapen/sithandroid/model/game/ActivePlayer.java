package com.grietenenknapen.sithandroid.model.game;

import android.os.Parcel;
import android.os.Parcelable;

import com.grietenenknapen.sithandroid.model.database.SithCard;

public class ActivePlayer implements Parcelable {
    private SithCard sithCard;
    @GameTeam.Team
    private int team;
    @GameSide.Side
    private int side;
    private boolean alive;

    private long player_id;
    private String name;
    private String telephoneNumber;

    private ActivePlayer(Builder builder) {
        alive = builder.alive;
        sithCard = builder.sithCard;
        team = builder.team;
        side = builder.side;
        player_id = builder.player_id;
        name = builder.name;
        telephoneNumber = builder.telephoneNumber;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(ActivePlayer copy) {
        Builder builder = new Builder();
        builder.alive = copy.alive;
        builder.sithCard = copy.sithCard;
        builder.team = copy.team;
        builder.side = copy.side;
        builder.player_id = copy.player_id;
        builder.name = copy.name;
        builder.telephoneNumber = copy.telephoneNumber;
        return builder;
    }

    public SithCard getSithCard() {
        return sithCard;
    }

    public void setSithCard(SithCard sithCard) {
        this.sithCard = sithCard;
    }

    @GameSide.Side
    public int getSide() {
        return side;
    }

    @GameTeam.Team
    public int getTeam() {
        return team;
    }

    public void setTeam(@GameTeam.Team int team) {
        this.team = team;
    }

    public void setSide(@GameSide.Side int side) {
        this.side = side;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public long getPlayerId() {
        return player_id;
    }

    public void setPlayerId(int player_id) {
        this.player_id = player_id;
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


    /**
     * {@code ActivePlayer} builder static inner class.
     */
    public static final class Builder {
        private boolean alive;
        private SithCard sithCard;
        private int team;
        private int side;
        private long player_id;
        private String name;
        private String telephoneNumber;

        private Builder() {
        }

        /**
         * Sets the {@code alive} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param alive the {@code alive} to set
         * @return a reference to this Builder
         */
        public Builder alive(boolean alive) {
            this.alive = alive;
            return this;
        }

        /**
         * Sets the {@code sithCard} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param sithCard the {@code sithCard} to set
         * @return a reference to this Builder
         */
        public Builder sithCard(SithCard sithCard) {
            this.sithCard = sithCard;
            return this;
        }

        /**
         * Sets the {@code team} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param team the {@code team} to set
         * @return a reference to this Builder
         */
        public Builder team(int team) {
            this.team = team;
            return this;
        }

        /**
         * Sets the {@code side} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param side the {@code side} to set
         * @return a reference to this Builder
         */
        public Builder side(int side) {
            this.side = side;
            return this;
        }

        /**
         * Sets the {@code player_id} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param player_id the {@code player_id} to set
         * @return a reference to this Builder
         */
        public Builder player_id(long player_id) {
            this.player_id = player_id;
            return this;
        }

        /**
         * Sets the {@code name} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param name the {@code name} to set
         * @return a reference to this Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the {@code telephoneNumber} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param telephoneNumber the {@code telephoneNumber} to set
         * @return a reference to this Builder
         */
        public Builder telephoneNumber(String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        /**
         * Returns a {@code ActivePlayer} built from the parameters previously set.
         *
         * @return a {@code ActivePlayer} built with parameters of this {@code ActivePlayer.Builder}
         */
        public ActivePlayer build() {
            return new ActivePlayer(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.sithCard, flags);
        dest.writeInt(this.team);
        dest.writeInt(this.side);
        dest.writeByte(this.alive ? (byte) 1 : (byte) 0);
        dest.writeLong(this.player_id);
        dest.writeString(this.name);
        dest.writeString(this.telephoneNumber);
    }

    @SuppressWarnings("resourceType")
    protected ActivePlayer(Parcel in) {
        this.sithCard = in.readParcelable(SithCard.class.getClassLoader());
        this.team = in.readInt();
        this.side = in.readInt();
        this.alive = in.readByte() != 0;
        this.player_id = in.readLong();
        this.name = in.readString();
        this.telephoneNumber = in.readString();
    }

    public static final Creator<ActivePlayer> CREATOR = new Creator<ActivePlayer>() {
        @Override
        public ActivePlayer createFromParcel(Parcel source) {
            return new ActivePlayer(source);
        }

        @Override
        public ActivePlayer[] newArray(int size) {
            return new ActivePlayer[size];
        }
    };
}
