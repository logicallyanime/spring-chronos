package com.timezonescheduler.chronos;

public class ChronosTriplet<K, X, Y> {
    public K key;
    public X val1;
    public Y val2;

    ChronosTriplet(K k, X x, Y y) {
        key = k;
        val1 = x;
        val2 = y;
    }

    void setK(K k) {
        key = k;
    }

    void setX(X x) {
        val1 = x;
    }

    void setY(Y y) {
        val2 = y;
    }
}
