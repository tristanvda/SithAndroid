package com.grietenenknapen.sithandroid.service;


import com.grietenenknapen.sithandroid.model.database.Player;

import java.util.List;

public interface PlayerService {

    void retrievePlayerById(long id, ServiceCallBack<Player> serviceCallBack);

    void retrieveAllPlayers(ServiceCallBack<List<Player>> serviceCallBack);

    void putPlayer(Player player, ServiceCallBack<Long> serviceCallBack);

    void removePlayer(Player player, ServiceCallBack<Void> serviceCallBack);
}
