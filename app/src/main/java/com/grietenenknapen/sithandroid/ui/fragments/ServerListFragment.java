package com.grietenenknapen.sithandroid.ui.fragments;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.maingame.multiplayer.WifiDirectBroadcastReceiver;
import com.grietenenknapen.sithandroid.maingame.multiplayer.client.WifiP2pService;
import com.grietenenknapen.sithandroid.ui.CallbackPresenterFragment;
import com.grietenenknapen.sithandroid.ui.PresenterFactory;
import com.grietenenknapen.sithandroid.ui.adapters.ServerAdapter;
import com.grietenenknapen.sithandroid.ui.presenters.ServerListPresenter;
import com.grietenenknapen.sithandroid.ui.views.LightSaberLoadingView;
import com.grietenenknapen.sithandroid.util.ItemClickSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Looper.getMainLooper;

public class ServerListFragment extends CallbackPresenterFragment<ServerListPresenter, ServerListPresenter.View, ServerListFragment.Callback>
        implements ServerListPresenter.View {

    private static final String PRESENTER_TAG = "server_list_presenter";

    @BindView(R.id.listButton)
    ImageButton listButton;
    @BindView(R.id.listTitle)
    TextView titleTextView;
    @BindView(R.id.listRecyclerView)
    RecyclerView serverRecyclerView;
    @BindView(R.id.saberLoadingView)
    LightSaberLoadingView lightSaberLoadingView;
    @BindView(R.id.emptyText)
    TextView emptyText;

    private ServerAdapter adapter;
    private final WifiDirectBroadcastReceiver receiver = new WifiDirectBroadcastReceiver();
    private final IntentFilter intentFilter = new IntentFilter();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLayout();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
    }

    private void initLayout() {
        listButton.setVisibility(View.VISIBLE);
        listButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_force_mode));
        titleTextView.setVisibility(View.GONE);
        final int listButtonSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        listButton.getLayoutParams().height = listButtonSize;
        listButton.getLayoutParams().width = listButtonSize;
        adapter = new ServerAdapter();
        serverRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        serverRecyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(serverRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(final RecyclerView recyclerView, final int position, final View v) {
                final WifiP2pService service = adapter.getItem(position);
                if (service != null) {
                    callback.onServerItemClicked(service);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().setWiFiDirectBroadcastReceiver(receiver);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.receiver.clearAllWifiDirectReceivers();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    protected String getPresenterTag() {
        return PRESENTER_TAG;
    }

    @Override
    protected PresenterFactory<ServerListPresenter> getPresenterFactory() {
        final Context appContext = getContext().getApplicationContext();
        WifiP2pManager manager = (WifiP2pManager) appContext.getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel = manager.initialize(appContext, getMainLooper(), null);
        return new ServerListFragmentFactory(manager, channel);
    }

    @Override
    protected ServerListPresenter.View getPresenterView() {
        return this;
    }

    @Override
    public void showServers(final List<WifiP2pService> services) {
        adapter.setData(services, true);
    }

    @Override
    public void showLoading(final boolean forceMode) {
        if (forceMode) {
            lightSaberLoadingView.startForceAnimation();
        }
        lightSaberLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        lightSaberLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyMessage(@StringRes final int stringRes) {
        emptyText.setVisibility(View.VISIBLE);
        emptyText.setText(getString(stringRes));
        serverRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyMessage() {
        emptyText.setVisibility(View.GONE);
        serverRecyclerView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.listButton)
    public void onForceButtonClicked() {
        getPresenter().startBoostSearch();
    }

    public interface Callback {

        void onServerItemClicked(@NonNull final WifiP2pService wifiP2pService);
    }

    private static final class ServerListFragmentFactory implements PresenterFactory<ServerListPresenter> {
        private final WifiP2pManager manager;
        private final WifiP2pManager.Channel channel;

        private ServerListFragmentFactory(final WifiP2pManager manager, final WifiP2pManager.Channel channel) {
            this.manager = manager;
            this.channel = channel;
        }

        @Override
        public ServerListPresenter createPresenter() {
            return new ServerListPresenter(manager, channel);
        }
    }
}
