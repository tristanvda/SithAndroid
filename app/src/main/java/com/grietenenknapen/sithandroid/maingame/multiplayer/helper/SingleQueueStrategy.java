package com.grietenenknapen.sithandroid.maingame.multiplayer.helper;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SingleQueueStrategy<T> implements QueueStrategy<T> {

    @Override
    public ConcurrentLinkedQueue<T> createQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    @Override
    public void addToQueue(final Queue<T> queue, final T item) {
        queue.clear();
        queue.add(item);
    }

}
