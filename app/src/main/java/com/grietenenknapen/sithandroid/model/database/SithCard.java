package com.grietenenknapen.sithandroid.model.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.grietenenknapen.sithandroid.model.game.GameCardType;

public class SithCard implements Parcelable {
    public static final String KEY_NAME = "name";
    public static final String KEY_CARD_TYPE = "cardType";
    public static final String IMAGE_ID = "imageId";
    public static final String SOUND_ID = "soundResId";
    public static final String COLOR_ID = "colorResId";

    private Long _id;
    private String name;
    private String nameResId;
    private String imageResId;
    private String soundResId;
    private String colorResId;

    @GameCardType.CardType
    private Integer cardType;

    private SithCard(Builder builder) {
        _id = builder._id;
        name = builder.name;
        nameResId = builder.nameResId;
        imageResId = builder.imageResId;
        soundResId = builder.soundResId;
        colorResId = builder.colorResId;
        cardType = builder.cardType;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(SithCard copy) {
        Builder builder = new Builder();
        builder._id = copy._id;
        builder.name = copy.name;
        builder.nameResId = copy.nameResId;
        builder.imageResId = copy.imageResId;
        builder.soundResId = copy.soundResId;
        builder.colorResId = copy.colorResId;
        builder.cardType = copy.cardType;
        return builder;
    }


    public long getId() {
        return _id;
    }

    public String getNameResId() {
        return nameResId;
    }

    public String getImageResId() {
        return imageResId;
    }

    public String getSoundResId() {
        return soundResId;
    }

    public String getColorResId() {
        return colorResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @GameCardType.CardType
    public int getCardType() {
        return cardType;
    }

    public void setCardType(@GameCardType.CardType int cardType) {
        this.cardType = cardType;
    }


    /**
     * {@code SithCard} builder static inner class.
     */
    public static final class Builder {
        private Long _id;
        private String name;
        private String nameResId;
        private String imageResId;
        private String soundResId;
        private String colorResId;
        private Integer cardType;

        private Builder() {
        }

        /**
         * Sets the {@code _id} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param _id the {@code _id} to set
         * @return a reference to this Builder
         */
        public Builder _id(Long _id) {
            this._id = _id;
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
         * Sets the {@code nameResId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param nameResId the {@code nameResId} to set
         * @return a reference to this Builder
         */
        public Builder nameResId(String nameResId) {
            this.nameResId = nameResId;
            return this;
        }

        /**
         * Sets the {@code imageResId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param imageResId the {@code imageResId} to set
         * @return a reference to this Builder
         */
        public Builder imageResId(String imageResId) {
            this.imageResId = imageResId;
            return this;
        }

        /**
         * Sets the {@code soundResId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param soundResId the {@code soundResId} to set
         * @return a reference to this Builder
         */
        public Builder soundResId(String soundResId) {
            this.soundResId = soundResId;
            return this;
        }

        /**
         * Sets the {@code colorResId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param colorResId the {@code colorResId} to set
         * @return a reference to this Builder
         */
        public Builder colorResId(String colorResId) {
            this.colorResId = colorResId;
            return this;
        }

        /**
         * Sets the {@code cardType} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param cardType the {@code cardType} to set
         * @return a reference to this Builder
         */
        public Builder cardType(Integer cardType) {
            this.cardType = cardType;
            return this;
        }

        /**
         * Returns a {@code SithCard} built from the parameters previously set.
         *
         * @return a {@code SithCard} built with parameters of this {@code SithCard.Builder}
         */
        public SithCard build() {
            return new SithCard(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeString(this.name);
        dest.writeString(this.nameResId);
        dest.writeString(this.imageResId);
        dest.writeString(this.soundResId);
        dest.writeString(this.colorResId);
        dest.writeValue(this.cardType);
    }

    @SuppressWarnings("ResourceType")
    protected SithCard(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.nameResId = in.readString();
        this.imageResId = in.readString();
        this.soundResId = in.readString();
        this.colorResId = in.readString();
        this.cardType = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<SithCard> CREATOR = new Parcelable.Creator<SithCard>() {
        @Override
        public SithCard createFromParcel(Parcel source) {
            return new SithCard(source);
        }

        @Override
        public SithCard[] newArray(int size) {
            return new SithCard[size];
        }
    };
}
