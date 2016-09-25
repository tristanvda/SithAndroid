package com.grietenenknapen.sithandroid.service;


import com.grietenenknapen.sithandroid.model.database.SithCard;

import java.util.List;

public interface SithCardService {

    void retrieveSithCardById(long id, ServiceCallBack<SithCard> serviceCallBack);

    void retrieveAllSithCards(ServiceCallBack<List<SithCard>> serviceCallBack);

    void putSithCard(SithCard sithCard, ServiceCallBack<Long> serviceCallBack);

    void removeSithCard(SithCard sithCard, ServiceCallBack<Void> serviceCallBack);
}
