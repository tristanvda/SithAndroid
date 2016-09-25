package com.grietenenknapen.sithandroid.service;

import com.grietenenknapen.sithandroid.model.database.PlayedGame;

import java.util.List;

public interface PlayedGameService {
    void retrievePlayedGameById(long id, ServiceCallBack<PlayedGame> serviceCallBack);

    void retrieveAllPlayedGames(ServiceCallBack<List<PlayedGame>> serviceCallBack);

    void putPlayedGame(PlayedGame playedGame, ServiceCallBack<Long> serviceCallBack);

    void removePlayedGame(PlayedGame playedGame, ServiceCallBack<Void> serviceCallBack);
}
