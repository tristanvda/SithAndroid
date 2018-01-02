package com.grietenenknapen.sithandroid.maingame.multiplayer.client;

import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.util.Log;

import com.grietenenknapen.sithandroid.maingame.multiplayer.DeviceSocketHandler;
import com.grietenenknapen.sithandroid.maingame.multiplayer.DeviceSocketManager;
import com.grietenenknapen.sithandroid.maingame.multiplayer.server.WifiDirectGameServerManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSocketRunner extends Thread {

    private static final String TAG = "ClientSocketRunner";

    private final DeviceSocketHandler handler;
    private final String hostAddress;
    private final Socket socket;
    private final WifiManager.WifiLock wifiLock;
    private final PowerManager.WakeLock wakeLock;

    public ClientSocketRunner(final DeviceSocketHandler handler,
                              final InetAddress serverAddress,
                              final WifiManager.WifiLock wifiLock,
                              final PowerManager.WakeLock wakeLock) {

        this.wifiLock = wifiLock;
        this.wakeLock = wakeLock;
        this.socket = new Socket();
        this.handler = handler;
        this.hostAddress = serverAddress.getHostAddress();
    }

    public ClientSocketRunner(final DeviceSocketHandler handler,
                              final String hostAddress,
                              final WifiManager.WifiLock wifiLock,
                              final PowerManager.WakeLock wakeLock) {

        this.wifiLock = wifiLock;
        this.wakeLock = wakeLock;
        this.socket = new Socket();
        this.handler = handler;
        this.hostAddress = hostAddress;
    }

    @Override
    public void run() {
        super.run();
        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(hostAddress, WifiDirectGameServerManager.SERVER_PORT), 5000);
            Log.d(TAG, "Launching the I/O handler");
            new Thread(new DeviceSocketManager(socket, handler, wifiLock, wakeLock)).start();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            try {
                socket.close();
            } catch (IOException e1) {
                Log.d(TAG, e1.getMessage());
            }
            handler.sendRunnerFailedMessage();
        }
    }

    public void shutDown() {
        closeSocket();
    }

    private void closeSocket() {
        try {
            if (!socket.isClosed())
                socket.close();
        } catch (IOException ioe) {
            handler.sendRunnerFailedMessage();
        }
    }

    public DeviceSocketHandler getDeviceSocketHandler() {
        return handler;
    }

}
