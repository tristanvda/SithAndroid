package com.grietenenknapen.sithandroid.service.storage;

import android.database.sqlite.SQLiteDatabase;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.service.ServiceCallBack;
import com.grietenenknapen.sithandroid.service.SithCardService;
import com.grietenenknapen.sithandroid.service.storage.SithDatabase;
import com.grietenenknapen.sithandroid.service.storage.tools.StorageBackgroundWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.QueryResultIterable;

public class SithCardServiceStorage implements SithCardService {
    private final Map<Long, SithCard> sithCardMap;
    private final SithDatabase sithDatabase;
    private final Cupboard cupboard;

    public SithCardServiceStorage(final SithDatabase sithDatabase, final Cupboard cupboard) {
        this.sithDatabase = sithDatabase;
        sithCardMap = Collections.synchronizedMap(new LinkedHashMap<Long, SithCard>());
        this.cupboard = cupboard;
    }

    @Override
    public void retrieveSithCardById(final long id, final ServiceCallBack<SithCard> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<SithCard>() {
            @Override
            public SithCard onExecute() {
                if (!sithCardMap.isEmpty() && sithCardMap.containsKey(id)) {
                    return sithCardMap.get(id);
                }
                final SQLiteDatabase db = sithDatabase.openDatabase();

                final SithCard sithCard = cupboard.withDatabase(db).get(SithCard.class, id);

                sithDatabase.closeDatabase();

                if (sithCard != null) {
                    sithCardMap.put(sithCard.getId(), sithCard);
                    return sithCard;
                } else {
                    return null;
                }
            }

            @Override
            public void postExecute(SithCard response) {
                if (response != null) {
                    serviceCallBack.onSuccess(response);
                } else {
                    serviceCallBack.onError(R.string.player_not_found);
                }
            }
        });
    }

    @Override
    public void retrieveAllSithCards(final ServiceCallBack<List<SithCard>> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<List<SithCard>>() {
            @Override
            public List<SithCard> onExecute() {
                if (!sithCardMap.isEmpty()) {
                    return new ArrayList<>(sithCardMap.values());
                }

                final SQLiteDatabase db = sithDatabase.openDatabase();

                QueryResultIterable<SithCard> sithCardsItr = null;
                List<SithCard> sithCards = null;

                boolean success = true;

                try {
                    sithCardsItr = cupboard.withDatabase(db).query(SithCard.class).query();

                    sithCards = sithCardsItr.list();
                    sithCardMap.clear();

                    for (SithCard sithCard : sithCards) {
                        sithCardMap.put(sithCard.getId(), sithCard);
                    }
                } catch (Exception e) {
                    success = false;
                } finally {
                    if (sithCardsItr != null) {
                        sithCardsItr.close();
                    }
                }

                sithDatabase.closeDatabase();

                if (success) {
                    return sithCards;
                } else {
                    return null;
                }
            }

            @Override
            public void postExecute(List<SithCard> response) {
                if (response != null) {
                    serviceCallBack.onSuccess(response);
                } else {
                    serviceCallBack.onError(R.string.service_fetch_data_error);
                }
            }
        });


    }

    @Override
    public void putSithCard(final SithCard sithCard, final ServiceCallBack<Long> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<Long>() {
            @Override
            public Long onExecute() {
                final SQLiteDatabase db = sithDatabase.openDatabase();
                final long id = cupboard.withDatabase(db).put(sithCard);

                sithCardMap.put(id, sithCard);

                sithDatabase.closeDatabase();

                return id;
            }

            @Override
            public void postExecute(Long response) {
                serviceCallBack.onSuccess(response);
            }
        });
    }

    @Override
    public void removeSithCard(final SithCard sithCard, final ServiceCallBack<Void> serviceCallBack) {
        StorageBackgroundWorker.executeTask(new StorageBackgroundWorker.BackgroundExecutionFactory<Void>() {
            @Override
            public Void onExecute() {
                final SQLiteDatabase db = sithDatabase.openDatabase();

                cupboard.withDatabase(db).delete(sithCard);
                sithCardMap.remove(sithCard.getId());

                sithDatabase.closeDatabase();

                return null;
            }

            @Override
            public void postExecute(Void response) {
                serviceCallBack.onSuccess(null);
            }
        });
    }
}
