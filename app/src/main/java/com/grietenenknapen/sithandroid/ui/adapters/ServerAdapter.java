package com.grietenenknapen.sithandroid.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.maingame.multiplayer.client.WifiP2pService;

import java.util.ArrayList;
import java.util.List;

public class ServerAdapter extends AnimateAdapter<WifiP2pService, ServerAdapter.DeviceViewHolder> {
    private List<WifiP2pService> items = new ArrayList<>();

    public WifiP2pService getItem(final int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public void setData(final List<WifiP2pService> items, final boolean animate) {
        this.items = items;
        super.setData(items, animate);
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_phone_server, parent, false));
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, final int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(final int position) {
        return (items.get(position).instanceName + items.get(position).device.deviceName).hashCode();
    }

    final static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private final TextView serverName;

        DeviceViewHolder(final View itemView) {
            super(itemView);
            serverName = (TextView) itemView.findViewById(R.id.serverName);
        }

        void onBind(WifiP2pService service) {
            serverName.setText(service.device.deviceName);
        }

    }
}
