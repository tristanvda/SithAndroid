package com.grietenenknapen.sithandroid.maingame.multiplayer.helper;

import java.util.Queue;

public interface QueueStrategy<T> {

    Queue<T> createQueue();

    void addToQueue(Queue<T> queue, T item);
}
