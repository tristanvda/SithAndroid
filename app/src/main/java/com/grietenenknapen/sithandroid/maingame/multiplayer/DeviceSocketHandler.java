package com.grietenenknapen.sithandroid.maingame.multiplayer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class DeviceSocketHandler extends Handler {

    private static final int HANDLE_TYPE_DEVICE_CONNECTED = 1;
    private static final int HANDLE_TYPE_DEVICE_WIFI_PACKAGE = 2;
    private static final int HANDLE_TYPE_DEVICE_DISCONNECTED = 3;
    private static final int HANDLE_TYPE_RUNNER_FAIL = 4;

    private final GroupOwnerHandlerListener listener;

    public DeviceSocketHandler(final GroupOwnerHandlerListener listener) {
        super(Looper.getMainLooper());
        this.listener = listener;
    }

    @Override
    public void handleMessage(final Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case HANDLE_TYPE_DEVICE_CONNECTED:
                listener.onDeviceConnected((DeviceSocketManager) msg.obj);
                break;
            case HANDLE_TYPE_DEVICE_WIFI_PACKAGE:
                ClientWifiPackage clientWifiPackage = (ClientWifiPackage) msg.obj;
                listener.onDevicePackageReceived(clientWifiPackage.deviceSocketManager, clientWifiPackage.wifiPackage);
                break;
            case HANDLE_TYPE_DEVICE_DISCONNECTED:
                listener.onDeviceDisconnected((DeviceSocketManager) msg.obj);
                break;
            case HANDLE_TYPE_RUNNER_FAIL:
                listener.onRunnerFailed();
                break;
        }
    }

    public void sendDeviceConnectedMessage(DeviceSocketManager deviceSocketManager) {
        this.obtainMessage(DeviceSocketHandler.HANDLE_TYPE_DEVICE_CONNECTED, deviceSocketManager).sendToTarget();
    }

    public void sendDeviceWifiPackageMessage(DeviceSocketManager deviceSocketManager, WifiPackage wifiPackage) {
        this.obtainMessage(DeviceSocketHandler.HANDLE_TYPE_DEVICE_WIFI_PACKAGE, new ClientWifiPackage(deviceSocketManager, wifiPackage)).sendToTarget();
    }

    public void sendDeviceDisconnectedMessage(DeviceSocketManager deviceSocketManager) {
        this.obtainMessage(DeviceSocketHandler.HANDLE_TYPE_DEVICE_DISCONNECTED, deviceSocketManager).sendToTarget();
    }

    public void sendRunnerFailedMessage() {
        this.obtainMessage(DeviceSocketHandler.HANDLE_TYPE_RUNNER_FAIL).sendToTarget();
    }

    public interface GroupOwnerHandlerListener {

        void onDeviceConnected(DeviceSocketManager deviceSocketManager);

        void onDevicePackageReceived(DeviceSocketManager deviceSocketManager, WifiPackage wifiPackage);

        void onDeviceDisconnected(DeviceSocketManager deviceSocketManager);

        void onRunnerFailed();

    }

    private static class ClientWifiPackage {

        DeviceSocketManager deviceSocketManager;
        WifiPackage wifiPackage;

        ClientWifiPackage(final DeviceSocketManager deviceSocketManager, final WifiPackage wifiPackage) {
            this.deviceSocketManager = deviceSocketManager;
            this.wifiPackage = wifiPackage;
        }
    }
}
