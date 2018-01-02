package com.grietenenknapen.sithandroid.maingame.multiplayer.server;

import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.util.Log;

import com.grietenenknapen.sithandroid.maingame.multiplayer.DeviceSocketHandler;
import com.grietenenknapen.sithandroid.maingame.multiplayer.DeviceSocketManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GroupOwnerSocketRunner extends Thread {

    private static final String TAG = "GroupOwnerSocketRunner";

    private ServerSocket socket = null;
    private final int THREAD_COUNT_INITIAL = 10;
    private final int THREAD_COUNT_MAX = 16;
    private final DeviceSocketHandler handler;
    private volatile boolean running;
    private PowerManager.WakeLock wakeLock;
    private WifiManager.WifiLock wifiLock;

    public GroupOwnerSocketRunner(DeviceSocketHandler handler,
                                  PowerManager.WakeLock wakeLock,
                                  WifiManager.WifiLock wifiLock) throws IOException {

        this.wakeLock = wakeLock;
        this.wifiLock = wifiLock;
        try {
            this.socket = new ServerSocket(WifiDirectGameServerManager.SERVER_PORT);
            this.handler = handler;
            Log.d("GroupOwnerSocketHandler", "Socket Started");
        } catch (IOException e) {
            pool.shutdownNow();
            throw e;
        }
    }

    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_COUNT_INITIAL, THREAD_COUNT_MAX, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public void run() {
        running = true;
        try {
            while (running) {
                // A blocking operation. Initiate a DeviceSocketManager instance when
                // there is a new connection
                pool.execute(new DeviceSocketManager(socket.accept(), handler, wifiLock, wakeLock)); //TODO: check if there is a problem with this on lollipop
                Log.d(TAG, "Launching the I/O handler");
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            handler.sendRunnerFailedMessage();
        } finally {
            shutDownPool();
        }
    }

    private void closeSocket() {
        try {
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException ioe) {
            handler.sendRunnerFailedMessage();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void shutDown() {
        running = false;
        shutDownPool();
        closeSocket();
    }

    private void shutDownPool() {
        if (!pool.isShutdown()) {
            pool.shutdownNow();
        }
    }

}