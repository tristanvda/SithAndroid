package com.grietenenknapen.sithandroid.ui.presenters;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.util.Log;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiDirectBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiDirectReceiverListenerAdapter;
import com.grietenenknapen.sithandroid.maingame.multiplayer.client.WifiP2pService;
import com.grietenenknapen.sithandroid.maingame.multiplayer.server.WifiDirectGameServerManager;
import com.grietenenknapen.sithandroid.ui.Presenter;
import com.grietenenknapen.sithandroid.ui.PresenterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerListPresenter extends Presenter<ServerListPresenter.View> {

    private static final String TAG = ServerListPresenter.class.getName();
    private static final long DNS_SERVICE_INTERVAL = 1000 * 30;
    final HashMap<String, TxRecord> txServices = new HashMap<>();

    private final WifiP2pManager manager;
    private final WifiP2pManager.Channel channel;
    private List<WifiP2pService> services = new ArrayList<>();
    private WifiP2pDnsSdServiceRequest serviceRequest;
    private Handler intervalHandler = new Handler();
    private boolean wifiP2PEnabled = false;

    private static class TxRecord {
        String networkName;
        String passPhrase;
        String deviceName;
        String deviceAddress;
    }

    public ServerListPresenter(final WifiP2pManager manager, final WifiP2pManager.Channel channel) {
        this.manager = manager;
        this.channel = channel;
    }

    @Override
    protected void onViewBound() {
        if (manager == null) {
            getView().showEmptyMessage(R.string.server_failed_p2p_not_supported);
            return;
        }

        if (!wifiP2PEnabled) {
            getView().showEmptyMessage(R.string.p2p_disabled_feature);
            return;
        }

        if (!services.isEmpty()) {
            getView().showServers(services);
            return;
        }

        restartServiceDiscovery(false);
    }

    private WifiDirectReceiverListenerAdapter wifiDirectReceiverListener = new WifiDirectReceiverListenerAdapter() {

        @Override
        public void p2pStateEnabled() {
            wifiP2PEnabled = true;
            restartServiceDiscovery(false);
        }

        @Override
        public void p2pStateDisabled() {
            wifiP2PEnabled = false;
            if (getView() != null) {
                clearServiceDiscovery();
                getView().hideLoading();
                getView().showEmptyMessage(R.string.p2p_disabled_feature);
            }
        }
    };

    public void startBoostSearch() {
        restartServiceDiscovery(true);
    }

    public void setWiFiDirectBroadcastReceiver(WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver) {
        wifiDirectBroadcastReceiver.addWifiDirectReceiver(wifiDirectReceiverListener);
    }

    private void restartIntervalHandler() {
        intervalHandler.removeCallbacksAndMessages(null);
        intervalHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                restartServiceDiscovery(false);
            }
        }, DNS_SERVICE_INTERVAL);
    }

    private void restartServiceDiscovery(final boolean boost) {
        if (manager == null) {
            return;
        }
        if (boost || services.isEmpty()) {
            getView().showLoading(boost);
        }
        clearServiceDiscovery();
        getView().hideEmptyMessage();
        manager.setDnsSdResponseListeners(channel,
                new WifiP2pManager.DnsSdServiceResponseListener() {

                    @Override
                    public void onDnsSdServiceAvailable(String instanceName,
                                                        String registrationType, WifiP2pDevice srcDevice) {

                        if (instanceName.equalsIgnoreCase(WifiDirectGameServerManager.SERVICE_NAME)
                                && txServices.containsKey(srcDevice.deviceAddress)) {

                            final TxRecord txRecord = txServices.get(srcDevice.deviceAddress);
                            WifiP2pService service = new WifiP2pService();
                            srcDevice.deviceName = txRecord.deviceName;
                            service.networkName = txRecord.networkName;
                            service.passPhrase = txRecord.passPhrase;
                            service.deviceAddress = txRecord.deviceAddress;
                            service.device = srcDevice;
                            service.instanceName = instanceName;
                            service.serviceRegistrationType = registrationType;
                            services.add(service);
                            if (getView() != null) {
                                getView().hideLoading();
                                getView().hideEmptyMessage();
                                getView().showServers(services);
                            }
                        }

                    }
                }, new WifiP2pManager.DnsSdTxtRecordListener() {

                    /**
                     * A new TXT record is available. Pick up the advertised
                     * buddy name.
                     */
                    @Override
                    public void onDnsSdTxtRecordAvailable(
                            String fullDomainName, Map<String, String> record,
                            WifiP2pDevice device) {
                        //Not implemented
                        if (record.containsKey(WifiDirectGameServerManager.INFO_SERVICE_NAME)
                                && record.containsKey(WifiDirectGameServerManager.INFO_DEVICE_NAME)
                                && record.containsKey(WifiDirectGameServerManager.INFO_NETWORK_NAME)
                                && record.containsKey(WifiDirectGameServerManager.INFO_PASS_PHRASE)
                                && record.containsKey(WifiDirectGameServerManager.INFO_DEVICE_ADDRESS)) {

                            if (record.get(WifiDirectGameServerManager.INFO_SERVICE_NAME)
                                    .equals(WifiDirectGameServerManager.SERVICE_NAME)) {

                                final TxRecord txRecord = new TxRecord();
                                txRecord.deviceName = record.get(WifiDirectGameServerManager.INFO_DEVICE_NAME);
                                txRecord.networkName = record.get(WifiDirectGameServerManager.INFO_NETWORK_NAME);
                                txRecord.passPhrase = record.get(WifiDirectGameServerManager.INFO_PASS_PHRASE);
                                txRecord.deviceAddress = record.get(WifiDirectGameServerManager.INFO_DEVICE_ADDRESS);
                                txServices.put(device.deviceAddress, txRecord);
                            }
                        }
                    }
                });

        // After attaching listeners, create a service request and initiate
        // discovery.
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        manager.addServiceRequest(channel, serviceRequest,
                new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Added service discovery request");
                    }

                    @Override
                    public void onFailure(int arg0) {
                        Log.d(TAG, "Added service discovery request");
                        if (getView() != null) {
                            getView().hideLoading();
                            getView().showEmptyMessage(R.string.service_discovery_failed);
                        }
                    }
                });
        manager.discoverServices(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "Service discovery initiated");
            }

            @Override
            public void onFailure(int arg0) {
                Log.d(TAG, "Service discovery failed");
                if (getView() != null) {
                    getView().hideLoading();
                    getView().showEmptyMessage(R.string.service_discovery_failed);
                }
            }
        });

        restartIntervalHandler();
    }

    private void clearServiceDiscovery() {
        if (manager == null) {
            return;
        }
        intervalHandler.removeCallbacksAndMessages(null);

        services.clear();
        txServices.clear();

        if (serviceRequest != null) {
            manager.removeServiceRequest(channel, serviceRequest, null);
        }
        manager.clearServiceRequests(channel, null);
        manager.stopPeerDiscovery(channel, null);
    }

    @Override
    protected void onViewUnBound() {
        super.onViewUnBound();
        clearServiceDiscovery();
    }

    public interface View extends PresenterView {

        void showServers(List<WifiP2pService> services);

        void showLoading(boolean forceMode);

        void hideLoading();

        void showEmptyMessage(@StringRes int stringRes);

        void hideEmptyMessage();
    }
}
