package com.timezonescheduler.chronos.application.util;

//Simple tuple classes cause I don't want to deal with more dependencies
public class ChronosPair<K, V> {
    public K key;
    public V val;

    public ChronosPair(K k, V v) {
        key = k;
        val = v;
    }

    void setK(K k) {
        key = k;
    }

    void setV(V v) {
        val = v;
    }
}
