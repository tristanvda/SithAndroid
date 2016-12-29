package com.grietenenknapen.sithandroid.game.usecase;


import android.os.Parcel;
import android.os.Parcelable;

public class FlowDetails implements Parcelable {
    private final int round;
    private final int turn;
    private final int step;

    public FlowDetails(int round, int step, int turn) {
        this.round = round;
        this.step = step;
        this.turn = turn;
    }

    public int getRound() {
        return round;
    }

    public int getStep() {
        return step;
    }

    public int getTurn() {
        return turn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowDetails that = (FlowDetails) o;

        if (round != that.round) return false;
        if (turn != that.turn) return false;
        return step == that.step;

    }

    @Override
    public int hashCode() {
        int result = round;
        result = 31 * result + turn;
        result = 31 * result + step;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.round);
        dest.writeInt(this.turn);
        dest.writeInt(this.step);
    }

    protected FlowDetails(Parcel in) {
        this.round = in.readInt();
        this.turn = in.readInt();
        this.step = in.readInt();
    }

    public static final Parcelable.Creator<FlowDetails> CREATOR = new Parcelable.Creator<FlowDetails>() {
        @Override
        public FlowDetails createFromParcel(Parcel source) {
            return new FlowDetails(source);
        }

        @Override
        public FlowDetails[] newArray(int size) {
            return new FlowDetails[size];
        }
    };
}
