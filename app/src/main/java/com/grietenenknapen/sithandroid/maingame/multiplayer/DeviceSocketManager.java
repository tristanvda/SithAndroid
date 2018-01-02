package com.grietenenknapen.sithandroid.maingame.multiplayer;

import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.grietenenknapen.sithandroid.application.GsonSingleton;
import com.grietenenknapen.sithandroid.maingame.multiplayer.helper.WifiPackageHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceSocketManager implements Runnable {

    private static final int KEEP_ALIVE_INTERVAL = 1000 * 60 * 2;

    private final Socket socket;
    private final DeviceSocketHandler handler;
    private final WifiManager.WifiLock wifiLock;
    private final PowerManager.WakeLock wakeLock;
    private boolean connected;
    private volatile boolean running;

    private InputStream iStream;
    private OutputStream oStream;
    private static final String TAG = DeviceSocketManager.class.getName();

    public DeviceSocketManager(Socket socket,
                               DeviceSocketHandler handler,
                               final WifiManager.WifiLock wifiLock,
                               final PowerManager.WakeLock wakeLock) {
        this.socket = socket;
        this.handler = handler;
        this.wifiLock = wifiLock;
        this.wakeLock = wakeLock;
        configureSocket();
    }

    private void configureSocket() {
        try {
            socket.setKeepAlive(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private TimerTask keepAliveTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (socket.isConnected() && running && connected) {
                try {
                    new DataOutputStream(oStream).writeUTF("{Keep-Alive=\"Keep-Alive\"}");
                    Log.e(TAG, "KeepAlive sent");
                } catch (IOException e) {
                    Log.e(TAG, "Exception keepAlive", e);
                }
            } else {
                keepAliveTimerTask.cancel();
            }
        }
    };

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void run() {
        try {
            acquireLock();

            iStream = socket.getInputStream();
            oStream = socket.getOutputStream();

            handler.sendDeviceConnectedMessage(this);
            connected = true;
            running = true;

            new Timer().schedule(keepAliveTimerTask, KEEP_ALIVE_INTERVAL, KEEP_ALIVE_INTERVAL);

            while (running) {
                try {
                    // Read from the InputStream
                    final DataInputStream br = new DataInputStream(iStream);
                    final String json = br.readUTF();

                    if (json.length() == 0) {
                        break;
                    }

                    final Class<? extends WifiPackage> clazz = WifiPackageHelper.findWifiPackageClass(json);
                    if (clazz == null) {
                        //It's not a wifi package, it's possible to do other stuff here
                        continue;
                    }

                    try {
                        final WifiPackage wifiPackage = GsonSingleton.getInstance().fromJson(json, clazz);
                        Log.e(TAG, "Package received: " + json);
                        handler.sendDeviceWifiPackageMessage(this, wifiPackage);
                    } catch (JsonSyntaxException e) {
                        Log.e(TAG, "Could not parse json object to wifi package");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "read buffer failed", e);
                    running = false;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            connected = false;
            try {
                socket.close();
            } catch (IOException e) {
                Log.d(TAG, e.getMessage(), e);
            }
            keepAliveTimerTask.cancel();
            handler.sendDeviceDisconnectedMessage(this);
            releaseLock();
        }
    }

    public void shutDown() {
        try {
            oStream.close();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
        }
        running = false;
    }

    private void acquireLock() {
        wakeLock.setReferenceCounted(true);
        wifiLock.setReferenceCounted(true);
        wakeLock.acquire(30 * 60 * 1000L /*30 minutes*/);
        wifiLock.acquire();
    }

    private void releaseLock() {
        if (wifiLock.isHeld()) {
            wifiLock.release();
        }
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    public void write(final WifiPackage wifiPackage) {

        //todo: !!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: CONVERT TO THREAD POOL EXECUTIONER
        //TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        if (!connected) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String json = GsonSingleton.getInstance().toJson(wifiPackage);
                    new DataOutputStream(oStream).writeUTF(json);
                    Log.e(TAG, "Package sent: " + json);
                } catch (IOException e) {
                    Log.e(TAG, "Exception during write", e);
                }
            }
        }).start();
    }
}
