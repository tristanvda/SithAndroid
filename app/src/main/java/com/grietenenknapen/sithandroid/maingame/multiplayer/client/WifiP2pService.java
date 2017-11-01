package com.grietenenknapen.sithandroid.maingame.multiplayer.client;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class WifiP2pService implements Parcelable {
    public WifiP2pDevice device;
    public String instanceName = null;
    public String serviceRegistrationType = null;
    public String networkName = null;
    public String passPhrase = null;
    public String deviceAddress = null;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.device, flags);
        dest.writeString(this.instanceName);
        dest.writeString(this.serviceRegistrationType);
        dest.writeString(this.networkName);
        dest.writeString(this.passPhrase);
        dest.writeString(this.deviceAddress);
    }

    public WifiP2pService() {
    }

    protected WifiP2pService(Parcel in) {
        this.device = in.readParcelable(WifiP2pDevice.class.getClassLoader());
        this.instanceName = in.readString();
        this.serviceRegistrationType = in.readString();
        this.networkName = in.readString();
        this.passPhrase = in.readString();
        this.deviceAddress = in.readString();
    }

    public static final Creator<WifiP2pService> CREATOR = new Creator<WifiP2pService>() {
        @Override
        public WifiP2pService createFromParcel(Parcel source) {
            return new WifiP2pService(source);
        }

        @Override
        public WifiP2pService[] newArray(int size) {
            return new WifiP2pService[size];
        }
    };
}
